package deadzone.graphics.worldobjects;

import deadzone.graphics.IIsoObject;
import deadzone.graphics.VertexBufferObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;


/**
 * An isometric tile.
 * It's basically a textured rectangle which represents the floor in the game.
 * It's a non-blocking world object which means other world objects can be placed on it.
 * Since Tiles are the world object with the lowest z index per type, they are always rendered before any other type of world objects.
 */
public class Tile implements IIsoObject {
  
  /** The mesh type so the renderer knows to which VAO the objects VBOs must be attached */
  private static final int glRenderType = GL_TRIANGLES;
  
  /** The sum of vertices of all VBOs of the object */
  private static final int vertexCount = 6;
  
  /** List of all VBOs which will form this object in the GPU */
  private ArrayList<VertexBufferObject> vboList;
  
  /** The x coordinate of the tile within the iso grid */
  private final int x;
  
  /** The y coordinate of the tile within the iso grid */
  private final int y;
  
  
  /**
   * Creates a new Tile
   */
  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  
  /**
   * Returns the x coordinate of the object in the orthogonal grid from bottom left
   *
   * @return Grid coordinate
   */
  @Override
  public int getXCoordinate() {
    return x;
  }
  
  /**
   * Returns the y coordinate of the object in the orthogonal grid from bottom left
   *
   * @return Grid coordinate
   */
  @Override
  public int getYCoordinate() {
    return y;
  }
  
  /**
   * Returns the OpenGL object type the renderable object uses, so the renderer knows to which VertexArray this shape must be attached
   */
  @Override
  public int getGL_TYPE() {
    return glRenderType;
  }
  
  /**
   * Returns the pointer to all VBOs which form this object together.
   * If there are no VBOs yet initialized, they will be initialized and bound on-the-fly
   */
  @Override
  public ArrayList<VertexBufferObject> getVBOs() {
    return vboList;
  }
  
  /**
   * Returns the total count of vertices the object have to form its mesh (some objects might have multiple mesh's
   * for example a button has two rectangle meshes, one for the shape and one for the font texture. Each mesh is a
   * rectangle formed by two GL_TRIANGLES).
   * The total count is used from the renderer to determine the amount of vertices to render to draw the object.
   */
  @Override
  public int getVertexCount() {
    return vertexCount;
  }
  
}
