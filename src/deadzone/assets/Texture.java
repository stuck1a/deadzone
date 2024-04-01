package deadzone.assets;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;


public class Texture implements IAsset {
  
  final AssetTypes assetType = AssetTypes.TEXTURE;
  final String filepath;
  
  private ByteBuffer data;
  
  public IntBuffer w;
  public IntBuffer h;
  public IntBuffer comp;
  
  public int width;
  public int height;
  
  
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
   * Reads the texture file from the hard drive and uploads it into GPU memory
   */
  @Override
  public void load() {
    // Prepare the buffers to store width, height and the texture component count (RGBA = 4)
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer comp = stack.mallocInt(1);
      // Load the image data from the stored texture object
      data = stbi_load(filepath, w, h, comp, 4);
      if (data == null) {
        throw new RuntimeException("Failed to load texture \"" + filepath + "\"" + System.lineSeparator() + stbi_failure_reason());
      }
      width = w.get();
      height = h.get();
    }
  }
  
  
  /**
   * Returns the binary data of the texture
   */
  public ByteBuffer getData() {
    if (data == null) {
      load();
    }
    return data;
  }
  
}
