package deadzone.graphics.fonts;

import deadzone.math.Vector2;


/**
 * This class represents a single glyph of a font.
 * It holds all information to find the glyph in the corresponding font atlas image
 * and print it properly on a font texture.
 */
public class Glyph {

  /** ID handle of the font texture */
  private int id;
  /** The position of the glyph in the corresponding font atlas image */
  private Vector2 position;
  /** Width and Height of the glyph in the corresponding font atlas image */
  private Vector2 size;
  /** The bearing values (offset from baseline to left/top of glyph) */
  private Vector2 bearing;
  /** The horizontal advance (offset from origin to the origin of the next glyph) */
  private float advance;
  
  /**
   * Creates a new glyph
   */
  public Glyph(int x, int y, int width, int height) {
  
  }
  
  
}
