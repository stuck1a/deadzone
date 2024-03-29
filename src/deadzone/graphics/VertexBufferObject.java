package deadzone.graphics;

import deadzone.Deadzone;
import deadzone.Window;
import deadzone.math.Matrix4x4;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;


/**
 * This class represents a vertex buffer object and transforms java-sided objects into OpenGL-sided objects.
 * It also moves these objects to the GPU memory and stores the handles to access them.
 *
 * While the VBOs represents a single attribute list (like position attribute, color attribute, ...).
 * All VBOs which in total describe a single renderable object will be grouped together in a VAO.
 */
public class VertexBufferObject {
  
  /** The handler to access the native VBO variant OpenGL uses */
  private int vboId;
  
  /** The memory stack with support of garbage collector which automatically free up the memory when necessary */
  MemoryStack stack;
  
  /** Buffer which stores the vertices to handover it to the GPU */
  FloatBuffer vertices;
  
  /** Stores the raw data received from the constructor for later use when initialized */
  private float[] vertexData;
  
  /** If true, an isometric projection matrix will be used for this VBO */
  private boolean isIso;
  
  
  /**
   * Creates a new VBO by allocating the required amount of GPU memory and adding the vertex data in it
   * @param vertexData Each vertex consists of 6 float values: x, y, r, g, b, a
   */
  public VertexBufferObject(boolean isIso, float[] vertexData) {
    this.vertexData = vertexData;
    this.isIso = isIso;
  }
  
  
  /**
   * Set up the GPU object which is related to this java-sided object
   */
  public void initialize() {
    vboId = glGenBuffers();
    stack = MemoryStack.stackPush();
    vertices = stack.mallocFloat(vertexData.length);
    vertices.put(vertexData);
    vertices.flip();
    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    MemoryStack.stackPop();
    specifyVertexAttributes();
    specifyUniformData();
  }
  
  
  public void delete() {
    glDeleteBuffers(vboId);
  }
  
  
  public int getID() {
    return vboId;
  }
  
  
  
  /**
   * Default mappings for the input values of the VBO and the input values of the first shader (vertex shader).
   * This default setup consists of 6 values:
   * x position
   * y position
   * r color value
   * g color value
   * b color value
   * a color value
   */
  private void specifyVertexAttributes() {
    final int shaderProgram = Deadzone.getApplication().getRenderer().getShaderProgram().getProgramId();
    final int bytePerFloat = Float.BYTES;
    
    // Define the position data (float values 1+2)
    int positionAttribute = glGetAttribLocation(shaderProgram, "position");
    glEnableVertexAttribArray(positionAttribute);
    glVertexAttribPointer(positionAttribute, 2, GL_FLOAT, false, 6 * bytePerFloat, 0);
    
    // Define the color data (float values 3-6)
    int colorAttribute = glGetAttribLocation(shaderProgram, "color");
    glEnableVertexAttribArray(colorAttribute);
    glVertexAttribPointer(colorAttribute, 4, GL_FLOAT, false, 6 * bytePerFloat, 2 * bytePerFloat);
  }
  
  
  /**
   * Maps the MVP projection matrix to the uniform input of the vertex shader.
   * This matrix is mainly used for the isometric projection of any IIsoObject.
   */
  private void specifyUniformData() {
    final int shaderProgram = Deadzone.getApplication().getRenderer().getShaderProgram().getProgramId();
    Matrix4x4 mvp;
    
    // Apply an isometric projection through the mvp uniform variable
    if (isIso) {
      final Window window = Deadzone.getApplication().getWindow();
      final float ratio = (float) window.getPixelWidth() / (float) window.getPixelHeight();
      
      mvp = Matrix4x4.createOrthoProjectionMatrix(-ratio, ratio, -1f, 1f, -1f, 1f);
      //mvp = Matrix4x4.createOrthoProjectionMatrix(-1f, 1f, -1f, 1f, -1f, 1f);
    } else {
      // Otherwise use the "neutral" identity matrix for mvp (its like multiply with 1 - nothing will change)
      mvp = new Matrix4x4();
    }
    
    // Send mvp matrix to the vertex shader
    int mvpPos = glGetUniformLocation(shaderProgram, "MVP");
    
    // Create a buffer containing the mvp matrix
    MemoryStack stack = MemoryStack.stackPush();
    FloatBuffer mvpBuffer = stack.mallocFloat(16);
    mvp.toBuffer(mvpBuffer);
    MemoryStack.stackPop();
    
    // Attach the buffered matrix
    glUniformMatrix4fv(mvpPos, false, mvpBuffer);
  }
  
}
