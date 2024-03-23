package deadzone.rendering;

import static org.lwjgl.opengl.GL30.*;

/**
 * This class represents a single renderable object in the GPU memory with all its data in an OpenGL compatible format.
 * It consists of multiple VBOs and groups them all together.
 * It also provides a handle which allows to access those objects in the GPU memory.
 */
public class VertexArrayObject {

  /** The handler to access the native VAO variant OpenGL uses */
  private final int vaoId;
  
  
  public VertexArrayObject() {
    vaoId = glGenVertexArrays();
  }
  
  public int getID() {
    return vaoId;
  }
  
  public void bind() {
    glBindVertexArray(vaoId);
  }
  
  public void delete() {
    glDeleteVertexArrays(vaoId);
  }
  

  
  
}