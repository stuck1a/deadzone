package deadzone.scenes;

import deadzone.Deadzone;
import deadzone.assets.AssetManager;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IsoGrid;
import deadzone.graphics.fonts.Font;
import deadzone.graphics.ui.Text;
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
    
    final AssetManager assets = Deadzone.getApplication().getAssetManager();
    final Texture texture_1_marked = assets.getTexture("1_marked");
    
    // Add some Tiles to test the MVP projection
    addObject(new Tile(grid, -2, -2, texture_1_marked));
    addObject(new Tile(grid, -1, -2, texture_1_marked));
    addObject(new Tile(grid, 0, -2, texture_1_marked));
    addObject(new Tile(grid, 1, -2, texture_1_marked));
    addObject(new Tile(grid, 2, -2, texture_1_marked));
    
    addObject(new Tile(grid, -2, -1, texture_1_marked));
    addObject(new Tile(grid, -1, -1, texture_1_marked));
    addObject(new Tile(grid, 0, -1, texture_1_marked));
    addObject(new Tile(grid, 1, -1, texture_1_marked));
    addObject(new Tile(grid, 2, -1, texture_1_marked));
    
    addObject(new Tile(grid, -2, 0, texture_1_marked));
    addObject(new Tile(grid, -1, 0, texture_1_marked));
    addObject(new Tile(grid, 0, 0, texture_1_marked));
    addObject(new Tile(grid, 1, 0, texture_1_marked));
    addObject(new Tile(grid, 2, 0, texture_1_marked));
    
    addObject(new Tile(grid, -2, 1, texture_1_marked));
    addObject(new Tile(grid, -1, 1, texture_1_marked));
    addObject(new Tile(grid, 0, 1, texture_1_marked));
    addObject(new Tile(grid, 1, 1, texture_1_marked));
    addObject(new Tile(grid, 2, 1, texture_1_marked));
    
    addObject(new Tile(grid, -2, 2, texture_1_marked));
    addObject(new Tile(grid, -1, 2, texture_1_marked));
    addObject(new Tile(grid, 0, 2, texture_1_marked));
    addObject(new Tile(grid, 1, 2, texture_1_marked));
    addObject(new Tile(grid, 2, 2, texture_1_marked));

    // Render a triangle to test rendering of no-iso objects like UI Elements
    addObject(new deadzone.graphics.shapes.TriangleShape(
      0.0f,  1.0f,
      -0.5f,  0.0f,
      0.5f,  0.0f,
      new deadzone.graphics.Color(60, 20, 210, 255)
    ));
   
    
    // Render some text (use Arial, size 10, bold)
    final Font font = assets.getFont("Arial");
    font.setColor(new Color(128, 64, 64));
    Text testText = new Text(0.0f, 0.0f, font, "H+Hallo Welt", 3);
    addObject(testText);
    System.out.println("Initialized testText:\nTotal width: " + testText.getTotalPixelWidth() + "px\nTotal Height: " + testText.getTotalPixelHeight() + "px");
    
  }
  
  /**
   * Returns the compounds iso grid
   */
  public IsoGrid getGrid() {
    return grid;
  }
  
}
