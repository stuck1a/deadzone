package deadzone.rendering;

import deadzone.Deadzone;
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
  
  /** Holds the raw input data for vertices and color attributes */
  float[] data;
  
  /** Buffer which stores the vertices to handover it to the GPU */
  FloatBuffer vertices;
  
  
  /**
   * Creates a new VBO by allocating the required amount of GPU memory
   * @param vertexData Base on our shader programs, each vertex consists of 6 float values: x, y, z, r, g, b
   *                   By convention, it's recommend to provide the points counter-clockwise.
   *                   For example a triangle with 3 points in 3 different colors:
   *                   -0.6f, -0.4f, 0f, 1f, 0f, 0f,
   *                    0.6f, -0.4f, 0f, 0f, 1f, 0f,
   *                    0f,    0.6f, 0f, 0f, 0f, 1f
   */
  public VertexBufferObject(float[] vertexData) {
    // Create the buffer
    stack = MemoryStack.stackPush();
    vertices = stack.mallocFloat(vertexData.length);
    
    // Add the position and color data to the buffer
    for (float val : vertexData) {
      vertices.put(val);
    }
    vertices.flip();
  }
  
  /**
   * Set up the GPU object which is related to this java-sided object
   */
  public void initialize() {
    // Generate GPU buffer and bind it to the correct buffer type
    vboId = glGenBuffers();
    bind(GL_ARRAY_BUFFER);
    
    // Upload vertices to the GPU
    uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    MemoryStack.stackPop();
  
    // Specify how to use the received data
    specifyVertexAttributes();

    // Set up uniform variables to allow transformations for camera rotation and such
    //specifyUniformData();
  }
  
  
  /**
   * Draws the fully initialized object
   */
  public void render() {
    // Clear the color buffer of the screen, otherwise we would get weird results
    glClear(GL_COLOR_BUFFER_BIT);
    // Render all 3 vertices of the triangle, so we start at 0 and render 3 vertices
    glDrawArrays(GL_TRIANGLES, 0, 3);
  }
  
  
  public void delete() {
    glDeleteBuffers(vboId);
  }
  
  
  public int getID() {
    return vboId;
  }
  
  
  /**
   * Binds the buffer to the GPU
   */
  private void bind(int target) {
    glBindBuffer(target, vboId);
  }
  
  
  /**
   * Adds data to the buffer
   */
  private void uploadData(int target, FloatBuffer data, int usage) {
    glBufferData(target, data, usage);
  }
  
  
  /**
   * Defines exactly what value of the input shall be used for and where it is located in the buffer.
   * OpenGL will automatically link the attribute information to the currently bound VAO.
   */
  private void specifyVertexAttributes() {
    int shaderProgram = Deadzone.getApplication().getRenderer().getShaderProgram().getProgramId();
    
    // Define the position data (float values 1+2)
    int positionAttribute = glGetAttribLocation(shaderProgram, "position");
    glEnableVertexAttribArray(positionAttribute);
    glVertexAttribPointer(positionAttribute, 2, GL_FLOAT, false, 0, 0);
    
    // Define the color data (float values 3-5)
    int colorAttribute = glGetAttribLocation(shaderProgram, "color");
    glEnableVertexAttribArray(colorAttribute);
    glVertexAttribPointer(colorAttribute, 3, GL_FLOAT, false, 0, 2 * Float.BYTES);
  }
  
}