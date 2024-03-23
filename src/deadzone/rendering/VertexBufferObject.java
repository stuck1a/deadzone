package deadzone.rendering;

import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;


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
  
  
  /**
   * Creates a new VBO
   * @param vertexData Base on our shader programs, each vertex consists of 6 float values: x, y, z, r, g, b
   *                   Note that per convetion the vertices are ordered counter-clockwise.
   *                   For example a triangle with 3 points in 3 different colors:
   *                   -0.6f, -0.4f, 0f, 1f, 0f, 0f,
   *                    0.6f, -0.4f, 0f, 0f, 1f, 0f,
   *                    0f,    0.6f, 0f, 0f, 0f, 1f
   */
  public VertexBufferObject(float[] vertexData) {
    // Create the buffer
    stack = MemoryStack.stackPush();
    FloatBuffer vertices = stack.mallocFloat(vertexData.length);
    for (float val : vertexData) {
      vertices.put(val);
    }
    vertices.flip();
    
    // Bind the buffer and upload it to the GPU memory
    vboId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    MemoryStack.stackPop();
  }
  
  
  
}