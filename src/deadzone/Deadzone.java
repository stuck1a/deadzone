package deadzone;

import deadzone.assets.AssetManager;
import deadzone.graphics.Renderer;
import deadzone.scenes.AbstractScene;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.swing.*;
import java.nio.IntBuffer;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Deadzone {
  private GameState gameState = GameState.STOPPED;
  private Window window;
  private GameTimer timer;
  private Renderer renderer;
  private static Deadzone app;
  private AssetManager assets;
  private boolean debugMode = false;
  
  
  public Window getWindow() {
    return window;
  }
  
  public GameTimer getTimer() {
    return timer;
  }
  
  public static Deadzone getApplication() {
    return app;
  }
  
  public AssetManager getAssetManager() {
    return assets;
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
    final long windowHandle = window.getHandle();
    // Run the primary loop until window is closed
    while ( !glfwWindowShouldClose(windowHandle) ) {
      // Update the game timer
      timer.updateTimer();
      // Check for events like pressed keys and such
      glfwPollEvents();
      // Execute all content related game loop subroutines
      input();
      update();
      // Clear framebuffer to prepare the upcoming render loop
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      render();
      // Swap color buffers
      glfwSwapBuffers(windowHandle);
      // Keep the target frame rate
      if (Settings.targetFPS != -1) {
        sleep(timer.getSleepTime());
      }
    }
  }
  
  /**
   * Executed when the application will or shall be closed.
   */
  public void shutdown() {
    long windowHandle = window.getHandle();
  
    // Remove all graphic data from GPU
    renderer.dispose();
    
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
    AbstractScene.getActiveScene().updateScene();
  }
  
  /**
   * Subroutine of the game loop which renders the updated data for the next frame
   */
  public void render() {
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
    // Load all available assets into GPU memory
    loadAssets();

    
    // Detect OpenGL thread and make bindings available for use
    GL.createCapabilities();
    // Configure OpenGL
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    // Set the base color of the application window
    float[] baseColor = Settings.baseColorRGBA;
    glClearColor(baseColor[0], baseColor[1], baseColor[2], baseColor[3]);
    // Set up the renderer
    renderer = new Renderer();
    // Initialize timer
    timer = new GameTimer(Settings.targetFPS);
  
    // Prepare initial scene
  
    // FOR TESTING: Set Compound as initial scene for iso testing
    //    deadzone.scenes.AbstractScene.setActiveScene(deadzone.scenes.Scene.LAUNCHER);
    //    new deadzone.scenes.Launcher();
    deadzone.scenes.AbstractScene.setActiveScene(deadzone.scenes.Scene.COMPOUND);
    new deadzone.scenes.Compound();
    // TEST BLOCK END
  }
  
  
  /**
   * Creates the initial window und configures OpenGL
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
  
  
  private void loadAssets() {
    // Tell STB to flip textures vertically, so that their origin is at bottom left instead of top left
    stbi_set_flip_vertically_on_load(false);
    
    assets = new AssetManager();
    assets.loadAllAssets();
  }
  
  
  
  
  
  
  
  
}
