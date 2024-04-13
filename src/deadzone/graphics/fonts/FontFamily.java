package deadzone.graphics.fonts;

import java.util.HashMap;


public class FontFamily implements Cloneable {
  
  /**
   * Stores all 4 subfonts:
   * 0 - small regular
   * 1 - large regular
   * 2 - small bold
   * 3 - large bold
   */
  HashMap<Integer, Font> fonts;
  
  
  /**
   * Creates a new Font Family object.
   * @param sr Path to the definition of small regular font variant
   * @param lr Path to the definition of large regular font variant
   * @param sb Path to the definition of small bold font variant
   * @param lb Path to the definition of large bold font variant
   */
  public FontFamily(String sr, String lr, String sb, String lb) {
    fonts = new HashMap<Integer, Font>() {{
      put(0, new Font(sr));
      put(1, new Font(lr));
      put(2, new Font(sb));
      put(3, new Font(lb));
    }};
  }
  
  
  public Font getSmallRegularFont() {
    return fonts.get(0);
  }
  
  public Font getLargeRegularFont() {
    return fonts.get(1);
  }
  
  public Font getSmallBoldFont() {
    return fonts.get(2);
  }
  
  public Font getLargeBoldFont() {
    return fonts.get(3);
  }
  
  @Override
  public FontFamily clone() {
    try {
      FontFamily clone = (FontFamily) super.clone();
      clone.fonts = new HashMap<Integer, Font>() {{
        put(0, fonts.get(0));
        put(1, fonts.get(1));
        put(2, fonts.get(2));
        put(3, fonts.get(3));
      }};
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
  
}
