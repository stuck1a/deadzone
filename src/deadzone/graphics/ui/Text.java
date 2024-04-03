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
  private ArrayList<VertexBufferObject> vboList;
  private int vertexCount = 0;
  public String renderedText;
  public final Font font;
  public final float xPos;
  public final float yPos;
  private final float scale;
  
  private Window window;
  
  /**
   * Total width of a rectangle around all textures of this text in display coordinates (0..1)
   */
  private float totalWidth = 0;
  
  /**
   * Total height of a rectangle around all textures of this text in display coordinates (0..1)
   */
  private float totalHeight = 0;
  
  
  
  
  
  public Text(float x, float y, Font font, String text) {
    window = Deadzone.getApplication().getWindow();
    this.xPos = x;
    this.yPos = y;
    this.font = font;
    renderedText = text;
    this.scale = 1;
    this.vertexCount = renderedText.length() * 6;
    addTextToDraw(x, y);
  }
  
  public Text(float x, float y, Font font, String text, float scale) {
    window = Deadzone.getApplication().getWindow();
    this.xPos = x;
    this.yPos = y;
    this.font = font;
    renderedText = text;
    this.scale = scale;
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
  
  public float getTotalWidth() {
    return totalWidth;
  }
  
  public float getTotalHeight() {
    return totalHeight;
  }
  
  public int getTotalPixelWidth() {
    return (int)(totalWidth * window.getPixelWidth());
  }
  
  public int getTotalPixelHeight() {
    return (int)(totalHeight * window.getPixelHeight());
  }
  
  /**
   * Registers all required VBOs at the renderer which are needed to render the given text.
   * For now, we use normalized OpenGL coordinates for x and y to ensure resolution-independent placement
   */
  private void addTextToDraw(float x, float y) {
    final int windowWidth = window.getPixelWidth();
    final int windowHeight = window.getPixelHeight();
    
    // Load the texture
    final Texture fontAtlas = font.getAtlasTexture();
    
    // Normalize the color data
    final Color color = font.getColor();
    float red = color.getRedNormalized();
    float green = color.getGreenNormalized();
    float blue = color.getBlueNormalized();
    float alpha = color.getAlphaNormalized();
    
    // Set color to white for testing
    red = 1.0f;
    green = 1.0f;
    blue = 1.0f;
    alpha = 1.0f;
  
    vboList = new ArrayList<>();
    
    // Iterate through the given text and create a texture (2 triangles each) at the correct location for each letter
    Vector2 currentPenPos = null;
    
    for (int i = 0; i < renderedText.length(); i++){
      // Get the glyph data
      final char c = renderedText.charAt(i);
      Glyph glyph = font.getGlyph(c);
      Vector2 pos = glyph.getPosition();
      Vector2 size = glyph.getSize();
      float width = glyph.getGlyphWidth();
      // Normalize pixel values to use them in OpenGL
      final float posX_Norm = pos.x / windowWidth;
      final float posY_Norm = pos.y / windowHeight;
      final float sizeX_Norm = size.x / windowWidth;
      final float sizeY_Norm = size.y / windowHeight;
      final float width_Norm = width / windowWidth;
      
      // Initialize / update current pen position
      if (currentPenPos == null) {
        currentPenPos = new Vector2(x, y);
      } else {
        currentPenPos.x += sizeX_Norm;
      }
      
      // Update total size of the text
      totalHeight = sizeY_Norm;
      totalWidth += sizeX_Norm;
      
      // Create and register VBOs for this character

      // We calculate the UV coordinates from the known glyph position within the atlas image and the position from their size
      // For now, we just use single row text, but later we will add a max width (and maybe max height) for the drawn text
      VertexBufferObject vbo1 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
        // x                         y                         R    G      B     A      U               V
           currentPenPos.x,          currentPenPos.y + sizeY_Norm, red, green, blue, alpha, posX_Norm,          posY_Norm + sizeY_Norm,
           currentPenPos.x,          currentPenPos.y,          red, green, blue, alpha, posX_Norm,          posY_Norm,
           currentPenPos.x + sizeX_Norm, currentPenPos.y + sizeY_Norm, red, green, blue, alpha, posX_Norm + sizeX_Norm, posY_Norm + sizeY_Norm
        }
      );
      vboList.add(vbo1);
      
      VertexBufferObject vbo2 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          currentPenPos.x + sizeX_Norm, currentPenPos.y,          red, green, blue, alpha, posX_Norm + sizeX_Norm, posY_Norm,
          currentPenPos.x,          currentPenPos.y,          red, green, blue, alpha, posX_Norm,          posY_Norm,
          currentPenPos.x + sizeX_Norm, currentPenPos.y + sizeY_Norm, red, green, blue, alpha, posX_Norm + sizeX_Norm, posY_Norm + sizeY_Norm
        }
      );
      vboList.add(vbo2);
      
      
      String debugMsg =
        "\"" + c + "\"\n" +
        "(A)\t" + "(" + (currentPenPos.x) + "|" + (currentPenPos.y + size.y) + ")\t(" + (pos.x) + "|" + (pos.y + size.y) + ")\n" +
        "(B)\t" + "(" + (currentPenPos.x) + "|" + (currentPenPos.y) + ")\t(" + (pos.x) + "|" + (pos.y) + ")\n" +
        "(C)\t" + "(" + (currentPenPos.x + size.x) + "|" + (currentPenPos.y + size.y) + ")\t(" + (pos.x + size.x) + "|" + (pos.y + size.y) + ")\n" +
        "(A')\t" + "(" + (currentPenPos.x + size.x) + "|" + (currentPenPos.y) + ")\t(" + (pos.x + size.x) + "|" + (pos.y) + ")\n" +
        "(B')\t" + "(" + (currentPenPos.x) + "|" + (currentPenPos.y) + ")\t(" + (pos.x) + "|" + (pos.y) + ")\n" +
        "(C')\t" + "(" + (currentPenPos.x + size.x) + "|" + (currentPenPos.y + size.y) + ")\t(" + (pos.x + size.x) + "|" + (pos.y + size.y) + ")\n\n";
  
      System.out.println(debugMsg);
      
      // TODO Vor dem rendern muss die Textur Render-Art von Stretch umgestellt werden auf clamp
      //      Notfalls müssen die vbos von texten in ein eigenes vao gebunden werden
    }
    
  }
  
}
