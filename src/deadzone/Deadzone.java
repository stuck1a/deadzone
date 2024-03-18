package deadzone;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Deadzone {
  
  private int width = 1280;
  private int height = 720;
  private String title = "Deadzone";
  
  private GameState gameState = GameState.STOPPED;
  
  private long window;
  public static Deadzone game;
  
  
  /**
   * Application entrypoint
   * @param args Launcher arguments
   */
  public static void main(String[] args) {
    game = new Deadzone();
    game.run();
  }
  
  
  /**
   * Initialization of the game engine/libraries
   */
  private void init() {
    // Setup error callback
    GLFWErrorCallback.createPrint(System.err).set();
    // Initialize GLFW
    if ( !glfwInit() )
      throw new IllegalStateException("Unable to initialize GLFW");
    // Init application window
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // hide until init is done
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // not resizable
    // Create application window
    window = glfwCreateWindow(width, height, title, NULL, NULL);
    if ( window == NULL )
      throw new RuntimeException("Failed to create the GLFW window");
    // Set up callback for pressed keys
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    });
    // Get thread stack and push initial frame
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
  }
  
  
  /**
   * Launcher sequence
   */
  public void run() {
    gameState = GameState.RUNNING;
    
    init();
    loop();
  
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);
  
    // Terminate GLFW and free the error callback
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
  
  
  /**
   * The primary execution loop
   */
  public void loop() {
    // Detect OpenGL thread and make bindings available for use
    GL.createCapabilities();
    // Set black as base color of the application window
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    // Run the primary loop until user window is closed or ESC was pressed
    while ( !glfwWindowShouldClose(window) ) {
      // Clear framebuffer
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      // Swap color buffers
      glfwSwapBuffers(window);
      // Check for events like pressed keys and such
      glfwPollEvents();
      // Execute the game loop subroutines
      update();
      render();
    }
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
  
  }
  
}