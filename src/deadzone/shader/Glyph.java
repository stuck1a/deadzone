package deadzone.shader;

/**
 * This class represents single glyphs of a font
 */
public class Glyph {
  
  public final int x;
  public final int y;
  public final int height;
  public final int width;
  public final float advance;
  
  
  
  /**
   * Creates a new font glyph
   *
   * @param width Glyph width
   * @param height Glyph height
   * @param x Font texture x coordinate
   * @param y Y Font texture x coordinate
   * @param advance Offset width
   */
  public Glyph(int width, int height, int x, int y, float advance) {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
    this.advance = advance;
  }
  
}