package deadzone.graphics;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaderProgram;
  
  /** Stores all handles of VAO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexArrayObject> attachedVaoObjects = new ArrayList<>();    // WIRD EVENTUELL NOCH BENÖTIGT, WENN ANDERE MESH TYPEN DAZUKOMMEN (Z.B. GL_LINE)
  
  ArrayList<IRenderable> registeredRenderableObjects = new ArrayList<>();
  
  
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

  }
  
  /**
   * The main render loop.
   * At this point all objects VAOs are ready and have their VBOs attached and all vertex attribute pointer are defined.
   * So we only render each VAO with the proper render type and the remove all registered objects as preparation for the
   * next render loop.
   */
  public void renderRegisteredObjects() {
    // Create and bind the default VAO (for meshes of type GL_TRIANGLES)
    VertexArrayObject vaoTriangleMeshes = new VertexArrayObject(GL_TRIANGLES);
    vaoTriangleMeshes.bind();
    
    // Iterate through all registered objects
    for ( IRenderable obj : registeredRenderableObjects ) {
      // Make sure, they use triangle meshes
      if (obj.getGL_TYPE() == GL_TRIANGLES) {
        
        // Iterate though all VBOs of the object
        ArrayList<VertexBufferObject> vboList = obj.getVBOs();
        final int vertexCount = obj.getVertexCount();
        for ( VertexBufferObject vbo : vboList ) {
          // Load the VBO to the GPU and attach it to the VAO
          vbo.initialize();
          // Render it
          glDrawArrays(GL_TRIANGLES, 0, 3);
          // Unbind it so can render the next one
          vbo.delete();
        }
      }
    }
    // Remove all registered object to prepare the next loop
    registeredRenderableObjects.clear();
    // Unbind the VAO, because currently we also recreate this one for each loop
    vaoTriangleMeshes.delete();
  }
  
  
  public void registerObject(IRenderable obj) {
    registeredRenderableObjects.add(obj);
  }
  
  
  /**
   * Free up all resources from GPU memory
   */
  public void dispose() {
    // Remove the shaders from the GPU memory
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