package deadzone.scenes;

import deadzone.Deadzone;
import deadzone.assets.AssetManager;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IsoGrid;
import deadzone.graphics.Renderer;
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
    addObject("tile11",new Tile(grid, -2, -2, texture_1_marked));
    addObject("tile21",new Tile(grid, -1, -2, texture_1_marked));
    addObject("tile31",new Tile(grid, 0, -2, texture_1_marked));
    addObject("tile41",new Tile(grid, 1, -2, texture_1_marked));
    addObject("tile51",new Tile(grid, 2, -2, texture_1_marked));
    
    addObject("tile12",new Tile(grid, -2, -1, texture_1_marked));
    addObject("tile22",new Tile(grid, -1, -1, texture_1_marked));
    addObject("tile32",new Tile(grid, 0, -1, texture_1_marked));
    addObject("tile42",new Tile(grid, 1, -1, texture_1_marked));
    addObject("tile52",new Tile(grid, 2, -1, texture_1_marked));
    
    addObject("tile13",new Tile(grid, -2, 0, texture_1_marked));
    addObject("tile23",new Tile(grid, -1, 0, texture_1_marked));
    addObject("tile33",new Tile(grid, 0, 0, texture_1_marked));
    addObject("tile43",new Tile(grid, 1, 0, texture_1_marked));
    addObject("tile54",new Tile(grid, 2, 0, texture_1_marked));
    
    addObject("tile15",new Tile(grid, -2, 1, texture_1_marked));
    addObject("tile25",new Tile(grid, -1, 1, texture_1_marked));
    addObject("tile35",new Tile(grid, 0, 1, texture_1_marked));
    addObject("tile45",new Tile(grid, 1, 1, texture_1_marked));
    addObject("tile55",new Tile(grid, 2, 1, texture_1_marked));
    
    addObject("tile16",new Tile(grid, -2, 2, texture_1_marked));
    addObject("tile26",new Tile(grid, -1, 2, texture_1_marked));
    addObject("tile36",new Tile(grid, 0, 2, texture_1_marked));
    addObject("tile46",new Tile(grid, 1, 2, texture_1_marked));
    addObject("tile56",new Tile(grid, 2, 2, texture_1_marked));

    // Render a triangle to test rendering of no-iso objects like UI Elements
    addObject(
      "lilaTriangleTest",
      new deadzone.graphics.shapes.TriangleShape(
      0.0f,  1.0f,
      -0.5f,  0.0f,
      0.5f,  0.0f,
      new deadzone.graphics.Color(60, 20, 210, 255)
    ));
    
    
    // Render some text (use Arial, size 10, bold)
    final Font font = assets.getFont("Arial");
    font.setColor(new Color(128, 64, 64));
    final int currentSecond = Deadzone.getApplication().getTimer().getCurrentSecond();
    Text testText = new Text(-1.0f, -1.0f, font, "FPS: " + currentSecond, 3.5f);
    addObject("fpsDisplay", testText);
    System.out.println("Initialized testText:\nTotal width: " + testText.getTotalPixelWidth() + "px\nTotal Height: " + testText.getTotalPixelHeight() + "px");
    
  }
  
  
  @Override
  public void updateRegisteredObjects() {
    // Remove the old timer text
    removeObject("fpsDisplay");
    
    // Update FPS display
    final AssetManager assets = Deadzone.getApplication().getAssetManager();
    final Font font = assets.getFont("Arial");
    font.setColor(new Color(128, 64, 64));
    final double currentTimestamp = Deadzone.getApplication().getTimer().getCurrentTimestamp();
    Text testText = new Text(-1.0f, -1.0f, font, "Time: " + currentTimestamp, 3.5f);
    addObject("fpsDisplay", testText);
    System.out.println("Initialized testText:\nTotal width: " + testText.getTotalPixelWidth() + "px\nTotal Height: " + testText.getTotalPixelHeight() + "px");
    
    
    
    // Now after all objects are adjusted accordingly, they are sent to the renderer with all their updated data for the next render loop
    Renderer renderer = Deadzone.getApplication().getRenderer();
    renderObjects.forEach((id, obj) -> {
      renderer.registerObject(obj);
    });
  }
  
  
  
  
  /**
   * Returns the compounds iso grid
   */
  public IsoGrid getGrid() {
    return grid;
  }
  
}
