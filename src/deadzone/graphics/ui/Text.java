package deadzone.graphics.ui;

import deadzone.Deadzone;
import deadzone.Window;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IRenderable;
import deadzone.graphics.VertexBufferObject;
import deadzone.graphics.fonts.Font;
import deadzone.graphics.fonts.Glyph;
import deadzone.math.Vector2;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


/**
 * Represents a rendered text.
 */
public class Text implements IRenderable {
  
  private static final int glRenderType = GL_TRIANGLES;
  /** List of all VBOs which will form this object in the GPU */
  final private ArrayList<VertexBufferObject> vboList = new ArrayList<>();
  private int vertexCount = 0;
  public String renderedText;
  public final Font font;
  public final float xPos;
  public final float yPos;
  private final float scale;
  
  final private Window window;
  
  /**
   * Total width of a rectangle around all textures of this text in display coordinates (0..1)
   */
  private int totalWidthPx = 0;
  
  /**
   * Total height of a rectangle around all textures of this text in display coordinates (0..1)
   */
  private int totalHeightPx = 0;
  
  
  
  
  
  public Text(float x, float y, Font font, String text) {
    window = Deadzone.getApplication().getWindow();
    this.xPos = x;
    this.yPos = y;
    this.font = font;
    renderedText = text;
    this.scale = 2;  // TODO: by default we need a scale of 2 the get the original cell size - why?
    this.vertexCount = renderedText.length() * 6;
    addTextToDraw(x, y);
  }
  
  public Text(float x, float y, Font font, String text, float scale) {
    window = Deadzone.getApplication().getWindow();
    this.xPos = x;
    this.yPos = y;
    this.font = font;
    renderedText = text;
    this.scale = scale + 1;
    this.vertexCount = renderedText.length() * 6;
    addTextToDraw(x, y);
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
  public ArrayList<VertexBufferObject> getVBOs() {
    return this.vboList;
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
  
  public float getTotalWidth() {
    return ((float)totalWidthPx) / window.getPixelWidth();
  }
  
  public float getTotalHeight() {
    return ((float)totalHeightPx) / window.getPixelHeight();
  }
  
  
  /**
   * Registers all required VBOs at the renderer which are needed to render the given text.
   */
  private void addTextToDraw(float x, float y) {
    final int windowWidth = window.getPixelWidth();
    final int windowHeight = window.getPixelHeight();
    
    // Normalize color data
    final Color color = font.getColor();
    float red = color.getRedNormalized();
    float green = color.getGreenNormalized();
    float blue = color.getBlueNormalized();
    float alpha = color.getAlphaNormalized();
  
    final Texture fontAtlas = font.getAtlasTexture();
    float totalWidth = 0, totalHeight = 0;
    Vector2 currentPenPos = null;
  
    // Iterate through the given text and create a texture (2 triangles each) at the correct location for each letter
    for (int i = 0; i < renderedText.length(); i++){
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
      
      // First iteration: init pen - all other: fetch kerning value
      float letterOffset = 0, letterOffsetPx = 0;
      if (currentPenPos == null) {
        currentPenPos = new Vector2(x, y);
      } else {
        final char predecessor = renderedText.charAt(i-1);
        letterOffsetPx = scale * glyph.getKerning(predecessor);
        letterOffset = letterOffsetPx / windowWidth;
      }
      
      // Update total size
      totalHeight = Math.max(totalHeight, size.y);
      totalWidth += size.x + letterOffsetPx;
      
      // Create and register VBOs for this character
      VertexBufferObject vbo1 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          currentPenPos.x,                 currentPenPos.y + sizeY_NormWin, red, green, blue, alpha, posX_NormTex,                 posY_NormTex,
          currentPenPos.x,                 currentPenPos.y,                 red, green, blue, alpha, posX_NormTex,                 posY_NormTex + sizeY_NormTex,
          currentPenPos.x + sizeX_NormWin, currentPenPos.y + sizeY_NormWin, red, green, blue, alpha, posX_NormTex + sizeX_NormTex, posY_NormTex,
        }
      );
      vboList.add(vbo1);
      
      VertexBufferObject vbo2 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          currentPenPos.x + sizeX_NormWin, currentPenPos.y,                 red, green, blue, alpha, posX_NormTex + sizeX_NormTex, posY_NormTex + sizeY_NormTex,
          currentPenPos.x + sizeX_NormWin, currentPenPos.y + sizeY_NormWin, red, green, blue, alpha, posX_NormTex + sizeX_NormTex, posY_NormTex,
          currentPenPos.x,                 currentPenPos.y,                 red, green, blue, alpha, posX_NormTex,                 posY_NormTex + sizeY_NormTex,
        }
      );
      vboList.add(vbo2);

      // Update pen position for the next character
      currentPenPos.x += sizeX_NormWin + letterOffset;
    }
    
    // Store total size as class fields
    totalHeightPx = (int) Math.ceil(totalHeight * scale);
    totalWidthPx = (int) Math.ceil(totalWidth);
  }
  
  
}
