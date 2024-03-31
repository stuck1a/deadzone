package deadzone;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;


/**
 * This class is responsible for creating and managing the game window with help of LWJGL
 */
public class Window {
  private static long handle;
  private int height;
  private int width;
  private String title;
  private long monitor;
  
  
  public Window(int width, int height, String title, long monitor, long share) {
    // Fill class fields
    this.width = width;
    this.height = height;
    this.title = title;
    this.monitor = monitor;
    // Configure the window for the launcher scene
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // hide until init is done
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // not resizable
    // Configure OpenGL
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, Settings.majorVersionOpenGL);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, Settings.minorVersionOpenGL);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);  // deactivate deprecated features
    // Create application window
    handle = glfwCreateWindow(width, height, title, monitor, share);
    
    // Register the callback function which is called when the window gets resized
    GLFWWindowSizeCallback windowSizeCallback;
    
    glfwSetWindowSizeCallback(handle, windowSizeCallback = new GLFWWindowSizeCallback(){
      @Override
      public void invoke(long window, int width, int height) {
        Deadzone.getApplication().getWindow().onResize(width, height);
      }
    });
  }
  
  public long getHandle() {
    return handle;
  }
  
  public int getPixelHeight() {
    return height;
  }
  
  public int getPixelWidth() {
    return width;
  }
  
  public String getTitle() {
    return title;
  }
  
  public long getMonitorHandle() {
    return monitor;
  }
  
  /**
   * Callback handler
   * Called when the user resizes or maximizes the current window
   */
  private void onResize(int newWidth, int newHeight) {
    this.width = newWidth;
    this.height = newHeight;
    glViewport(0, 0, newWidth, newHeight);
  }

  
}
