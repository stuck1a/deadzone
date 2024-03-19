package deadzone;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Deadzone {
  private GameState gameState = GameState.STOPPED;
  private long window;
  public GameTimer timer;
  public static Deadzone game;
  
  public boolean debugMode = false;

  
  /**
   * Application entrypoint
   * @param args Launcher arguments
   */
  public static void main(String[] args) throws InterruptedException {
    game = new Deadzone();
    // Process arguments
    for (String arg: args) {
      String param = arg.toUpperCase();
      if (Objects.equals( param, "D") || Objects.equals( param, "DEBUG")) {
        game.debugMode = true;
        continue;
      }
    }
    game.run();
  }
  
  public boolean isDebugMode() {
    return debugMode;
  }
  
  
  /**
   * Initialization of the game engine/libraries
   */
  private void init() {
    // Set up the error callback
    GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    glfwSetErrorCallback(errorCallback);
    
    // Initialize GLFW
    if ( !glfwInit() )
      throw new IllegalStateException("Unable to initialize GLFW");
    
    // Set up render context
    createRenderContext(800, 600, "Deadzone", NULL, NULL);
    // Set up initial scene (launcher scene)
    // TODO ...
    
    // Get thread stack and generate initial frame
    try ( MemoryStack stack = stackPush() ) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*
      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);
      // Get primary monitor resolution
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      // Center window
      assert vidmode != null;
      glfwSetWindowPos(
        window,
        (vidmode.width() - pWidth.get(0)) / 2,
        (vidmode.height() - pHeight.get(0)) / 2
      );
    }
    // Use OpenGL context
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);
    // Display application window
    glfwShowWindow(window);
    // Initialize the game timer object
    timer = new GameTimer(30);  // default value  // TODO: use value from stored user config, if any
  }
  
  
  /**
   * Launcher sequence
   */
  public void run() throws InterruptedException {
    this.gameState = GameState.RUNNING;
    this.init();
    this.loop();
    this.shutdown();
  }
  
  
  /**
   * The primary execution loop
   */
  public void loop() throws InterruptedException {
    // Detect OpenGL thread and make bindings available for use
    GL.createCapabilities();
    // Set black as base color of the application window
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    // Run the primary loop until window is closed
    while ( !glfwWindowShouldClose(window) ) {
      // Clear framebuffer
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      // Swap color buffers
      glfwSwapBuffers(window);
      // Check for events like pressed keys and such
      glfwPollEvents();
      // Execute all content related game loop subroutines
      input();
      update();
      render();
      // Update the game timer
      timer.updateTimer();
      // Keep the target frame rate
      sleep(timer.getSleepTime());
    }
  }
  
  
  /**
   * Set up GLFW to use the OpenGL 3.2 core profile for rendering
   */
  private void createRenderContext(int width, int height, String title, long monitor, long share) {
    // Configure the window (launcher scene)
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // hide until init is done
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // not resizable
    // Configure OpenGL 3.2 core profile
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);  // deactivate deprecated features
    // Create application window
    window = glfwCreateWindow(width, height, title, monitor, share);
    // If the GPU doesn't support OpenGL 3.2 try 2.1 as fallback
    if ( window == NULL ) {
      final JPanel panel = new JPanel();
      JOptionPane.showMessageDialog(
        panel,
        "It appears that your graphics card does not support OpenGL 3.1.\nUnfortunately, this means that 'Deadzone' will not run on your system.",
        "Failed to create the GLFW window",
        JOptionPane.ERROR_MESSAGE
      );
      glfwTerminate();
      throw new RuntimeException("Failed to create the GLFW window");
    }
  }
  
  /**
   * Executed when the application will be closed.
   */
  public void shutdown() {
    // Free up all resources and release callbacks
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);
  
    // Terminate GLFW and free the error callback
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
  
  
  /**
   * Subroutine of the game loop which will register all user inputs from the current frame
   */
  public void input() {
  
  }
  
  
  /**
   * Subroutine of the game loop which updates all data for the next frame
   */
  public void update() {

  }
  
  
  /**
   * Subroutine of the game loop which renders the updated data for the next frame
   */
  public void render() {
    if (isDebugMode()) {
      System.out.println("Elapsed Time:" + timer.getCurrentTimestamp());
      System.out.println("Elapsed Seconds:" + timer.getCurrentSecond());
      System.out.println("Current Frame:" + timer.getCurrentFrame());
      System.out.println("Current FPS:" + timer.getFps());
      System.out.println("----------------------------");
    }

    
    
    // DEBUG BLOCK START - RENDER A VERTICE BASED TRIANGLE
    
    // DEBUG BLOCK END
  }
  

  
}