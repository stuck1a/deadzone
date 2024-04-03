package deadzone.graphics.fonts;

import deadzone.Util;
import deadzone.assets.IAsset;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.math.Vector2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;


/**
 * This class represents a single font with all its glyphs.
 *
 * TODO: Add pre-created textures for to each glyph
 *
 */
public class Font implements IAsset {
  
  /** Stored the generated atlas texture on which als character are printed on initialization */
  private Texture atlasTexture;
  private HashMap<Character, Glyph> glyphs = new HashMap<>();
  /** Path to the glyph definition for this Font */
  private final String jsonFilePath;
  private String name;
  private boolean isItalic;
  private boolean isBold;
  private int size;
  private Color color;
  
  
  /**
   * Prepares a new Font for use.
   */
  public Font(String fontDefJsonPath) {
    this.jsonFilePath = assetsDir + "fonts" + fileSeparator +  fontDefJsonPath;
  }
  
  
  /**
   * Prepares a new Font for use.
   * Presets the given color
   */
  public Font(String fontDefJsonPath, Color color) {
    this.jsonFilePath = assetsDir + "fonts" + fileSeparator +  fontDefJsonPath;
    this.color = color;
  }
  
  
  /**
   * Generates glyph definitions and loads the atlas image
   */
  @Override
  public void load() {
    JSONObject json = Util.parseJSON(jsonFilePath);
    assert json != null;
    this.name = (String) json.get("name");
    
    final JSONObject styleData = (JSONObject) json.get("style");
    final JSONObject cellData = (JSONObject) json.get("cells");
    final JSONArray glyphArray = (JSONArray) json.get("glyphs");
    
    this.isItalic = (boolean) styleData.get("italic");
    this.isBold = (boolean) styleData.get("bold");
    this.size = Integer.parseInt((String)styleData.get("height"));
    
    final int cellHeight = Integer.parseInt((String)(cellData.get("height")));
    final int cellWidth = Integer.parseInt((String)(cellData.get("width")));
    final int cols = Integer.parseInt((String)(cellData.get("cols")));
    
    // Load the atlas image
    atlasTexture = new Texture((String)json.get("atlas"), assetsDir + "fonts" + fileSeparator);

    
    /*
    * Zellgröße (BxH): 24*27
    *
    * TESTDATEN FÜR DEN CHARACTER "H"
    * Korrekte Werte wären:
    * id = 72
    * Row = 5
    * Col = 2
    * xPos (Pixel) = 24
    * yPos (Pixel) = 108
    * xPos (Norm.) = 24/800 = 0,03
    * yPos (Norm.) = 24/600 = 0,18
    *
    *
    * TESTDATEN FÜR DEN CHARACTER "!"
    * Korrekte Werte wären:
    * id = 33
    * Row = 1
    * Col = 3
    * xPos (Pixel) = 48
    * yPos (Pixel) = 0
    * xPos (Norm.) = 48/800 = 0,06
    * yPos (Norm.) = 0/600 = 0
    *
    *
    *
    *
    *
    * */
    
    // Parse the glyph data, create glyph objects from it and add them to the fonts glyph map
    int counter = 0;
    for (Object item : glyphArray) {
      // get the data of the current glyph definition
      JSONObject currentGlyphData = (JSONObject) item;
      final int id = Integer.parseInt((String)currentGlyphData.get("id"));
      final int glyphWidth = Integer.parseInt((String)currentGlyphData.get("width"));
      // Calculate row and col of the glyph
      final int glyphsRow = (counter / cols) + 1;
      final int glyphsCol = (counter + 1) % cols;
      // create the glyph object
      Glyph glyph = new Glyph(
        new Vector2((glyphsCol - 1) * cellWidth, (glyphsRow - 1)  * cellHeight),
        new Vector2(cellWidth, cellHeight),
        glyphWidth
      );
      // add glyph to the glyph map of this font
      this.glyphs.put((char) id, glyph);
      counter++;
    }
  }
  
  
  public Color getColor() {
    return this.color;
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
  
  /**
   * Returns the absolute path to the font definition
   */
  @Override
  public String getFilePath() {
    return null;
  }
  
  public String getName() {
    return name;
  }
  
  public Texture getAtlasTexture() {
    return atlasTexture;
  }
  
  public HashMap<Character, Glyph> getGlyphMap() {
    return glyphs;
  }
  
  public Glyph getGlyph(char character) {
    Glyph result = glyphs.get(character);
    if (result == null) {
      System.err.println("Tried to draw a character for which no glyph data exist!"); // TODO: Substitute undefined characters with a zero-length-space or something like that?
    }
    return result;
  }
  
  public boolean isItalic() {
    return isItalic;
  }
  
  public boolean isBold() {
    return isBold;
  }
  
  public int getSize() {
    return size;
  }
  
}
