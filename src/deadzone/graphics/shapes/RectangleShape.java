package deadzone.graphics.shapes;

import deadzone.graphics.Color;
import deadzone.graphics.IShape;
import deadzone.graphics.VertexBufferObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


public class RectangleShape implements IShape {
  
  /** The mesh type so the renderer knows to which VAO the objects VBOs must be attached */
  private static final int glRenderType = GL_TRIANGLES;
  
  /** The sum of vertices of all VBOs of the object */
  private static final int vertexCount = 6;    // TODO: Set to 4 after implementing vertex index buffer logic
  
  /** List of all VBOs which will form this object in the GPU */
  private ArrayList<VertexBufferObject> vboList;
  
  private float x;
  private float y;
  private float width;
  private float height;
  
  public Color color;
  
  
  /**
   * Creates a new colored rectangle shape.
   * For now, we use already normalized values for x and y as input.
   * As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public RectangleShape(float x, float y, float width, float height, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
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
    
    vboList = new ArrayList<>();
    
    // Create the two VBOs which represents the given rectangle
    final float red = color.getRedNormalized();
    final float green = color.getGreenNormalized();
    final float blue = color.getBlueNormalized();
    final float alpha = color.getAlphaNormalized();
    
    VertexBufferObject vbo1 = new VertexBufferObject(
      false,
      new float[] {
        x, y + height, red, green, blue, alpha, 0.5f, 1.0f,
        x, y, red, green, blue, alpha, 0.0f, 0.0f,
        x + width, y + height, red, green, blue, alpha, 1.0f, 1.0f
      }
    );
    vboList.add(vbo1);
  
    VertexBufferObject vbo2 = new VertexBufferObject(
      false,
      new float[] {
        x + width, y, red, green, blue, alpha, 0.5f, 1.0f,
        x,  y, red, green, blue, alpha, 0.0f, 0.0f,
        x + width, y + height, red, green, blue, alpha, 1.0f, 1.0f
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
