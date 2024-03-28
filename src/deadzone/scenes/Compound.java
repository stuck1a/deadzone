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
  private final IsoGrid grid = new IsoGrid(50, 50, 10, 10);
  
  
  public Compound() {
    super(name);
  
  
    // Add a Tile for testing
    Tile testTile = new Tile(grid, 1, 1);
    addObject(testTile);
    
  }
  
  /**
   * Returns the compounds iso grid
   */
  public IsoGrid getGrid() {
    return grid;
  }
  
}