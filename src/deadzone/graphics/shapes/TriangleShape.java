package deadzone.graphics.shapes;

import deadzone.graphics.Color;
import deadzone.graphics.IShape;
import deadzone.graphics.VertexBufferObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


public class TriangleShape implements IShape {
  
  /** The OpenGL object type, so the renderer knows to which VertexArray this shape must be added */
  private static final int glRenderType = GL_TRIANGLES;
  
  /** The amount of vertices which are added through VBOs for each TriangleShape which is created */
  private static final int vertexCount = 3;
  
  
  public float x1;
  public float y1;
  
  public float x2;
  public float y2;
  
  public float x3;
  public float y3;
  
  public Color color;
  
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
   * Called from the renderer after binding the proper VAO.
   * One TriangleShape is reflected in OpenGL by one VBO of type GL_TRIANGLES.
   */
  public ArrayList<VertexBufferObject> prepareVBOs() {
    // DEV NOTE: If this would be the RectangleShape, we would set up two VBOs here since we need to GL_TRIANGLES to form a rectangle
  
    ArrayList<VertexBufferObject> vboList = new ArrayList<>();
    
    // Create the VBO which represents the triangle
    final float red = color.getRedNormalized();
    final float green = color.getGreenNormalized();
    final float blue = color.getBlueNormalized();
    final float alpha = color.getAlphaNormalized();
    VertexBufferObject vbo1 = new VertexBufferObject(new float[] {
      x1, y1, red, green, blue, alpha,
      x2, y2, red, green, blue, alpha,
      x3, y3, red, green, blue, alpha,
    });
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
