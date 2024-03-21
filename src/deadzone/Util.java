package deadzone;

/**
 * Support class which provides some smart general utility functions
 */
public class Util {
  
  /** Stores the absolute path of the applications root folder */
  private static final String absRootDir;
  /** Stores the absolute path of the assets folder */
  private static final String absAssetsDir;
  /** Stores the absolute path to the library folder */
  private static final String absLibraryDir;
  /** Stores the absolute path to the fonts folder */
  private static final String absFontsDir;
  /** Stores the absolute path to the tiles folder */
  private static final String absTilesDir;
  
  
  static {
    // Initialize paths
    String separator = System.getProperty("file.separator");
    absRootDir = System.getProperty("user.dir") + separator;
    absLibraryDir = absRootDir + "libs" + separator;
    
    absAssetsDir = absRootDir + "assets" + separator;
    absFontsDir = absAssetsDir + "fonts" + separator;
    absTilesDir = absAssetsDir + "tiles" + separator;
  }
  
  
  /**
   * @return Application root path
   */
  public static String getAbsoluteRootDir()  {
    return absRootDir;
  }
  
  /**
   * @return Libraries folder path
   */
  public static String getAbsoluteLibraryDir()  {
    return absLibraryDir;
  }
  
  /**
   * @return Assets folder path
   */
  public static String getAbsoluteAssetsDir()  {
    return absAssetsDir;
  }
  
  /**
   * @return Fonts folder path
   */
  public static String getAbsoluteFontsDir()  {
    return absFontsDir;
  }
  
  /**
   * @return Tiles folder path
   */
  public static String getAbsoluteTilesDir()  {
    return absTilesDir;
  }
  
}