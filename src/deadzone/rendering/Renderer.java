package deadzone.rendering;

import deadzone.rendering.ShaderProgram;


public class Renderer {
  protected ShaderProgram shaders;
  
  
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

}