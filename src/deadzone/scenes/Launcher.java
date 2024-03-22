package deadzone.scenes;


import deadzone.rendering.ShaderProgram;

/**
 * This is the first rendered scene when the game is launched.
 * Here the user can logn/register his account, do some basic options
 * and start the initial in-game scene (Compound)
 */
public class Launcher extends AbstractScene {
  
  private static final Scene name = Scene.LAUNCHER;
  
  protected ShaderProgram shaders;
  
  
  public Launcher() {
    super(name);
  }
  

  
  public void renderScene() {
    
    // Make sure the shaders are upset
    if (shaders == null) {
      try {
        shaders = new ShaderProgram();
        shaders.initializeBaseShaders();
        shaders.initializeBaseShaders();
      } catch (Exception e) {
        System.err.println("Could not initialize shaders.\n" + e.getMessage());
        return;
      }
    }
  
    System.out.println("###   Do some rendering here...   ###");
    
  }
  
}