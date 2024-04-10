package deadzone;

/**
 * Class which is responsible for managing, loading and storing internal and user settings
 */
public class Settings {
  
  public final static String windowTitle = "Deadzone";
  
  public final static int majorVersionOpenGL = 3;
  
  public final static int minorVersionOpenGL = 2;
  
  public static int launcherWidth = 800;
  
  public static int launcherHeight = 600;
  
  public static boolean vSync = false;
  
  public static float[] baseColorRGBA = { 0.0f, 0.0f, 0.0f, 0.0f };
  
  public static int targetFPS = -1;  // If v-sync is enabled, the FPS is limited to the frame rate of the monitor (-1 = unlimited)
  
}
