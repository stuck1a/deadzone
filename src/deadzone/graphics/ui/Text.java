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
      
      // Normalize position and size for XYZ data (percent of window size)
      final float posX_Norm = pos.x / windowWidth;
      final float posY_Norm = pos.y / windowHeight;
      final float sizeX_Norm = size.x * scale / windowWidth;
      final float sizeY_Norm = size.y * scale / windowHeight;
      final float width_Norm = width / windowWidth;
  
      // Normalize position and size for UV data (percent of texture atlas size)
      final float posX_NormOnTexture = pos.x / fontAtlas.width;
      final float posY_NormOnTexture = pos.y / fontAtlas.height;
      final float sizeX_NormOnTexture =  size.x / fontAtlas.width;
      final float sizeY_NormOnTexture = size.y / fontAtlas.height;
      
      // Initialize / update current pen position
      if (currentPenPos == null) {
        currentPenPos = new Vector2(x, y);
      } else {
        currentPenPos.x = currentPenPos.x + sizeX_Norm;
      }
      
      // Update total size of the text
      totalHeight = sizeY_Norm;
      totalWidth += sizeX_Norm;
      
      
      /*
       * TESTDATEN FÜR "H"
       * Korrekte normalisierte UV-Werte wären:
       *
       * Größe Atlas Image (BxH):   240x486
       *
       * A  = 24 | 108 px = 0,1        | 0,2222222
       * B  = 24 | 134 px = 0,1        | 0,2757201
       * C  = 47 | 108 px = 0,1958333  | 0,2222222
       *
       * A' = 47 | 134 px = 0,1958333  | 0,2757201
       * B' = 24 | 134 px = 0,1        | 0,2757201
       * C' = 47 | 108 px = 0,1958333  | 0,18
       *
       */
      
      // Create and register VBOs for this character
      VertexBufferObject vbo1 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          // x                              y                              R    G      B     A       U                        V
          currentPenPos.x,               currentPenPos.y + sizeY_Norm,  red, green, blue, alpha,  posX_NormOnTexture,               posY_NormOnTexture,
          currentPenPos.x,               currentPenPos.y,               red, green, blue, alpha,  posX_NormOnTexture,               posY_NormOnTexture + sizeY_NormOnTexture,
          currentPenPos.x + sizeX_Norm,  currentPenPos.y + sizeY_Norm,  red, green, blue, alpha,  posX_NormOnTexture + sizeX_NormOnTexture,  posY_NormOnTexture,
        }
//      new float[] {
//        // x                              y                              R    G      B     A       U                        V
//        currentPenPos.x,               currentPenPos.y + sizeY_Norm,  red, green, blue, alpha, 0.1f,  0.2222222f,
//        currentPenPos.x,               currentPenPos.y,               red, green, blue, alpha,  0.1f, 0.2757201f,
//        currentPenPos.x + sizeX_Norm,  currentPenPos.y + sizeY_Norm,  red, green, blue, alpha,   0.1958333f, 0.2222222f,
//      }
      );
      vboList.add(vbo1);
      
      VertexBufferObject vbo2 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          // x                              y                              R    G      B     A       U                        V
          currentPenPos.x + sizeX_Norm,  currentPenPos.y,               red, green, blue, alpha,  posX_NormOnTexture + sizeX_NormOnTexture,  posY_NormOnTexture,
          currentPenPos.x,               currentPenPos.y,               red, green, blue, alpha,  posX_NormOnTexture,               posY_NormOnTexture + sizeY_NormOnTexture,
          currentPenPos.x + sizeX_Norm,  currentPenPos.y + sizeY_Norm,  red, green, blue, alpha,  posX_NormOnTexture + sizeX_NormOnTexture,  posY_NormOnTexture,
        }
//      new float[] {
//        // x                              y                              R    G      B     A       U                        V
//        currentPenPos.x + sizeX_Norm,  currentPenPos.y,               red, green, blue, alpha,   0.1958333f, 0.2757201f,
//        currentPenPos.x,               currentPenPos.y,               red, green, blue, alpha,  0.1f, 0.2757201f,
//        currentPenPos.x + sizeX_Norm,  currentPenPos.y + sizeY_Norm,  red, green, blue, alpha,    0.1958333f, 0.2222222f,
//      }
      );
      vboList.add(vbo2);
      
      
      String debugMsg =
        "\"" + c + "\"\n" +
        "(A)\t" + "(" + (currentPenPos.x) + "|" + (currentPenPos.y + size.y) + ")\t(" + (pos.x) + "|" + (pos.y + size.y) + ")\n" +
        "(B)\t" + "(" + (currentPenPos.x) + "|" + (currentPenPos.y) + ")\t(" + (pos.x) + "|" + (pos.y) + ")\n" +
        "(C)\t" + "(" + (currentPenPos.x + size.x) + "|" + (currentPenPos.y + size.y) + ")\t(" + (pos.x + size.x) + "|" + (pos.y + size.y) + ")\n" +
        "(A')\t" + "(" + (currentPenPos.x + size.x) + "|" + (currentPenPos.y) + ")\t(" + (pos.x + size.x) + "|" + (pos.y) + ")\n" +
        "(B')\t" + "(" + (currentPenPos.x) + "|" + (currentPenPos.y) + ")\t(" + (pos.x) + "|" + (pos.y) + ")\n" +
        "(C')\t" + "(" + (currentPenPos.x + size.x) + "|" + (currentPenPos.y + size.y) + ")\t(" + (pos.x + size.x) + "|" + (pos.y + size.y) + ")";
  
      System.out.println(debugMsg);
      System.out.println("Glyph UV in Pixel: " + glyph.getPosition().x + " | " + glyph.getPosition().y + "\n");
      
      
      
      // TODO Vor dem rendern muss die Textur Render-Art von Stretch umgestellt werden auf clamp
      //      Notfalls müssen die vbos von texten in ein eigenes vao gebunden werden
    }
    
  }
  
}
