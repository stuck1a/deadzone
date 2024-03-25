package deadzone.graphics.shapes;

import deadzone.graphics.IShape;
import deadzone.scenes.AbstractScene;


public class TriangleShape implements IShape {
  
  
  public TriangleShape() {}
  
  
  /**
   * Returns the x position of the bottom left edge of the triangle.
   *
   * @return Pixel value
   */
  @Override
  public int getX() {
    return 0;
  }
  
  /**
   * Returns the y position of the bottom left edge of the triangle.
   *
   * @return Pixel value
   */
  @Override
  public int getY() {
    return 0;
  }
  
  /**
   * Returns the scene object in which this triangle will be rendered.
   *
   * @return scene object
   */
  @Override
  public AbstractScene getSceneName() {
    return null;
  }
  
}
