package deadzone.render;

import deadzone.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


enum Fonts {
  PLAY,
  PLAY_BOLD
}


public class FontRenderer {
  
  /** Stores the file names of all available fonts and links them with the corresponding Fonts enum item */
  static EnumMap<Fonts, String> fontMappings = new EnumMap<>(Fonts.class);
  
  private Font activeFont;
  
  
  // Holds the height to the tallest character of the initialized font
  private int fontHeight;
  
  // Store the position of each character on the atlas texture of the initialized font
  Map<Character, Glyph> glyphs = new HashMap<>();
  
  
  
  static {
    fontMappings.put(Fonts.PLAY, "Play-Regular.ttf");      // default font
    fontMappings.put(Fonts.PLAY_BOLD, "Play-Bold.ttf");
  }
  
  
  /**
   * Creates a new Font renderer
   */
  public FontRenderer() {
    // Use default font
    String fontPath = getFontPile(Fonts.PLAY);
    File fontFile = new File(fontPath);
    try {
      activeFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
    } catch (IOException | FontFormatException e) {
      System.err.println("Failed to create font from file '" + fontPath + "'\n" + e.getMessage());
    }
  
    generateFontTexture();
  }
  
  /**
   * Creates a new Font renderer
   * @param font Target font to use
   */
  public FontRenderer(Fonts font) {
  
    String fontPath = getFontPile(font);
    File fontFile = new File(fontPath);
    try {
      activeFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
    } catch (IOException | FontFormatException e) {
      System.err.println("Failed to create font from file '" + fontPath + "'\n" + e.getMessage());
    }
  
    generateFontTexture();
  }
  
  
  public void writeText(int x, int y, Font font, String text) {
    setActiveFont(font);
    renderText(x, y, text);
  }
  
  public void writeText(int x, int y, String text) {
    // Make sure an active font is set
    if (activeFont == null) {
      System.err.println("Failed to write text, because no active Font was set!");
      return;
    }
    renderText(x, y, text);
  }
  
  public void setActiveFont(Font font) {
    this.activeFont = font;
  }
  
  
  /**
   * @param font Target font
   * @return Absolute path of the target font file
   */
  private String getFontPile(Fonts font) {
    return Util.getAbsoluteFontsDir() + fontMappings.get(font);
  }
  
  private void renderText(int x, int y, String text) {
    // Get the number of lines
    int lines = 1;
    for(int i = 0; i < text.length(); i++) {
      char ch = text.charAt(i);
      if(ch == '\n') {
        lines++;
      }
    }
    int textHeight = lines * fontHeight;
  
    // Find the correct y position to start drawing the chars
    float drawX = x;
    float drawY = y;
    if (textHeight > fontHeight) {
      drawY += textHeight - fontHeight;
    }
    
    // Go over each char, fetch its glyph info from the glyph map and draw it with that info
    Texture texture = new Texture();
    Renderer renderer = new Renderer();
    
    texture.bind();
    renderer.begin();
    for (int i = 0; i < text.length(); i++) {
      char ch = text.charAt(i);
      if (ch == '\n') {
        // Line feed, set x and y to draw at the next line
        drawY -= fontHeight;
        drawX = x;
        continue;
      }
      if (ch == '\r') {
        continue;   // Carriage return, just skip it
      }
      Glyph g = glyphs.get(ch);
      renderer.drawTextureRegion(texture, drawX, drawY, g.x, g.y, g.width, g.height);
      drawX += g.width;
    }
    renderer.end();
  }
  
  
  /**
   * This initializes the active font by creating a font texture
   * with all printable characters of the font.
   * DAS SOLLTE NUR EINMAL PRO FONT GEMACHT WERDEN UND DANN GESPEICHERT WERDEN
   *
   */
  private void generateFontTexture() {
    // Measure each char to determine the total width and height for our font atlas texture
    int imageWidth = 0;
    int imageHeight = 0;
    final boolean antiAlias = true;
    
    for (int i = 32; i < 256; i++) {
      if (i == 127) {
        continue;  // 127 = DEL
      }
      char c = (char) i;
      BufferedImage ch = createCharImage(activeFont, c, antiAlias);
      
      imageWidth += ch.getWidth();
      imageHeight = Math.max(imageHeight, ch.getHeight());
    }
  
    // Create the graphics object to draw the atlas texture on it
    fontHeight = imageHeight;
    BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    

    Map<Character, Glyph> glyphs = new HashMap<>();
    
    // Generate the texture
    int offset = 5;  // Offset between character on the atlas texture
    for (int i = 32; i < 256; i++) {
      if (i == 127) {
        continue;  // 127 = DEL
      }
      char c = (char) i;
      BufferedImage charImage = createCharImage(activeFont, c, antiAlias);
  
      assert charImage != null;
      int charWidth = charImage.getWidth();
      int charHeight = charImage.getHeight();
  
      // Draw char on texture
      Glyph ch = new Glyph(charWidth, charHeight, offset, image.getHeight() - charHeight);
      g.drawImage(charImage, offset, 0, null);
      offset += ch.width;
      // Add Glyph to map
      glyphs.put(c, ch);
    }
    
    
  }
  
  private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias) {
    // Creating temporary image to extract character size
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    if (antiAlias) {
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    g.setFont(font);
    FontMetrics metrics = g.getFontMetrics();
    g.dispose();
    
    // Get char width and height
    int charWidth = metrics.charWidth(c);
    int charHeight = metrics.getHeight();
    
    // Make sure we got no zero-width character
    if (charWidth == 0) {
      return null;
    }
    
    // Create image which holds the char
    image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
    g = image.createGraphics();
    if (antiAlias) {
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    g.setFont(font);
    g.setPaint(java.awt.Color.WHITE);
    g.drawString(String.valueOf(c), 0, metrics.getAscent());
    g.dispose();
    return image;
  }
  
  
  /**
   * Gets the width of the specified text.
   *
   * @param text The text
   *
   * @return Width of text
   */
  public int getWidth(CharSequence text) {
    int width = 0;
    int lineWidth = 0;
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == '\n') {
        /* Line end, set width to maximum from line width and stored
         * width */
        width = Math.max(width, lineWidth);
        lineWidth = 0;
        continue;
      }
      if (c == '\r') {
        /* Carriage return, just skip it */
        continue;
      }
      Glyph g = glyphs.get(c);
      lineWidth += g.width;
    }
    width = Math.max(width, lineWidth);
    return width;
  }
  
  /**
   * Gets the height of the specified text.
   *
   * @param text The text
   *
   * @return Height of text
   */
  public int getHeight(CharSequence text) {
    int height = 0;
    int lineHeight = 0;
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == '\n') {
        /* Line end, add line height to stored height */
        height += lineHeight;
        lineHeight = 0;
        continue;
      }
      if (c == '\r') {
        /* Carriage return, just skip it */
        continue;
      }
      Glyph g = glyphs.get(c);
      lineHeight = Math.max(lineHeight, g.height);
    }
    height += lineHeight;
    return height;
  }
  
}