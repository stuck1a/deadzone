package deadzone.graphics;

/**
 * Every world object which uses this interface will be rendered in orthogonal view and must therefore
 * provide methods to get its coordinates in the orthogonal grid
 */
public interface IIsoObject extends IRenderable {
  
  /**
   * Returns the x coordinate of the object in the orthogonal grid from bottom left
   * @return Grid coordinate
   */
  int getXCoordinate();
  
  /**
   * Returns the y coordinate of the object in the orthogonal grid from bottom left
   * @return Grid coordinate
   */
  int getYCoordinate();
  
}