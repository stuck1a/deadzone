package deadzone.graphics.fonts;

import deadzone.graphics.Color;
import deadzone.graphics.ui.Text;
import deadzone.math.Vector2;


/**
 * The Pen is used to add text
 */
public class Pen implements Cloneable {
  protected FontFamily fontFamily;
  protected Vector2 pos;
  protected Color color;
  protected int fontSize;
  protected boolean bold = false;
  protected boolean italic = false;
  private Font activeFont;
  private float activeScaling;
  
  /** Flag whether anything has been changed which requires to recalculate the scaling factor before rendering */
  private boolean hasChanged = true;
  
  
  public Pen(FontFamily fontFamily, int size) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.color = new Color(0, 0, 0);
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = new Color(0, 0, 0);
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = new Color(0, 0, 0);
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y, Color color) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = color;
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y, Color color, boolean bold) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = color;
    this.bold = bold;
  }
  
  public Pen(FontFamily fontFamily, int size, float x, float y, Color color, boolean bold, boolean italic) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = new Vector2(x, y);
    this.color = color;
    this.bold = bold;
    this.italic = italic;
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos, Color color) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = color;
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos, Color color, boolean bold) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = color;
    this.bold = bold;
  }
  
  public Pen(FontFamily fontFamily, int size, Vector2 pos, Color color, boolean bold, boolean italic) {
    this.fontFamily = fontFamily;
    this.fontSize = size;
    this.pos = pos;
    this.color = color;
    this.bold = bold;
    this.italic = italic;
  }
  
  
  /**
   * Writes some text which will be rendered in the upcoming frame.
   * Returns the Pen object to update the pen position of the callee.
   */
  public Text writeText(String text) {
    // Choose Font and calculate scaling factor, if needed
    if (hasChanged) {
      updateFontData();
      hasChanged = false;
    }
    
    // TODO: Apply 16° rotation matrix if italic = true
    
    // If we dont clone pen and color, it would be text would be influenced
    // if this pen is changed afterwards. But we only want to sync the pos and only from Text to this, not vice versa!
    Text obj = new Text(this.clone(), activeFont, text, color.clone(), activeScaling);
    
    // Update the position of this pen
    this.setPos(obj.getNewPenPos());
    
    return obj;
  }
  
  
  
  /**
   * This method will set the font which is closest to the target size and then calculate and set the scale factor
   * which is required to render the text in the target size.
   */
  protected void updateFontData() {
    // Make sure, the Pen is fully initialized. If not, add default values where necessary
    if (pos == null) pos = new Vector2(0, 0);
    if (color == null) color = new Color(0, 0, 0);
    
    // Get the best suitable font variant
    final Font smallFont = bold ? fontFamily.getSmallBoldFont() : fontFamily.getSmallRegularFont();
    final Font largeFont = bold ? fontFamily.getLargeBoldFont() : fontFamily.getLargeRegularFont();
    activeFont = (Math.abs(fontSize - smallFont.getOriginalSize()) < Math.abs(fontSize - largeFont.getOriginalSize())) ? smallFont : largeFont;
    
    // Recalculate the required scaling factor to reach the effective target size
    activeScaling = (float)fontSize / (float)activeFont.getOriginalSize() + 1;
  }
  
  
  /**
   * Change the height of any text which will be written afterwards to the given pixel value
   */
  public Pen setFontSize(int size) {
    if (size < 1) size = 1;
    this.fontSize = size;
    this.hasChanged = true;
    return this;
  }
  
  public FontFamily getFont() {
    return fontFamily;
  }
  
  public Pen setFont(FontFamily fontFamily) {
    this.fontFamily = fontFamily;
    this.hasChanged = true;
    return this;
  }
  
  public Vector2 getPos() {
    if (pos == null) pos = new Vector2(0, 0);
    return pos;
  }
  
  public float getX() {
    if (pos == null) pos = new Vector2(0, 0);
    return pos.x;
  }
  
  public Pen moveX(float xOffset) {
    if (pos == null) pos = new Vector2(0, 0);
    pos.x += xOffset;
    return this;
  }
  
  public Pen moveY(float yOffset) {
    if (pos == null) pos = new Vector2(0, 0);
    pos.y += yOffset;
    return this;
  }
  
  public float getY() {
    if (pos == null) pos = new Vector2(0, 0);
    return pos.y;
  }
  
  public Pen setPos(Vector2 pos) {
    this.pos = pos;
    return this;
  }
  
  public Pen setPos(float x, float y) {
    if (pos == null) {
      pos = new Vector2(x, y);
      return this;
    }
    pos.x = x;
    pos.y = y;
    return this;
  }
  
  public Pen setX(float x) {
    if (pos == null) {
      pos = new Vector2(x, 0);
      return this;
    }
    pos.x = x;
    return this;
  }
  
  public Pen setY(float y) {
    if (pos == null) {
      pos = new Vector2(0, y);
      return this;
    }
    pos.y = y;
    return this;
  }
  
  public Color getColor() {
    if (color == null) color = new Color(0, 0, 0);
    return color;
  }
  
  public Pen setColor(Color color) {
    this.color = color;
    return this;
  }
  
  public Pen setColor(int red, int green, int blue) {
    if (color == null) {
      color = new Color(red, green, blue);
      return this;
    }
    color.setRed(red);
    color.setGreen(green);
    color.setBlue(blue);
    return this;
  }
  
  public Pen setColor(int red, int green, int blue, int alpha) {
    if (color == null) {
      color = new Color(red, green, blue, alpha);
      return this;
    }
    color.setRed(red);
    color.setGreen(green);
    color.setBlue(blue);
    color.setAlpha(alpha);
    return this;
  }
  
  public Pen setAlpha(int alpha) {
    if (color == null) {
      color = new Color(0, 0, 0, alpha);
      return this;
    }
    color.setAlpha(alpha);
    return this;
  }
  
  public int getFontSize() {
    return fontSize;
  }
  
  public boolean isBold() {
    return bold;
  }
  
  public Pen setBold(boolean bold) {
    this.bold = bold;
    hasChanged = true;
    return this;
  }
  
  public boolean isItalic() {
    return italic;
  }
  
  public Pen setItalic(boolean italic) {
    this.italic = italic;
    return this;
  }
  
  @Override
  public Pen clone() {
    final Pen clone = new Pen(
      fontFamily.clone(),
      fontSize,
      new Vector2(pos.x, pos.y),
      color.clone(),
      bold,
      italic
    );
    clone.activeScaling = activeScaling;
    clone.activeFont = activeFont;
    return clone;
  }
  
}
