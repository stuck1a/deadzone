package deadzone.render;

/**
 * This class represents single glyphs of a font
 */
public class Glyph {
  
  public final int x;
  public final int y;
  public final int height;
  public final int width;
  
  
  
  /**
   * Creates a new font glyph
   *
   * @param width Glyph width
   * @param height Glyph height
   * @param x Font texture x coordinate
   * @param y Y Font texture x coordinate
   */
  public Glyph(int width, int height, int x, int y) {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }
  
}