package deadzone.graphics;

/**
 * This class describes the isometric grid and provides functions to convert pixel positions to coordinates and vice versa
 */
public class IsoGrid {
  /** Number of horizontal pixels for a tile in this grid */
  private final int tileWidth;
  
  /** Number of vertical pixels for a tile in this grid */
  private final int tileHeight;
  
  /** Total count of horizontal tiles in this grid */
  private final int tileCountX;
  
  /** Total count of vertical tiles in this grid */
  private final int tileCountY;
  
  
  /**
   * Creates a new isometric grid.
   *
   * @param tileWidth Width in pixels of each tile
   * @param tileHeight Height in pixels of each tile
   * @param tileCountX Total number of tiles in horizontal direction
   * @param tileCountY Total number of tiles in vertical direction
   */
  public IsoGrid(int tileWidth, int tileHeight, int tileCountX, int tileCountY) {
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.tileCountX = tileCountX;
    this.tileCountY = tileCountY;
  }
  
  
  /**
   * @return Width in pixels of a single tile
   */
  public int getTileWidth() {
    return tileWidth;
  }
  
  /**
   * @return Height in pixels of a single tile
   */
  public int getTileHeight() {
    return tileHeight;
  }
  
  /**
   * @return Total number of tiles in horizontal direction in the grid
   */
  public int getTileCountX() {
    return tileCountX;
  }
  
  /**
   * @return Total number of tiles in vertical direction in the grid
   */
  public int getTileCountY() {
    return tileCountY;
  }
  
}