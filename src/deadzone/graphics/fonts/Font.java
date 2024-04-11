package deadzone.graphics.fonts;

import deadzone.Util;
import deadzone.assets.Texture;
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
  private int size;
  
  
  /**
   * Prepares a new Font for use.
   */
  public Font(String fontDefXmlFile) {
    this.xmlFilePath = fontDefXmlFile;
    load();
  }
  
  
  /**
   * Generates glyph definitions and loads the atlas image
   */
  public void load() {
    // Parse the given xml file, create the glyphs from it and load the atlas image
    final Document xml = Util.parseXML(xmlFilePath);
    if (xml == null) {
      System.err.println("Failed to load font definition!");
      return;
    }
    
    // fetch font attributes
    final Node fontNode = xml.getElementsByTagName("font").item(0);
    final NamedNodeMap attributes = fontNode.getAttributes();
    size = Integer.parseInt(attributes.getNamedItem("height").getTextContent().replace("px", "").trim());
    
    atlasTexture = new Texture(
      attributes.getNamedItem("atlas").getTextContent(),
      Util.getFontsDir()
    );
    
    // fetch and process glyph data
    final NodeList fontNodeContent = xml.getElementsByTagName("font").item(0).getChildNodes();
  
    final int len = fontNodeContent.getLength();
    for (int i = 0; i < len; i++) {
      final Node child = fontNodeContent.item(i);
      // Skip invalid nodes
      if (child != null && child.getNodeType() == Node.ELEMENT_NODE) {
        
        // Process "char" nodes
        if (child.getNodeName().equals("char")) {
          final NamedNodeMap glyphAttr = child.getAttributes();
          final char c = (char)(Integer.parseInt(glyphAttr.getNamedItem("id").getTextContent()));
          final Vector2 texturePos = new Vector2(
            Float.parseFloat(glyphAttr.getNamedItem("x").getTextContent()),
            Float.parseFloat(glyphAttr.getNamedItem("y").getTextContent())
          );
          final Vector2 textureSize = new Vector2(
            Float.parseFloat(glyphAttr.getNamedItem("width").getTextContent()),
            size
          );
          this.glyphs.put(c, new Glyph(texturePos, textureSize));
          continue;
        }
        
        // Process kerning nodes (all glyphs are created at this point, because kerning data comes after char data in the XML
        if (child.getNodeName().equals("kerning")) {
          final NamedNodeMap kerningAttr = child.getAttributes();
          final char firstChar = (char)(Integer.parseInt(kerningAttr.getNamedItem("first").getTextContent()));
          final char secondChar = (char)(Integer.parseInt(kerningAttr.getNamedItem("second").getTextContent()));
          final float value = Float.parseFloat(kerningAttr.getNamedItem("value").getTextContent());
          
          final Glyph glyph = this.glyphs.get(secondChar);
          if (glyph == null) {
            System.err.println("Skipped invalid kerning entry in font definition \"" + xmlFilePath + "\"");
            continue;
          }
          glyph.addKerning(firstChar, value);
          continue;
        }
        
      }
    }
    
  }
  
  /**
   * Returns the absolute path to the font definition
   */
  public String getFilePath() {
    return null;
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
      System.err.println("Tried to receive a character for which no glyph data exist!"); // TODO: Substitute undefined characters with a zero-length-space or something like that?
    }
    return result;
  }
  
  public int getOriginalSize() {
    return size;
  }
  
}
