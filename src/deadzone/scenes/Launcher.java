package deadzone.scenes;

import deadzone.graphics.Color;
import deadzone.graphics.shapes.RectangleShape;
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
    addObject(new TriangleShape(
      0.0f,  1.0f,
      -0.5f,  0.0f,
      0.5f,  0.0f,
      new Color(0, 255, 0, 100)
    ));
  
    // Add a blue rectangle for testing purposes (currently x/y/width/height are ignored and constant values are used instead for testing)
    addObject(new RectangleShape(0.0f, 0.0f, 0.0f, 0.0f, new Color(0, 0, 255, 100)));
    
    // Add a red triangle for testing purposes
    addObject(new TriangleShape(
      0.0f,  0.5f,
      -0.5f, -0.5f,
      0.5f, -0.5f,
      new Color(255, 0, 0, 100)
    ));
  }
  
}
