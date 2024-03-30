package deadzone.assets;

import deadzone.Util;


/**
 * Every class which represent a specific asset type (Textures, Fonts, Music, ...) must implement this interface
 * which allows managing of its objects through the AssetManager.
 */
public interface IAsset {
  
  final String assetsDir = Util.getAssetsDir();
  final String fileSeparator = System.getProperty("file.separator");
  
  /** Returns the absolute path to the file, the asset object represents */
  String getFilePath();
  
  void load();
  
}
