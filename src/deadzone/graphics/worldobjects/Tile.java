package deadzone.graphics.worldobjects;

import deadzone.graphics.IIsoObject;
import deadzone.graphics.IsoGrid;
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
  
  /** The y coordinate of the Tile within the iso grid */
  private final int y;
  
  /** The Grid to which this Tile belongs to  */
  private final IsoGrid grid;
  
  
  /**
   * Creates a new Tile
   */
  public Tile(IsoGrid grid, int x, int y) {
    this.grid = grid;
    this.x = x;
    this.y = y;
  }
  
  
  /**
   * Returns the iso grid to which this Tile belongs to
   */
  public IsoGrid getGrid() {
    return this.grid;
  }
  
  /**
   * Returns the x coordinate of the Tile within the iso grid
   */
  @Override
  public int getXCoordinate() {
    return x;
  }
  
  /**
   * Returns the y coordinate of the Tile within the iso grid
   */
  @Override
  public int getYCoordinate() {
    return y;
  }
  
  @Override
  public int getGL_TYPE() {
    return glRenderType;
  }
  
  @Override
  public ArrayList<VertexBufferObject> getVBOs() {
    return vboList;
  }
  
  @Override
  public int getVertexCount() {
    return vertexCount;
  }
  
}
