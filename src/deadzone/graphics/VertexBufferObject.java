package deadzone.graphics;

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
  
}
