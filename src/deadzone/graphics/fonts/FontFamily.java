package deadzone.graphics.fonts;

import java.util.HashMap;


public class FontFamily {
  
  /**
   * Stores all 4 subfonts:
   * 0 - small regular
   * 1 - large regular
   * 2 - small bold
   * 3 - large bold
   */
  final HashMap<Integer, Font> fonts;
  
  
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
  
  
  
  
}
