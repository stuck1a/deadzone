package deadzone.graphics.fonts;

import deadzone.Util;
import deadzone.assets.IAsset;
import deadzone.math.Vector2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;


/**
 * This class represents a single font with all its glyphs.
 */
public class Font implements IAsset {
  
  /** Stored the generated atlas texture on which als character are printed on initialization */
  private ByteBuffer atlasImage;
  
  private HashMap<Character, Glyph> glyphs = new HashMap<>();
  
  /** Path to the glyph definition for this Font */
  private final String jsonFilePath;
  
  private String name;
  private boolean isItalic;
  private boolean isBold;
  private int size;
  
  
  /**
   * Prepares a new Font for use.
   */
  public Font(String fontDefJsonPath) {
    this.jsonFilePath = assetsDir + "fonts" + fileSeparator +  fontDefJsonPath;
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
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer comp = stack.mallocInt(1);
      final String atlasImagePath = assetsDir + "fonts" + fileSeparator + json.get("atlas");
      atlasImage = stbi_load(atlasImagePath, w, h, comp, 4);
      if (atlasImage == null) {
        throw new RuntimeException("Failed to load texture \"" + atlasImagePath + "\"" + fileSeparator + stbi_failure_reason());
      }
    }
    
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
        new Vector2(glyphsRow * cellWidth, glyphsCol * cellHeight),
        new Vector2(cellWidth, cellHeight),
        glyphWidth
      );
      // add glyph to the glyph map of this font
      this.glyphs.put((char) id, glyph);
      counter++;
    }
    
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
  
  public ByteBuffer getAtlasImage() {
    return atlasImage;
  }
  
  public HashMap<Character, Glyph> getGlyphs() {
    return glyphs;
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
