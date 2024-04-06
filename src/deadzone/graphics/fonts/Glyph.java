package deadzone.graphics.fonts;

import deadzone.math.Vector2;

import java.util.HashMap;


/**
 * This class represents a single glyph of a font.
 */
public class Glyph {
  
  /** ID handle of the font texture */
  private int id;
  /** The position of the glyph in the corresponding font atlas image */
  private Vector2 position;
  /** Width and Height of the glyph in the corresponding font atlas image */
  private Vector2 size;
  private Vector2 origSize;
  /** Offset between texture size and actual character size */
  private Vector2 offset;
  /** All kerning values which must be taken into account when placing a given glyph after this one */
  private HashMap<Character, Integer> kernings = new HashMap<>();
  
  
  /**
   * Creates a new glyph.
   * @param pos Location of the glyph in the font atlas image
   * @param size Size of the glyph in the font atlas image
   * @param offset Offset between glyph size and the actual glyph position
   * @param origSize Real glyph size
   */
  public Glyph(Vector2 pos, Vector2 size, Vector2 offset, Vector2 origSize) {
    this.position = pos;
    this.size = size;
    this.offset = offset;
    this.origSize = origSize;
  }
  
  
  /**
   * Adds a new kerning info to this glyph.
   * @param target Following char on which the kerning will be applied
   * @param value Offset in Pixel for the placement of the upcoming target character
   */
  public void addKerning(char target, int value) {
    kernings.put(target, value);
  }
  
  /**
   * Returns the kerning value in pixel for the given successor character or 0 if no matching kerning pair exist
   */
  public int getKerning(char successor) {
    Integer value = kernings.get(successor);
    return value == null ? 0 : value;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getId() {
    return id;
  }
  
  /**
   * Returns the position of the correponding character in the atlas, basically the UV coordinates.
   * @return
   */
  public Vector2 getPosition() {
    return position;
  }
  
  public Vector2 getSize() {
    return size;
  }
  
  public Vector2 getOffset() {
    return offset;
  }
  
  public Vector2 getOrigSize() {
    return origSize;
  }
  
}
