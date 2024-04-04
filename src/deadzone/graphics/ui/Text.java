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
      final float posX_NormWin = pos.x / windowWidth;
      final float posY_NormWin = pos.y / windowHeight;
      final float sizeX_NormWin = size.x * scale / windowWidth;
      final float sizeY_NormWin = size.y * scale / windowHeight;
      final float width_NormWin = width / windowWidth;
  
      // Normalize position and size for UV data (percent of texture atlas size)
      final float posX_NormTex = pos.x / fontAtlas.width;
      final float posY_NormTex = pos.y / fontAtlas.height;
      final float sizeX_NormTex =  size.x / fontAtlas.width;
      final float sizeY_NormTex = size.y / fontAtlas.height;
      
      // Initialize / update current pen position
      if (currentPenPos == null) {
        currentPenPos = new Vector2(x, y);
      } else {
        final float letterOffset = 2f / windowWidth; // 2px offset between each letter
        currentPenPos.x = currentPenPos.x + scale * (width_NormWin + letterOffset);
      }
      
      // Update total size of the text
      totalHeight = sizeY_NormWin;
      totalWidth += sizeX_NormWin;
      
      // Create and register VBOs for this character
      float x_A, y_A, x_B, y_B, x_C, y_C, u_A, v_A, u_B, v_B, u_C, v_C;
      
      x_A = currentPenPos.x;
      y_A = currentPenPos.y + sizeY_NormWin;
      
      x_B = currentPenPos.x;
      y_B = currentPenPos.y;
      
      x_C = currentPenPos.x + sizeX_NormWin;
      y_C = currentPenPos.y + sizeY_NormWin;
      
      u_A = posX_NormTex;
      v_A = posY_NormTex;
      
      u_B = posX_NormTex;
      v_B = posY_NormTex + sizeY_NormTex;
      
      u_C = posX_NormTex + sizeX_NormTex;
      v_C = posY_NormTex;
      
      VertexBufferObject vbo1 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          x_A, y_A, red, green, blue, alpha, u_A, v_A,
          x_B, y_B, red, green, blue, alpha, u_B, v_B,
          x_C, y_C, red, green, blue, alpha, u_C, v_C,
        }
      );
      vboList.add(vbo1);
      
      System.out.println(
        "\"" + c + "\"\n" +
          "(A)\t" + "(" + x_A + "|" + y_A + ")\t(" + u_A + "|" + v_A + ")\n" +
          "(B)\t" + "(" + x_B + "|" + y_B + ")\t(" + u_B + "|" + v_B + ")\n" +
          "(C)\t" + "(" + x_C + "|" + y_C + ")\t(" + u_C + "|" + v_C + ")\n"
      );
      
      
      
  
  
      x_A = currentPenPos.x + sizeX_NormWin;
      y_A = currentPenPos.y;
  
      x_B = currentPenPos.x + sizeX_NormWin;
      y_B = currentPenPos.y + sizeY_NormWin;
  
      x_C = currentPenPos.x;
      y_C = currentPenPos.y;
  
      u_A = posX_NormTex + sizeX_NormTex;
      v_A = posY_NormTex + sizeY_NormTex;
  
      u_B = posX_NormTex + sizeX_NormTex;
      v_B = posY_NormTex;
      
      u_C = posX_NormTex;
      v_C = posY_NormTex + sizeY_NormTex;
  

      
      VertexBufferObject vbo2 = new VertexBufferObject(
        false,
        fontAtlas,
        new float[] {
          x_A, y_A, red, green, blue, alpha, u_A, v_A,
          x_B, y_B, red, green, blue, alpha, u_B, v_B,
          x_C, y_C, red, green, blue, alpha, u_C, v_C,
        }
      );
      vboList.add(vbo2);
  
  
      System.out.println(
        "\"" + c + "\"\n" +
        "(A')\t" + "(" + x_A + "|" + y_A + ")\t(" + u_A + "|" + v_A + ")\n" +
        "(B')\t" + "(" + x_B + "|" + y_B + ")\t(" + u_B + "|" + v_B + ")\n" +
        "(C')\t" + "(" + x_C + "|" + y_C + ")\t(" + u_C + "|" + v_C + ")\n"
      );
      
      System.out.println("Glyph UV in Pixel: " + glyph.getPosition().x + " | " + glyph.getPosition().y + "\n");
    }
    
  }
  
}
