package deadzone;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;

/**
 * This class is responsible for creating and managing the game window with help of LWJGL
 */
public class Window {

  /** Stores the window handle */
  private static long handle;
  
  
  public Window(int width, int height, String title, long monitor, long share) {
    // Configure the window (launcher scene)
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // hide until init is done
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // not resizable
    // Configure OpenGL 3.2 core profile
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, Settings.majorVersionOpenGL);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, Settings.minorVersionOpenGL);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);  // deactivate deprecated features
    // Create application window
    handle = glfwCreateWindow(width, height, title, monitor, share);
  }
  
  public long getHandle() {
    return handle;
  }
  
}