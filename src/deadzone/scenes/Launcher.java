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
    addObject(
      "triangle1",
      new TriangleShape(
      0.0f,  1.0f,
      -0.5f,  0.0f,
      0.5f,  0.0f,
      new Color(0, 255, 0)
    ));
    
    // Add a red triangle for testing purposes
    addObject(
      "triangle2",
      new TriangleShape(
      0.0f,  0.5f,
      -0.5f, -0.5f,
      0.5f, -0.5f,
      new Color(255, 0, 0, 100)
    ));
  
    // Add a blue rectangle for testing purposes
    addObject("rectangle1", new RectangleShape(0.0f, -0.1f, 0.3f, 0.2f, new Color(0, 0, 255, 100)));
    addObject("rectangle2", new RectangleShape(-0.48f, 0.32f, 0.4f, 0.4f, new Color(20, 125, 255, 200)));
  }
  
}
