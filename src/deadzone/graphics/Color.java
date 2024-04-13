package deadzone.graphics;

/**
 * This class describes an RGBA color
 * and provide some support functions like normalizing the color value.
 */
public class Color implements Cloneable {

  private int red;
  private int green;
  private int blue;
  private int alpha;
  
  /**
   * Creates a new color without transparency.
   *
   * @param r Red value between 0-255
   * @param g Green value between 0-255
   * @param b Blue value between 0-255
   */
  public Color(int r, int g, int b) {
    setRed(r);
    setGreen(g);
    setBlue(b);
    setAlpha(255);
  }
  
  
  /**
   * Creates a new color with transparency.
   *
   * @param r     Red value between 0-255
   * @param g     Green value between 0-255
   * @param b     Blue value between 0-255
   * @param alpha Transparency value between 0-255
   */
  public Color(int r, int g, int b, int alpha) {
    setRed(r);
    setGreen(g);
    setBlue(b);
    setAlpha(alpha);
  }
  
  
  /**
   * @return The red value normalized between 0 and 1 (Rounded to two decimal places)
   */
  public float getRedNormalized() {
    return Math.round(red/255f * 100) / 100f;
  }
  
  /**
   * @return The green value normalized between 0 and 1 (Rounded to two decimal places)
   */
  public float getGreenNormalized() {
    return Math.round(green/255f * 100) / 100f;
  }
  
  /**
   * @return The blue value normalized between 0 and 1 (Rounded to two decimal places)
   */
  public float getBlueNormalized() {
    return Math.round(blue/255f * 100) / 100f;
  }
  
  /**
   * @return The alpha value normalized between 0 and 1 (Rounded to two decimal places)
   */
  public float getAlphaNormalized() {
    return Math.round(alpha/255f * 100) / 100f;
  }
  
  /**
   * @return The regular red value between 0 and 255
   */
  public int getRed() {
    return red;
  }
  
  public int getBlue() {
    return blue;
  }
  
  public int getGreen() {
    return green;
  }
  
  public int getAlpha() {
    return alpha;
  }
  
  public void setRed(int red) {
    if (red > 255) {
      this.red = 255;
      return;
    }
    if (red < 0) {
      this.red = 0;
      return;
    }
    this.red = red;
  }
  
  public void setGreen(int green) {
    if (green > 255) {
      this.green = 255;
      return;
    }
    if (green < 0) {
      this.green = 0;
      return;
    }
    this.green = green;
  }
  
  public void setBlue(int blue) {
    if (blue > 255) {
      this.blue = 255;
      return;
    }
    if (blue < 0) {
      this.blue = 0;
      return;
    }
    this.blue = blue;
  }
  
  public void setAlpha(int alpha) {
    if (alpha > 255) {
      this.alpha = 255;
      return;
    }
    if (alpha < 0) {
      this.alpha = 0;
      return;
    }
    this.alpha = alpha;
  }
  
  @Override
  public Color clone() {
    try {
      return (Color) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
  
}
