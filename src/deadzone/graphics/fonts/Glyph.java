package deadzone.graphics.fonts;

import deadzone.math.Vector2;


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
  
  /** The real glyph width */
  private float glyphWidth;
  
  
  /**
   * Creates a new glyph
   * @param cellPos Location of the glyph in the font atlas image
   * @param cellSize Size of the glyph in the font atlas image
   * @param glyphWidth Actual glyph width
   */
  public Glyph(Vector2 cellPos, Vector2 cellSize, int glyphWidth) {
    this.position = cellPos;
    this.size = cellSize;
    this.glyphWidth = glyphWidth;
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
  
  public float getGlyphWidth() {
    return glyphWidth;
  }
  
}
