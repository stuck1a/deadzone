package deadzone.graphics.fonts;

import deadzone.Util;
import deadzone.assets.Texture;
import deadzone.graphics.Color;
import deadzone.math.Vector2;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;


/**
 * This class represents a single font with all its glyphs.
 *
 * TODO: Add pre-created textures for to each glyph
 *
 */
public class Font  {
  
  /** Stored the generated atlas texture on which als character are printed on initialization */
  private Texture atlasTexture;
  private HashMap<Character, Glyph> glyphs = new HashMap<>();
  /** Path to the glyph definition for this Font */
  private final String xmlFilePath;
  private String name;
  private boolean isBold;
  private int size;
  private Color color;
  
  
  /**
   * Prepares a new Font for use.
   */
  public Font(String fontDefXmlFile) {
    this.xmlFilePath = Util.getAssetsDir() + "fonts" + System.getProperty("file.separator") +  fontDefXmlFile;
  }
  
  
  /**
   * Prepares a new Font for use.
   * Presets the given color
   */
  public Font(String fontDefXmlFile, Color color) {
    this.xmlFilePath = Util.getAssetsDir() + "fonts" + System.getProperty("file.separator") +  fontDefXmlFile;
    this.color = color;
  }
  
  
  /**
   * Generates glyph definitions and loads the atlas image
   */
  public void load() {
    // NEUE LOGIK
    // Parse the given xml file, create the glyphs from it and load the atlas image
    Document xml = Util.parseXML(xmlFilePath);
    if (xml == null) {
      System.err.println("Failed to load font definition!");
      return;
    }
  
    // get the "font" node
    Node fontNode = xml.getElementsByTagName("font").item(0);
    
    // fetch font attributes
    NamedNodeMap attributes = fontNode.getAttributes();
    name = attributes.getNamedItem("name").getTextContent();
    isBold = Boolean.getBoolean(attributes.getNamedItem("bold").getTextContent());
    size = Integer.parseInt(attributes.getNamedItem("size").getTextContent());
    atlasTexture = new Texture(
      attributes.getNamedItem("atlas").getTextContent(),
      Util.getAssetsDir() + "fonts" + System.getProperty("file.separator")
    );
    
    // fetch and process glyph data
    NodeList fontNodeContent = xml.getElementsByTagName("font").item(0).getChildNodes();
  
    final int len = fontNodeContent.getLength();
    for (int i = 0; i < len; i++) {
      Node child = fontNodeContent.item(i);
      // Skip invalid nodes
      if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
        
        // Process "char" nodes
        if (child.getNodeName().equals("char")) {
          NamedNodeMap glyphAttr = child.getAttributes();
          final char c = (char)(Integer.parseInt(glyphAttr.getNamedItem("id").getTextContent()));
          final int x = Integer.parseInt(glyphAttr.getNamedItem("x").getTextContent());
          final int y = Integer.parseInt(glyphAttr.getNamedItem("y").getTextContent());
          final int width = Integer.parseInt(glyphAttr.getNamedItem("width").getTextContent());
          final int height = Integer.parseInt(glyphAttr.getNamedItem("height").getTextContent());
          final int xOffset = Integer.parseInt(glyphAttr.getNamedItem("Xoffset").getTextContent());
          final int yOffset = Integer.parseInt(glyphAttr.getNamedItem("Yoffset").getTextContent());
          final int origWidth = Integer.parseInt(glyphAttr.getNamedItem("OrigWidth").getTextContent());
          final int origHeight = Integer.parseInt(glyphAttr.getNamedItem("OrigHeight").getTextContent());
          
          Glyph glyph = new Glyph(
            new Vector2(x, y),
            new Vector2(width, height),
            origWidth
          );
  
          this.glyphs.put(c, glyph);
          continue;
        }
        
        // Process kerning nodes (all glyphs are created at this point, because kerning data comes after char data in the XML
        if (child.getNodeName().equals("kerning")) {
          NamedNodeMap kerningAttr = child.getAttributes();
          final char relatedChar = (char)(Integer.parseInt(kerningAttr.getNamedItem("first").getTextContent()));
          final char targetChar = (char)(Integer.parseInt(kerningAttr.getNamedItem("second").getTextContent()));
          final int value = Integer.parseInt(kerningAttr.getNamedItem("value").getTextContent());
          
          Glyph glyph = this.glyphs.get(relatedChar);
          if (glyph == null) {
            System.err.println("Skipped invalid kerning entry in font definition \"" + name + "\"");
            continue;
          }
          glyph.addKerning(targetChar, value);
          continue;
        }
        
        
      }
    }
    
    
    
    
    /*
    // ALTE LOGIK
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
    atlasTexture = new Texture((String)json.get("atlas"), Util.getAssetsDir() + "fonts" + System.getProperty("file.separator"));
    
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
    */
    
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
  
  public boolean isBold() {
    return isBold;
  }
  
  public int getSize() {
    return size;
  }
  
}
