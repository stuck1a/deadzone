package deadzone.graphics.fonts;

import deadzone.graphics.Color;
import deadzone.graphics.ui.Text;
import deadzone.math.Vector2;


/**
 * The Pen is used to add text
 */
public class Pen {
  public FontFamily fontFamily;
  public Vector2 pos;
  public Color color;
  protected int fontSize;
  protected boolean bold = false;
  protected boolean italic = false;
  private Font activeFont;
  private float activeScaling;
  
  
  public Pen(FontFamily fontFamily, int size) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.color = new Color(0, 0, 0);
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = new Color(0, 0, 0);
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = new Color(0, 0, 0);
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y, Color color) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = color;
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y, Color color, boolean bold) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = color;
    this.bold = bold;
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y, Color color, boolean bold, boolean italic) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = color;
    this.bold = bold;
    this.italic = italic;
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos, Color color) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = color;
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos, Color color, boolean bold) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = color;
    this.bold = bold;
    calculateScalingFactor(size);
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos, Color color, boolean bold, boolean italic) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = color;
    this.bold = bold;
    this.italic = italic;
    calculateScalingFactor(size);
  }
  
  
  /**
   * Writes some text which will be rendered in the upcoming frame.
   * Returns the Text object for further use
   */
  public Text writeText(String text) {
    Text obj = new Text(pos.x, pos.y, activeFont, text, activeScaling);
    return obj;
  }
  
  
  /**
   * Change the height of any text which will be written afterwards to the given pixel value
   */
  public void setFontSize(int size) {
    if (size < 1) size = 1;
    this.fontSize = size;
    calculateScalingFactor(size);
  }
  
  
  /**
   * This method will set the font which is closest to the target size and then calculate and set the scale factor
   * which is required to render the text in the target size.
   */
  private void calculateScalingFactor(int targetSize) {
  
  }
  

  
  
}
