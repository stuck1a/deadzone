package deadzone;

/**
 * Support class which provides some smart general utility functions
 */
public class Util {
  
  /** Stores the absolute path of the applications root folder */
  private static final String absRootDir;
  
  /** Stores the absolute path to the library folder */
  private static final String absLibrariesDir;
  
  /** Stores the absolute path to the ressources folder */
  private static final String absResourcesDir;
  
  /** Stores the absolute path to the library folder */
  private static final String absShadersDir;
  
  /** Stores the absolute path of the assets folder */
  private static final String absAssetsDir;
  
  /** Stores the absolute path to the fonts folder */
  private static final String absFontsDir;
  
  /** Stores the absolute path to the tiles folder */
  private static final String absTilesDir;
  
  
  static {
    // Initialize fixed paths
    String separator = System.getProperty("file.separator");
    absRootDir = System.getProperty("user.dir") + separator;
    absLibrariesDir = absRootDir + "libs" + separator;
    
    absResourcesDir = absRootDir + "res" + separator;
    absShadersDir = absResourcesDir + "shaders" + separator;
    
    absAssetsDir = absRootDir + "assets" + separator;
    absFontsDir = absAssetsDir + "fonts" + separator;
    absTilesDir = absAssetsDir + "tiles" + separator;
  }
  
  
  /**
   * @return Application root path
   */
  public static String getRootDir()  {
    return absRootDir;
  }
  
  /**
   * @return Libraries folder path
   */
  public static String getResourcesDir()  {
    return absResourcesDir;
  }
  
  /**
   * @return Resources folder path
   */
  public static String getShadersDir()  {
    return absShadersDir;
  }
  
  /**
   * @return Shaders folder path
   */
  public static String getLibrariesDir()  {
    return absLibrariesDir;
  }
  
  /**
   * @return Assets folder path
   */
  public static String getAssetsDir()  {
    return absAssetsDir;
  }
  
  /**
   * @return Fonts folder path
   */
  public static String getFontsDir()  {
    return absFontsDir;
  }
  
  /**
   * @return Tiles folder path
   */
  public static String getTilesDir()  {
    return absTilesDir;
  }
  
}