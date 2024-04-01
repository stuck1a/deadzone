package deadzone.graphics.fonts;

import deadzone.Util;
import deadzone.assets.IAsset;
import deadzone.assets.Texture;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;


/**
 * This class represents a single font with all its glyphs.
 */
public class Font implements IAsset {
  
  /** Stored the generated atlas texture on which als character are printed on initialization */
  private Texture atlasImage;
  
  private HashMap<Character, Glyph> glyphs;
  
  /** Path to the glyph definition for this Font */
  private String xmlFilePath;
  
  private String name;
  
  
  /**
   * Prepares a new Font for use.
   */
  public Font(String xmlFile) {
    this.xmlFilePath = assetsDir + "fonts" + fileSeparator +  xmlFile;
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
    // Parse the given xml file, create the glyphs from it and load the atlas image
    Document xml = Util.parseXML(xmlFilePath);
    if (xml == null) {
      System.err.println("Failed to load font definition!");
      return;
    }
  
    // get the "font" node
    Node fontNode = xml.getElementsByTagName("font").item(0);
    NodeList fontNodeContent = xml.getElementsByTagName("font").item(0).getChildNodes();
    
    
    this.name = xml.getElementsByTagName("font").item(2).getTextContent();
  }
  
  
  public String getName() {
    return name;
  }
  
}
