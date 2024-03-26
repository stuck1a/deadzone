package deadzone.scenes;

import deadzone.graphics.Color;
import deadzone.graphics.shapes.TriangleShape;


/**
 * This is the first rendered scene when the game is launched.
 * Here the user can log in/register his account, do some basic options
 * and start the initial in-game scene (Compound)
 */
public class Launcher extends AbstractScene {
  
  private static final Scene name = Scene.LAUNCHER;
  
  
  public Launcher() {
    super(name);
  
    // Add a green triangle for testing purposes
    TriangleShape greenTriangle = new TriangleShape(
      0.0f,  1.0f,
      -0.5f,  0.0f,
      0.5f,  0.0f,
      new Color(0, 255, 0, 255)
    );
    addObject(greenTriangle);
    
    // Add a red triangle for testing purposes
    TriangleShape redTriangle = new TriangleShape(
      0.0f,  0.5f,
      -0.5f, -0.5f,
      0.5f, -0.5f,
      new Color(255, 0, 0, 255)
    );
    addObject(redTriangle);
    

  }
  
}
