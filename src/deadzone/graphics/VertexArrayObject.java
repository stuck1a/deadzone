package deadzone.graphics;

import static org.lwjgl.opengl.GL30.*;

/**
 * This class represents a single renderable object in the GPU memory with all its data in an OpenGL compatible format.
 * It consists of multiple VBOs and groups them all together.
 * It also provides a handle which allows to access those objects in the GPU memory.
 */
public class VertexArrayObject {

  /** The handler to access the native VAO object in the GPU */
  private final int vaoId;
  
  /** Because we use one VAO per OpenGL render type (like GL_TRIANGLES, ...) we must specify for which one this VAO is made for */
  public final int GL_RENDER_TYPE;
  
  
  public VertexArrayObject(int GL_RENDER_TYPE) {
    this.GL_RENDER_TYPE = GL_RENDER_TYPE;
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