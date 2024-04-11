package deadzone.graphics.ui;

import deadzone.Deadzone;
import deadzone.Window;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IRenderable;
import deadzone.graphics.VertexBufferObject;
import deadzone.graphics.fonts.Font;
import deadzone.graphics.fonts.Glyph;
import deadzone.graphics.fonts.Pen;
import deadzone.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


/**
 * Represents a rendered text.
 */
public class Text implements IRenderable {
  
  private final static int glRenderType = GL_TRIANGLES;
  private VertexBufferObject vbo;
  protected int vertexCount;
  public final Font font;
  public Color color;
  protected String renderedText;
  protected final float xPos;
  protected final float yPos;
  protected final float scale;
  protected int lineCount = 1;
  private final Pen pen;
  final private Window window;
  /**
   * Total width of a rectangle around all textures of this text in display coordinates (0..1)
   */
  private int totalWidthPx = 0;
  /**
   * Total height of a rectangle around all textures of this text in display coordinates (0..1)
   */
  private int totalHeightPx = 0;
  
  
  
  public Text(Pen pen, Font font, String text, Color color, float scale) {
    this.window = Deadzone.getApplication().getWindow();
    this.pen = pen;
    this.xPos = pen.getX();
    this.yPos = pen.getY();
    this.font = font;
    this.renderedText = text;
    this.color = color;
    this.scale = scale + 1;
    this.vertexCount = renderedText.length() * 6;
  }
  
  
  /**
   * Returns the OpenGL object type used for rendered text
   */
  @Override
  public int getGL_TYPE() {
    return glRenderType;
  }
  
  /**
   * Returns the pointer to all VBOs which form this text together.
   * If there are no VBOs yet initialized, they will be initialized and bound on-the-fly
   */
  @Override
  public VertexBufferObject getVBO() {
    if (vbo == null) {
      generateVBO();
    }
    return this.vbo;
  }
  
  /**
   * Returns the total count of vertices the object have to form the text texture meshes
   * Each letter is printed on a rectangle and therefore uses 2 triangle meshes.
   */
  @Override
  public int getVertexCount() {
    return vertexCount;
  }
  
  public int getTotalPixelWidth() {
    return totalWidthPx;
  }
  
  public int getTotalPixelHeight() {
    return totalHeightPx;
  }
  
  public float getX() {
    return xPos;
  }
  
  public float getY() {
    return yPos;
  }
  
  public float getScale() {
    return scale;
  }
  
  public float getTotalWidth() {
    return ((float)totalWidthPx) / window.getPixelWidth();
  }
  
  public float getTotalHeight() {
    return ((float)totalHeightPx) / window.getPixelHeight();
  }
  
  public void setText(String newText) {
    renderedText = newText;
    vertexCount = newText.length() * 6;
  }
  
  public String getText() {
    return renderedText;
  }
  
  public int getLineCount() {
    return lineCount;
  }
  
  
  /**
   * Registers all required VBOs at the renderer which are needed to render the given text.
   */
  private void generateVBO() {
    final int windowWidth = window.getPixelWidth();
    final int windowHeight = window.getPixelHeight();
    final Vector2 initialPenPos = new Vector2(pen.getX(), pen.getY());
    float red = color.getRedNormalized();
    float green = color.getGreenNormalized();
    float blue = color.getBlueNormalized();
    float alpha = color.getAlphaNormalized();
    final Texture fontAtlas = font.getAtlasTexture();
    float totalWidth = 0, pxWidthOfLongestLine = 0, lineHeight = 0;
    ArrayList<Float> vertexData = new ArrayList<>();
    
    // Iterate through the given text and create a texture (2 triangles each) at the correct location for each letter
    for (int i = 0; i < renderedText.length(); i++) {
      float letterOffsetPx = 0;
      
      // Get the glyph data
      final char c = renderedText.charAt(i);
      final Glyph glyph = font.getGlyph(c);
      final Vector2 pos = glyph.getPosition();
      final Vector2 size = glyph.getSize();
      
      // Normalize position and size for XYZ data (percent of window size)
      final float sizeX_NormWin = size.x * scale / windowWidth;
      final float sizeY_NormWin = size.y * scale / windowHeight;
      
      // Normalize position and size for UV data (percent of texture atlas size)
      final float posX_NormTex = pos.x / fontAtlas.width;
      final float posY_NormTex = pos.y / fontAtlas.height;
      final float sizeX_NormTex =  size.x / fontAtlas.width;
      final float sizeY_NormTex = size.y / fontAtlas.height;
      
      // First iteration: init pen (origin to top left), store line height  - all other: fetch kerning value
      if (i == 0) {
        // In first iteration: set pen to origin (top left) and store the line height
        pen.setY(initialPenPos.y - sizeY_NormWin);
        lineHeight = size.y;
      } else {
        // In all other iterations: get value of the kerning pair "last char" - "current char"
        letterOffsetPx = scale * glyph.getKerning(renderedText.charAt(i-1));
      }
  
      // Update total width
      totalWidth += size.x + letterOffsetPx;
      
      // If the current letter is a line break start new line and draw nothing
      if (c == 10) {
        lineCount++;
        pen.setPos(initialPenPos.x, pen.getY() - sizeY_NormWin);
        // Reset total width to measure width of the new line
        totalWidth = 0;
        continue;
      }
      
      // Collect the vertex data for this character
      final Vector2 currentPenPos = pen.getPos();
      vertexData.addAll(new ArrayList<>( Arrays.asList(
        // Triangle 1
        currentPenPos.x, currentPenPos.y + sizeY_NormWin, red, green, blue, alpha, posX_NormTex, posY_NormTex,
        currentPenPos.x, currentPenPos.y, red, green, blue, alpha, posX_NormTex, posY_NormTex + sizeY_NormTex,
        currentPenPos.x + sizeX_NormWin, currentPenPos.y + sizeY_NormWin, red, green, blue, alpha, posX_NormTex + sizeX_NormTex, posY_NormTex,
        // Triangle 2
        currentPenPos.x + sizeX_NormWin, currentPenPos.y, red, green, blue, alpha, posX_NormTex + sizeX_NormTex, posY_NormTex + sizeY_NormTex,
        currentPenPos.x + sizeX_NormWin, currentPenPos.y + sizeY_NormWin, red, green, blue, alpha, posX_NormTex + sizeX_NormTex, posY_NormTex,
        currentPenPos.x, currentPenPos.y, red, green, blue, alpha, posX_NormTex, posY_NormTex + sizeY_NormTex
      )));
      
      
      // Prepare pen pos for next character and update total width, if this was the longest line by now
      pen.setX(currentPenPos.x + sizeX_NormWin + letterOffsetPx / windowWidth);
      pxWidthOfLongestLine = Math.max(pxWidthOfLongestLine, totalWidth);
    }
    
    
    // Generate a single VBO for the whole text
    final float[] vertexDataArr = new float[vertexData.size()];
    int index = 0;
    for (final Float value: vertexData) {
      vertexDataArr[index++] = value;
    }
    
    vbo = new VertexBufferObject(false, fontAtlas, vertexDataArr);
    
    // Finally store the texts total size
    totalWidthPx = (int) Math.ceil(pxWidthOfLongestLine * scale);
    totalHeightPx = (int) Math.ceil(lineHeight * lineCount * scale);
    
    // Since we manipulated the pen pos origin within this function we must revert this otherwise the "global" pen pos would get an offset
    pen.setY(pen.getY() + 2 * lineHeight / windowHeight);  // works, but why the hell x2 ??
  }
  
  
}
