package deadzone;

import java.io.File;
import java.io.IOException;

/**
 * Support class which provides some smart general utility functions
 */
public class Util {
  
  /** Stores the absolute path of the applications root dir once located for better performance */
  private static String absClientDir;
  
  /** Stores the absolute path of the systems user dir once located for better performance */
  private static final String absUserDir;
  
  
  static {
    // Initialize application dir
    File currentDirFile = new File(".");
    String helper = currentDirFile.getAbsolutePath();
    try {
      absClientDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());
    } catch (IOException e) {
      System.err.println("Failed to determine the application path!");
      throw new RuntimeException("Fatal error occurred, check log for more details");
    }
    // Initialize user dir
    absUserDir = System.getProperty("user.dir");
  }
  
  
  /**
   * @return The root directory of this application
   */
  public static String getAbsoluteClientDir()  {
    return absClientDir;  // FIXME: Executed from IDE currently returns "E:"
  }
  
  /**
   * @return Path to the systems user directory
   */
  public static String getAbsoluteUserDir() {
    return absUserDir;    // FIXME: Executed from IDE currently returns "E:\Development\Java Workspace\Deadzone"
  }
  
}