package deadzone.graphics.fonts;

import deadzone.Util;
import deadzone.assets.IAsset;
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
  
  private HashMap<Character, Glyph> glyphs;
  
  /** Path to the glyph definition for this Font */
  private String jsonFilePath;
  
  private String name;
  private boolean isItalic;
  private boolean isBold;
  
  
  /**
   * Prepares a new Font for use.
   */
  public Font(String xmlFile) {
    this.jsonFilePath = assetsDir + "fonts" + fileSeparator +  xmlFile;
  }
  
  /**
   * Returns the absolute path to the file, the asset object represents
   */
  @Override
  public String getFilePath() {
    return null;
  }
  
  /**
   *
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

    int startChar = (int) cellData.get("startChar");
    int cellWidth = (int) cellData.get("width");
    int cellHeight = (int) cellData.get("height");
    int fontSize = (int) styleData.get("height");
    
    // Load the atlas image
    final int atlasWidth, atlasHeight;
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer comp = stack.mallocInt(1);
      final String atlasImagePath = assetsDir + "fonts" + fileSeparator + json.get("atlas");
      atlasImage = stbi_load(atlasImagePath, w, h, comp, 4);
      if (atlasImage == null) {
        throw new RuntimeException("Failed to load texture \"" + atlasImagePath + "\"" + fileSeparator + stbi_failure_reason());
      }
      atlasWidth = w.get();
      atlasHeight = h.get();
    }
    
    // Parse the glyph data, create glyph objects from it and add them to the fonts glyph map
    for (Object item : glyphArray) {
      // get the data of the current glyph definition
      JSONObject currentGlyphData = (JSONObject) item;
      final int id = (int) currentGlyphData.get("id");
      final int glyphWidth = (int) currentGlyphData.get("width");
      final int widthOffset = (int) currentGlyphData.get("widthOffset");
      final int xOffset = (int) currentGlyphData.get("xOffset");
      final int yOffset = (int) currentGlyphData.get("yOffset");
      // create the glyph object
      Glyph glyph = new Glyph();
      // add glyph to the glyph map of this font
      this.glyphs.put((char) (startChar + id), glyph);
    }
    
  }
  
  
  public String getName() {
    return name;
  }
  
}
