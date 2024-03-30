package deadzone.assets;

public class Texture implements IAsset {
  
  final AssetTypes assetType = AssetTypes.TEXTURE;
  final String filepath;
  
  CharSequence data;
  
  /** Unique object identifier */
  String identifier;
  
  /**
   * Creates a new texture object
   * @param path Relative to the assets/textures directory
   */
  public Texture(String path) {
    filepath = assetsDir + "textures" + fileSeparator + path;
    load();
  }
  
  
  /**
   * Returns the absolute path to the texture file
   */
  @Override
  public String getFilePath() {
    return filepath;
  }
  
  
  /**
   * Loads the texture file from the hard drive.
   */
  @Override
  public void load() {
    data = "";
  }
  
  
  /**
   * Returns the binary data of the texture
   */
  public CharSequence getData() {
    if (data == null) {
      load();
    }
    return data;
  }
  
}
