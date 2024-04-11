package deadzone.scenes;

import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.graphics.IsoGrid;
import deadzone.graphics.fonts.Pen;
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
  
  
    if (game.isDebugMode()) {
      initDebugInfos();
    }
  }
  
  
  @Override
  public void updateScene() {
    
    if (game.isDebugMode()) {
      updateDebugInfos();
    }
    
    // Register objects for rendering  // TODO: Maybe omit this here and let the renderer grab all registered objects itself? Check Pro/Con!
    sendToRenderer();
  }
  
  
  
  
  /**
   * Returns the compounds iso grid
   */
  public IsoGrid getGrid() {
    return grid;
  }
  
  
  
  /**
   * Adds the static part of the debug data which is displayed at the top left edge
   */
  private void initDebugInfos() {
    // NEUE LOGIK
    final Pen pen = new Pen(
      assets.getFont("Arial"),
      50,
      -1, 1,
      new Color(128, 128, 128)
    );

    final Text test = pen.writeText("TestTest");
    final Text test2 = pen.writeText("FooBar");
    
    
    addObject("testText", test);
    addObject("testText2", test2);
    
    
    // ALTE LOGIK
    
//    final float textScaling = -.5f;
//    final Font font = assets.getFont("Arial");
//    font.setColor(new Color(255, 255, 255));
//
//    final Text timeLbl = new Text(-1, 1, font, "Time:", textScaling);
//    final Text fpsLbl = new Text(-1, timeLbl.getY() - timeLbl.getTotalHeight(), font, "FPS:", textScaling);
//    final Text coordLbl = new Text(-1, fpsLbl.getY() - fpsLbl.getTotalHeight(), font, "Mouse Pos:", textScaling);
//
//    addObject("timeLbl", timeLbl);
//    addObject("fpsLbl", fpsLbl);
//    addObject("coordLbl", coordLbl);
  }
  
  
  /**
   * Updates the dynamic part of the debug data which is displayed at the top left edge
   */
  private void updateDebugInfos() {
    // NEUE LOGIK
    
    
    
    // ALTE LOGIK
    
//    // Remove the old timer text
//    removeObject("time");
//
//    final Font font = assets.getFont("Arial");
//    final float textScaling = -.5f;
//    font.setColor(new Color(128, 64, 64));
//
//    // Update rendered time value   // TODO: Skalierung wird hier nicht angewandt! Findet diese zu früh statt?
//    final Text TimeLbl = ((Text) renderObjects.get("timeLbl"));
//    addObject("time", new Text(TimeLbl.getX() + TimeLbl.getTotalWidth() + .05f, TimeLbl.getY(), font, "" + timer.getCurrentTimestamp(), textScaling));
//
//    // Update rendered fps value
//    final Text fpsLbl = ((Text) renderObjects.get("fpsLbl"));
//    addObject("fps", new Text(fpsLbl.getX() + fpsLbl.getTotalWidth() + .05f, fpsLbl.getY(), font, "" + timer.getFps(), textScaling));
//
//    // Update current mouse coordinates
//    final Text coordLbl = ((Text) renderObjects.get("coordLbl"));
//    // TODO: Get and render current mouse position in display coordinates
    
  }
  
}
