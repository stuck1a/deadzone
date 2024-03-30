package deadzone.graphics.shapes;

import deadzone.Deadzone;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IShape;
import deadzone.graphics.VertexBufferObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


public class RectangleShape implements IShape {
  
  /** The mesh type so the renderer knows to which VAO the objects VBOs must be attached */
  private static final int glRenderType = GL_TRIANGLES;
  
  /** The sum of vertices of all VBOs of the object */
  private static final int vertexCount = 6;    // TODO: Set to 4 after implementing vertex index buffer (EBO)
  
  /** List of all VBOs which will form this object in the GPU */
  private ArrayList<VertexBufferObject> vboList;
  
  private float x;
  private float y;
  private float width;
  private float height;
  private Texture texture;
  public Color color;
  
  
  
  /**
   * Creates a new colored rectangle shape.
   * For now, we use already normalized values for x and y as input.
   * TODO: As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public RectangleShape(float x, float y, float width, float height, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
    this.texture = Deadzone.getApplication().getAssetManager().getTexture("blank");
  }
  
  /**
   * Creates a new textured rectangle shape.
   * For now, we use already normalized values for x and y as input.
   * TODO: As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public RectangleShape(float x, float y, float width, float height, Texture texture) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture = Deadzone.getApplication().getAssetManager().getTexture("blank");
  }
  
  
  /**
   * Creates a new colored and textured rectangle shape.
   * For now, we use already normalized values for x and y as input.
   * TODO: As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public RectangleShape(float x, float y, float width, float height, Color color, Texture texture) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
    this.texture = texture;
  }
  
  
  /**
   * Returns a list of all VBOs which represent this object.
   * If necessary, they will be created.
   */
  @Override
  public ArrayList<VertexBufferObject> getVBOs() {
    if (vboList != null) {
      return vboList;
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
    
    // Create the two VBOs which represents the given rectangle
    vboList = new ArrayList<>();
    VertexBufferObject vbo1 = new VertexBufferObject(
      false,
      texture,
      new float[] {
        x, y + height, red, green, blue, alpha, 0, 1,
        x, y, red, green, blue, alpha, 0, 0,
        x + width, y + height, red, green, blue, alpha, 1, 1
      }
    );
    vboList.add(vbo1);
  
    VertexBufferObject vbo2 = new VertexBufferObject(
      false,
      texture,
      new float[] {
        x + width, y, red, green, blue, alpha, 0, 1,
        x,  y, red, green, blue, alpha, 0, 0,
        x + width, y + height, red, green, blue, alpha, 1, 1
      }
    );
    vboList.add(vbo2);
    
    return vboList;
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
