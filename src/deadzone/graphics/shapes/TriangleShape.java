package deadzone.graphics.shapes;

import deadzone.Deadzone;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IShape;
import deadzone.graphics.VertexBufferObject;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


/**
 * This class represents a triangle.<br>
 * The edge points are defined as follows:<br>
 * ##############<br>
 * ###### A #####<br>
 * ##### /\ #####<br>
 * #### /**\ ####<br>
 * ### /****\ ###<br>
 * ## /______\ ##<br>
 * # B ###### C #<br>
 * ##############
 */
public class TriangleShape implements IShape {
  
  /** The mesh type so the renderer knows to which VAO the objects VBOs must be attached */
  private static final int glRenderType = GL_TRIANGLES;
  
  /** The sum of vertices of all VBOs of the object */
  private static final int vertexCount = 3;
  
  private VertexBufferObject vbo;
  
  private float x1;
  private float y1;
  private float x2;
  private float y2;
  private float x3;
  private float y3;
  private Color color;
  private Texture texture;
  
  
  /**
   * Creates a new colored triangle shape.
   * For now, we use already normalized values for x and y as input.
   * TODO: As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public TriangleShape(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.x3 = x3;
    this.y3 = y3;
    this.color = color;
    this.texture = Deadzone.getApplication().getAssetManager().getTexture("blank");
  }
  
  /**
   * Creates a new textured triangle shape.
   * For now, we use already normalized values for x and y as input.
   * TODO: As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public TriangleShape(float x1, float y1, float x2, float y2, float x3, float y3, Texture texture) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.x3 = x3;
    this.y3 = y3;
    this.texture = texture;
  }
  
  /**
   * Creates a new colored and textured triangle shape.
   * The given color will be interpolated with the texture, which can be used to "colorize" as texture
   * (for example to darken the texture or to add some transparency to it)
   * For now, we use already normalized values for x and y as input.
   * TODO: As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public TriangleShape(float x1, float y1, float x2, float y2, float x3, float y3, Color color, Texture texture) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.x3 = x3;
    this.y3 = y3;
    this.color = color;
    this.texture = texture;
  }
  
  
  /**
   * Returns a list of all VBOs which represent this object.
   * If necessary, they will be created.
   */
  @Override
  public VertexBufferObject getVBO() {
    if (vbo != null) {
      return vbo;
    }
    
    // Prepare the color data
    final float red, green, blue, alpha;
    if (color == null) {
      // If no color is given, we use white, because white is "neutral", so it does not affect the texture through the interpolation
      red = green = blue = alpha = 1;
    } else {
      red = color.getRedNormalized();
      green = color.getGreenNormalized();
      blue = color.getBlueNormalized();
      alpha = color.getAlphaNormalized();
    }
    
    // Create the VBO
    vbo = new VertexBufferObject(
      false,
      texture,
      new float[] {
        x1, y1, red, green, blue, alpha, 0.5f, 1.0f,
        x2, y2, red, green, blue, alpha, 0.0f, 0.0f,
        x3, y3, red, green, blue, alpha, 1.0f, 0.0f
      }
    );
    
    return vbo;
  }
  
  
  /**
   * Returns the total count of vertices the actual renderable object uses, so that the renderer knows the total count in the render loop
   */
  @Override
  public int getVertexCount() {
    return vertexCount;
  }
  
  
  /**
   * Returns the OpenGL object type the renderable object uses, so the renderer knows to which VertexArray this shape must be attached
   */
  @Override
  public int getGL_TYPE() {
    return glRenderType;
  }
  
}
