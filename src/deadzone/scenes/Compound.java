package deadzone.scenes;

import deadzone.graphics.IsoGrid;
import deadzone.graphics.worldobjects.Tile;


/**
 * The compound is the central and also initial in-game scene.
 * It contains the player base, his characters and the central UI
 * from where other scenes (like Map for starting a mission) can be accessed
 */
public class Compound extends AbstractScene {
  
  protected static final Scene name = Scene.COMPOUND;
  
  /** Stores the compound tile grid */
  private final IsoGrid grid = new IsoGrid(100, 100, 10, 10);
  
  
  public Compound() {
    super(name);
  
  
    // Add some Tiles to test the MVP projection
    addObject(new Tile(grid, 0, 0));
    addObject(new Tile(grid, 2, 0));
    addObject(new Tile(grid, 4, 0));
    
    addObject(new Tile(grid, 1, 1));
    addObject(new Tile(grid, 3, 1));
    
    addObject(new Tile(grid, 0, 2));
    addObject(new Tile(grid, 2, 2));
    addObject(new Tile(grid, 4, 2));
  
//    addObject(new TriangleShape(
//      0.0f,  1.0f,
//      -0.5f,  0.0f,
//      0.5f,  0.0f,
//      new Color(255, 255, 255, 255)
//    ));
    
  }
  
  /**
   * Returns the compounds iso grid
   */
  public IsoGrid getGrid() {
    return grid;
  }
  
}