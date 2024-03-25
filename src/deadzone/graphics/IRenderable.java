package deadzone.graphics;

import deadzone.scenes.AbstractScene;

/**
 * Every object that shall be displayed need to implement this interface.
 * This can be world objects, items, characters and so on, but also menu items, images, forms or whatever.
 */
public interface IRenderable {
  
  /**
   * Returns the x position of the object from bottom left
   * @return Pixel value
   */
  int getX();
  
  /**
   * Returns the y position of the object from bottom left
   * @return Pixel value
   */
  int getY();
  
  /**
   * Returns the scene object in which this object will be rendered
   * @return scene object
   */
  AbstractScene getSceneName();
  
}