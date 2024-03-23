package deadzone;

import deadzone.rendering.Renderer;
import deadzone.scenes.AbstractScene;
import deadzone.scenes.Launcher;
import deadzone.scenes.Scene;
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
  private Window window;
  private GameTimer timer;
  private Renderer renderer;
  private static Deadzone app;
  private boolean debugMode = false;
  
  
  public Window getWindowHandle() {
    return window;
  }
  
  public GameTimer getTimer() {
    return timer;
  }
  
  public static Deadzone getApplication() {
    return app;
  }
  
  public Renderer getRenderer() {
    return renderer;
  }
  
  public GameState getGameState() {
    return gameState;
  }
  
  public boolean isDebugMode() {
    return debugMode;
  }
  
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }
  
  /**
   * Application entrypoint
   * @param args Launcher arguments
   */
  public static void main(String[] args) throws InterruptedException {
    app = new Deadzone();
    // Process arguments
    for (String arg: args) {
      String param = arg.toUpperCase();
      if (Objects.equals( param, "D") || Objects.equals( param, "DEBUG")) {
        app.debugMode = true;
        continue;
      }
    }
    app.run();
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
    long windowHandle = window.getHandle();
    // Run the primary loop until window is closed
    while ( !glfwWindowShouldClose(windowHandle) ) {
      // Clear framebuffer
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      // Swap color buffers
      glfwSwapBuffers(windowHandle);
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
   * Executed when the application will or shall be closed.
   */
  public void shutdown() {
    long windowHandle = window.getHandle();
  
    // Remove all graphic data from GPU
    renderer.cleanup();
    
    // Free up all resources and release callbacks
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(windowHandle);
    glfwDestroyWindow(windowHandle);
    
    // Terminate GLFW and free the error callback
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
  
  /**
   * Subroutine of the game loop which will register all user inputs from the current frame
   */
  public void input() {
    AbstractScene.getActiveScene().processInputs();
  }
  
  /**
   * Subroutine of the game loop which updates all data for the next frame
   */
  public void update() {
    AbstractScene.getActiveScene().updateRegisteredObjects();
  }
  
  /**
   * Subroutine of the game loop which renders the updated data for the next frame
   */
  public void render() {
    // Write some additional debug information to the console if in debug mode
    if (isDebugMode()) {
      System.out.println("Elapsed Time:" + timer.getCurrentTimestamp());
      System.out.println("Elapsed Seconds:" + timer.getCurrentSecond());
      System.out.println("Current Frame:" + timer.getCurrentFrame());
      System.out.println("Current FPS:" + timer.getFps());
      System.out.println("----------------------------");
    }

    // Render the current frame
    renderer.renderRegisteredObjects();
  }
  
  
  /**
   * Initialization of the game engine/libraries
   */
  private void init() {
    // Set up the error callback
    GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    glfwSetErrorCallback(errorCallback);
    
    // Initialize GLFW
    if ( !glfwInit() ) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    
    // Create render context (also creates the window)
    createRenderContext();
    long windowHandle = window.getHandle();
    
    // Get thread stack and generate initial frame
    try ( MemoryStack stack = stackPush() ) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*
      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(windowHandle, pWidth, pHeight);
      // Get primary monitor resolution
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      // Center window
      assert vidmode != null;
      glfwSetWindowPos(
        windowHandle,
        (vidmode.width() - pWidth.get(0)) / 2,
        (vidmode.height() - pHeight.get(0)) / 2
      );
    }
    // Use OpenGL context
    glfwMakeContextCurrent(windowHandle);
    // Enable v-sync
    glfwSwapInterval(Settings.vSync ? 1 : 0);
    // Display application window
    glfwShowWindow(windowHandle);
    // Initialize timer and initial scene
    AbstractScene.setActiveScene(Scene.LAUNCHER);
    timer = new GameTimer(Settings.targetFPS);
    Launcher launcherScene = new Launcher();
    // Detect OpenGL thread and make bindings available for use
    GL.createCapabilities();
    // Set the base color of the application window
    float[] baseColor = Settings.baseColorRGBA;
    glClearColor(baseColor[0], baseColor[1], baseColor[2], baseColor[3]);
    // Set up the renderer
    renderer = new Renderer();
  }
  
  
  /**
   * Set up GLFW to use the desired OpenGL version with core profile for rendering
   */
  private void createRenderContext() {
    window = new Window(Settings.launcherWidth, Settings.launcherHeight, Settings.windowTitle, NULL, NULL);
    // Exit if the users graphic cards doesn't support the required OpenGL version
    if ( window.getHandle() == NULL ) {
      String msg = "Your graphics card does not support OpenGL "
                 + Settings.majorVersionOpenGL + "." + Settings.minorVersionOpenGL + ".\n"
                 + "Unfortunately, this means that '" + Settings.windowTitle + "' will not run on your system.\n"
                 + "You can try to update your graphic card drivers to solve this, but there is no guarantee.";
      final JPanel panel = new JPanel();
      JOptionPane.showMessageDialog(panel, msg, "Failed to create the GLFW window", JOptionPane.ERROR_MESSAGE);
      glfwTerminate();
      throw new RuntimeException("Failed to create the GLFW window");
    }
  }
  
}