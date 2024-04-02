package deadzone.graphics.ui;

import deadzone.Deadzone;
import deadzone.Window;
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
  
  
  
  
  public Text(float x, float y, Font font, String text) {
    this.xPos = x;
    this.yPos = y;
    this.font = font;
    renderedText = text;
    this.scale = 1;
    this.vertexCount = renderedText.length() * 6;
    addTextToDraw(x, y);
  }
  
  public Text(float x, float y, Font font, String text, float scale) {
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
  
  
  /**
   * Registers all required VBOs at the renderer which are needed to render the given text.
   * For now, we use normalized OpenGL coordinates for x and y to ensure resolution-independent placement
   */
  private void addTextToDraw(float x, float y) {
    final Window window = Deadzone.getApplication().getWindow();
    final int windowWidth = window.getPixelWidth();
    final int windowHeight = window.getPixelHeight();
    
    // Normalize the color data
    final Color color = font.getColor();
    final float red = color.getRedNormalized();
    final float green = color.getGreenNormalized();
    final float blue = color.getBlueNormalized();
    final float alpha = color.getAlphaNormalized();
    
    // Iterate through the given text and create a texture (2 triangles each) at the correct location for each letter
    for (int i = 0; i < renderedText.length(); i++){
      // Get the glyph data
      final char c = renderedText.charAt(i);
      Glyph glyph = font.getGlyph(c);
      Vector2 pos = glyph.getPosition();
      Vector2 size = glyph.getSize();
      float width = glyph.getGlyphWidth();
      // Normalize pixel values to use them in OpenGL
      pos.x /= windowWidth;
      pos.y /= windowHeight;
      size.x /= windowWidth;
      size.y /= windowHeight;
      width /= windowWidth;
      
      // Create and register VBOs for this character  // TODO: Either rewrite class Texture so we can use them for the atlas image too or create a FontTexture class
      vboList = new ArrayList<>();
      // We calculate the UV coordinates from the known glyph position within the atlas image and the position from their size
      // For now, we just use single row text, but later we will add a max width (and maybe max height) for the drawn text
//      VertexBufferObject vbo1 = new VertexBufferObject(
//        false,
//        font.getAtlasTexture(),
//        new float[] {
           // x y z  - R G B A - U V
           // TODO
//        }
//      );
//      vboList.add(vbo1);
//
//      VertexBufferObject vbo2 = new VertexBufferObject(
//        false,
//        font.getAtlasTexture(),
//        new float[] {
//
//        }
//      );
//      vboList.add(vbo2);
    }
    
  }
  
}
