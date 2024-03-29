package deadzone.graphics.shapes;

import deadzone.graphics.Color;
import deadzone.graphics.IShape;
import deadzone.graphics.VertexBufferObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


public class TriangleShape implements IShape {
  
  /** The mesh type so the renderer knows to which VAO the objects VBOs must be attached */
  private static final int glRenderType = GL_TRIANGLES;
  
  /** The sum of vertices of all VBOs of the object */
  private static final int vertexCount = 3;
  
  /** List of all VBOs which will form this object in the GPU */
  private ArrayList<VertexBufferObject> vboList;
  
  private float x1;
  private float y1;
  
  private float x2;
  private float y2;
  
  private float x3;
  private float y3;
  
  private Color color;
  
  
  /**
   * Creates a new colored triangle shape.
   * For now, we use already normalized values for x and y as input.
   * As soon as the window width/height mapping is implemented, we can provide px or some other smart input instead.
   */
  public TriangleShape(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.x3 = x3;
    this.y3 = y3;
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
    
    // Create the VBO which represents the triangle
    final float red = color.getRedNormalized();
    final float green = color.getGreenNormalized();
    final float blue = color.getBlueNormalized();
    final float alpha = color.getAlphaNormalized();
    VertexBufferObject vbo1 = new VertexBufferObject(
      false,
      new float[] {
        x1, y1, red, green, blue, alpha, 0.5f, 1.0f,
        x2, y2, red, green, blue, alpha, 0.0f, 0.0f,
        x3, y3, red, green, blue, alpha, 1.0f, 1.0f
      }
    );
    vboList.add(vbo1);
    
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
