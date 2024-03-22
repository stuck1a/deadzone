package deadzone.rendering;

import deadzone.rendering.ShaderProgram;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaders;
  
  /** Stores all handles of objects which are currently stored in the GPU memory for rendering */
  int[] registeredObjects;
  
  
  public Renderer() {
  
  }
  
  public ShaderProgram getShaders() {
    return shaders;
  }
  
  public void init() {
    if (shaders == null) {
      try {
        shaders = new ShaderProgram();
        shaders.initializeBaseShaders();
        shaders.bind();
      } catch (Exception e) {
        System.err.println("Could not initialize shaders.\n" + e.getMessage());
        return;
      }
    }
  }
  
  public void renderRegisteredObjects() {
  
  }
  
  /**
   * Free up all resources from GPU memory
   */
  public void cleanup() {
  
  }
  
}