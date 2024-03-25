package deadzone.graphics;

import java.util.ArrayList;


/**
 * Every object that shall be displayed need to implement this interface.
 * This can be world objects, items, characters and so on, but also menu items, images, forms or whatever.
 */
public interface IRenderable {
  
  
  /** Returns the OpenGL object type the renderable object uses, so the renderer knows to which VertexArray this shape must be attached */
  public int getGL_TYPE();
  
  /** Set up all vertex attributes for all VBOs the actual renderable object requires */
  public ArrayList<VertexBufferObject> prepareVBOs();
  
  /** Returns the total count of vertices the actual renderable object uses, so that the renderer knows the total count in the render loop */
  public int getVertexCount();
  
}