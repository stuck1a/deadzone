package deadzone.graphics;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaderProgram;
  
  /** Stores all VAO with the related mesh type as key which are currently stored in the GPU memory for rendering */
  private final HashMap<Integer,VertexArrayObject> attachedVAOs = new HashMap<>();
  
  private ArrayList<IRenderable> registeredRenderableObjects = new ArrayList<>();
  
  
  public Renderer() {
    init();
  }
  
  public ShaderProgram getShaderProgram() {
    return shaderProgram;
  }
  
  public void init() {
    // Create and bind shader program
    if (shaderProgram == null) {
      try {
        shaderProgram = new ShaderProgram();
        shaderProgram.initializeBaseShaders();
        glBindFragDataLocation(shaderProgram.getProgramId(), 0, "outColor");
        shaderProgram.use();
      } catch (Exception e) {
        System.err.println("Could not initialize shaders.\n" + e.getMessage());
      }
    }
  
    // Create and bind the VAO for standard meshes
    VertexArrayObject vaoTriangles = new VertexArrayObject(GL_TRIANGLES);
    attachedVAOs.put(GL_TRIANGLES, vaoTriangles);
    vaoTriangles.bind();
  }
  
  /**
   * Called from the render loop.
   * Iterates through all VAOs, initializes and binds all related VBOs (related by mesh type) to them,
   * draws the corresponding meshes and finally unbind those VBOs again.
   * After that, all registered objects will be cleared to prepare for the upcoming game loop.
   */
  public void renderRegisteredObjects() {
    // TODO: As soon as another mesh types is implemented, test if the render order can mixed up or not.
    //  If so we don't need the inner if-statement here and just use a variable mode parameter for glDrawArrays instead,
    //  since this would speed up the execution.
    
    // TODO: Besides that, the current iteration logic would skip some VBOs if they are not perfectly ordered.
    //  But as long as we have no other mesh type and don't know the details how to handle that properly,
    //  that's okay. Rework will be done when with the introduction of mesh type GL_LINE.
    
    // Draw all meshes of type GL_TRIANGLES
    for ( IRenderable obj : registeredRenderableObjects ) {
      if (obj.getGL_TYPE() == GL_TRIANGLES) {
        ArrayList<VertexBufferObject> vboList = obj.getVBOs();
        
        for ( VertexBufferObject vbo : vboList ) {
          vbo.initialize();
          glDrawArrays(GL_TRIANGLES, 0, obj.getVertexCount());
          vbo.delete();
        }
      }
    }
    registeredRenderableObjects.clear();
  }
  
  
  public void registerObject(IRenderable obj) {
    registeredRenderableObjects.add(obj);
  }
  
  
  /**
   * Free up all resources from GPU memory
   */
  public void dispose() {
    // Delete VAOs
    attachedVAOs.forEach((type, vao) -> vao.delete());
    attachedVAOs.clear();
    
    // Delete shaders
    int vertexShader = shaderProgram.getVertexShaderId();
    int fragmentShader = shaderProgram.getFragmentShaderId();

    if (vertexShader != 0) {
      glDeleteShader(vertexShader);
    }
    if (fragmentShader != 0) {
      glDeleteShader(fragmentShader);
    }
    this.shaderProgram.cleanup();
  }
  
}