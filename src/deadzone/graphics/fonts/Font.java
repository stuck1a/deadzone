package deadzone.graphics.fonts;

import deadzone.assets.Texture;

import java.util.HashMap;


/**
 * This class represents a single font with all its glyphs.
 */
public class Font {
  
  /** Stored the generated atlas texture on which als character are printed on initialization */
  private Texture atlasImage;
  
  private HashMap<Character, Glyph> glyphs;
  
  private final String name;
  
  
  /**
   * Prepares a new TrueType Font for use.
   */
  public Font(String path, String name) {
    this.name = name;
    
    // Create font atlas and add glyph mappings
    int i = 0;
    while (i < 120) {  // Only map a-Z for now
      i++;
    }
    
  }
  
  

  
  
}
