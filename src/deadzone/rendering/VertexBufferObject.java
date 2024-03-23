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
    /* First step is to allocate GPU memory for our vertex data and put the vertex position and color data in it */
    
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
    
    /* Next step is to tell OpenGL how to use the data by specifying vertex attributes */
    
    // Get the location of the attributes
    int floatSize = 4;  // 4 byte per float value
    int shaderProgram = Deadzone.getApplication().getRenderer().getShaders().getProgramId();
    int posAttrib = glGetAttribLocation(shaderProgram, "position");  // Tells OpenGL that the first 3 floats describe the position attribute (x,y,z)
    
    // Enable the vertex attributes
    glEnableVertexAttribArray(posAttrib);
    
    // Point to the vertex attributes
    // - size is 3 because we have 3 floats per attribute (x,y,z / r,g,b)
    // - stride is 6 * floatSize because we have 2 attributes (position/color) with 3 floats values each
    // - pointer is the offset in bytes in the memory between an attribute
    //   The position attribute is the first one, so we have 0 offset. The offset to the color attribute is 3 float values
    //   so its 3 * floatSize = 12 bytes but this pointer is related to the position attribute, so we use 0.
    glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 6 * floatSize, 0);
    
    /* Last step for initialization is setting the uniform variables - we need it do transformation stuff for example if we want to rotate the camera view */
//    int uniModel = glGetUniformLocation(shaderProgram, "model");
//    Matrix4f model = new Matrix4f();
//    glUniformMatrix4fv(uniModel, false, model.getBuffer());
//
//    int uniView = glGetUniformLocation(shaderProgram, "view");
//    Matrix4f view = new Matrix4f();
//    glUniformMatrix4fv(uniView, false, view.getBuffer());
//
//    int uniProjection = glGetUniformLocation(shaderProgram, "projection");
//    float ratio = 640f / 480f;
//    Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
//    glUniformMatrix4fv(uniProjection, false, projection.getBuffer());
    
    
    /* Finally we can render the initialized triangle */
    glClear(GL_COLOR_BUFFER_BIT);  // clear the color buffer of the screen, otherwise we would get weird results
    glDrawArrays(GL_TRIANGLES, 0, 3); // we want to render all 3 vertices of the triangle, so we start at 0 and render 3 vertices
    
    
  }
  
  
  
}