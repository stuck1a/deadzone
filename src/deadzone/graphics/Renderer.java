package deadzone.graphics;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaderProgram;
  
  /** Stores all VAO with the related mesh type as key which are currently stored in the GPU memory for rendering */
  final private HashMap<Integer,VertexArrayObject> attachedVAOs = new HashMap<>();
  
  final private ArrayList<IRenderable> registeredRenderableObjects = new ArrayList<>();
  
  /** With this info, we know if we need to switch the VAO while rendering */
  protected int activeDrawType;
  
  
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
  
    // Prepare the VAO for triangle-based meshes
    attachedVAOs.put(GL_TRIANGLES, new VertexArrayObject(GL_TRIANGLES));
    attachedVAOs.put(GL_LINES, new VertexArrayObject(GL_LINES));  // TODO: not used yet, implement class LineShape
  }
  
  /**
   * Called from the render loop.
   * Iterates through all VAOs, initializes and binds all related VBOs (related by mesh type) to them,
   * draws the corresponding meshes and finally unbind those VBOs again.
   * After that, all registered objects will be cleared to prepare for the upcoming game loop.
   */
  public void renderRegisteredObjects() {
    
    // Draw all registered vertices
    for ( IRenderable obj : registeredRenderableObjects ) {
      // Do we need to bind another VAO for the current obj/vbo?
      final int currentDrawType = obj.getGL_TYPE();
      if (currentDrawType != activeDrawType) {
        attachedVAOs.get(currentDrawType).bind();
      }
      
      // Draw all vertices of the current VBO, then pop it from stack
      final VertexBufferObject vbo = obj.getVBO();
      vbo.initialize();
      glDrawArrays(GL_TRIANGLES, 0, obj.getVertexCount());
      vbo.delete();
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