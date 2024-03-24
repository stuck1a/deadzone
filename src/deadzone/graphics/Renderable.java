package deadzone.graphics;

import deadzone.scenes.Scene;

/**
 * Every object that shall be displayed need to implement this interface.
 * This can be world objects, items, characters and so on, but also menu items, images, forms or whatever.
 */
public interface Renderable {
  
  // Rendering position in pixel
  int posX = 1;
  int posY = 1;
  
  // Rendering location in pixel
  int height = 1;
  int width = 1;
  
  // The scene this object belongs to (NONE is placeholder to prevent issues)
  Scene scene = Scene.NONE;
  
}