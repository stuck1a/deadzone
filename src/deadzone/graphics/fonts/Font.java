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
public class Font {
  
  /** Stored the generated atlas texture on which als character are printed on initialization */
  private Texture atlasTexture;
  private final HashMap<Character, Glyph> glyphs = new HashMap<>();
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
    // Parse the given xml file, create the glyphs from it and load the atlas image
    Document xml = Util.parseXML(xmlFilePath);
    if (xml == null) {
      System.err.println("Failed to load font definition!");
      return;
    }
    
    // fetch font attributes
    Node fontNode = xml.getElementsByTagName("font").item(0);
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
          final Vector2 texturePos = new Vector2(
            Float.parseFloat(glyphAttr.getNamedItem("x").getTextContent()),
            Float.parseFloat(glyphAttr.getNamedItem("y").getTextContent())
          );
          final Vector2 textureSize = new Vector2(
            Float.parseFloat(glyphAttr.getNamedItem("width").getTextContent()),
            Float.parseFloat(glyphAttr.getNamedItem("height").getTextContent())
          );
          this.glyphs.put(c, new Glyph(texturePos, textureSize));
          continue;
        }
        
        // Process kerning nodes (all glyphs are created at this point, because kerning data comes after char data in the XML
        if (child.getNodeName().equals("kerning")) {
          NamedNodeMap kerningAttr = child.getAttributes();
          final char firstChar = (char)(Integer.parseInt(kerningAttr.getNamedItem("first").getTextContent()));
          final char secondChar = (char)(Integer.parseInt(kerningAttr.getNamedItem("second").getTextContent()));
          final float value = Float.parseFloat(kerningAttr.getNamedItem("value").getTextContent());
          
          Glyph glyph = this.glyphs.get(secondChar);
          if (glyph == null) {
            System.err.println("Skipped invalid kerning entry in font definition \"" + name + "\"");
            continue;
          }
          glyph.addKerning(firstChar, value);
          continue;
        }
        
        
      }
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
