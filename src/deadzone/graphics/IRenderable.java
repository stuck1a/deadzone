package deadzone.graphics;

/**
 * Every object that shall be displayed need to implement this interface.
 * This can be world objects, items, characters and so on, but also menu items, images, forms or whatever.
 *
 * Every renderable object is reflected by one or more VBOs in the GPU.
 */
public interface IRenderable {
  
  /** Returns the OpenGL object type the renderable object uses, so the renderer knows to which VertexArray this shape must be attached */
  public int getGL_TYPE();
  
  
  public VertexBufferObject getVBO();
  
  /** Returns the total count of vertices the object have to form its mesh (some objects might have multiple mesh's
   *  for example a button has two rectangle meshes, one for the shape and one for the font texture. Each mesh is a
   *  rectangle formed by two GL_TRIANGLES).
   *  The total count is used from the renderer to determine the amount of vertices to render to draw the object. */
  public int getVertexCount();
}