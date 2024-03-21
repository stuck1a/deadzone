package deadzone.render;

import deadzone.Util;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;


enum Fonts {
  PLAY,
  PLAY_BOLD
}


public class FontRenderer {
  
  /** Stores the file names of all available fonts and links them with the corresponding Fonts enum item */
  static EnumMap<Fonts, String> fontMappings = new EnumMap<>(Fonts.class);
  
  private Font activeFont;
  
  
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
  
  }
  
}