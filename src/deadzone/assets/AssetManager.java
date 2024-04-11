package deadzone.assets;

import deadzone.Util;
import deadzone.graphics.fonts.Font;
import deadzone.graphics.fonts.FontFamily;

import java.util.HashMap;


/**
 * The Asset Manager is the central class for accessing assets.
 * It allows to preload some or even all available assets at a given moment and
 * provides a registry which ensures, that assets are loaded only once.
 */
public class AssetManager {
  
  HashMap<String, Texture> textures = new HashMap<>();
  HashMap<String, Font> fonts = new HashMap<>();
  
  /**
   * For now, we hardcode all available assets here.
   * TODO: For later maybe use script files which define all concrete assets to load or something like this
   */
  public void loadAllAssets() {
    textures.put("blank", new Texture("blank.png"));
    textures.put("1", new Texture("1.png"));
    textures.put("1_marked", new Texture("1_marked.png"));
    textures.put("2", new Texture("2.jpg"));
    
    // Load Font: Arial, 70px, regular
    Font arial70 = new Font("arial70.xml");
    arial70.load();
    fonts.put(arial70.getName(), arial70);
    
    
    // Initialize font family "Arial"
    final String fontsDir = Util.getFontsDir();
    FontFamily arial = new FontFamily(
      fontsDir + "arial20.xml",
      fontsDir + "arial70.xml",
      fontsDir + "arial20b.xml",
      fontsDir + "arial70b.xml"
    );
    
    
    
  }
  
  
  public Texture getTexture(String identifier) {
    final Texture result = textures.get(identifier);
    if (result == null) {
      throw new RuntimeException("Could not find requested texture \""+ identifier +"\"!");
    }
    return result;
  }
  
  public Font getFont(String identifier) {
    final Font result = fonts.get(identifier);
    if (result == null) {
      throw new RuntimeException("Could not find requested font \""+ identifier +"\"!");
    }
    return result;
  }
  
}
