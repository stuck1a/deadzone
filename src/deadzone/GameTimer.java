package deadzone;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * Class to calculate and control the time flow and FPS
 */
public class GameTimer {
  
  /** Timestamp created when the game was started */
  private final double initialTimestamp;
  /** The target FPS defined by user settings */
  private int targetFps;
  /** The current real FPS value - may vary from target FPS if CPU is overloaded */
  private int fps;
  /** The amount of rendered frames since game start. Also, the number of the current frame */
  private int currentFrame;
  /** The timestamp for the last frame */
  private double currentTimestamp;
  /** The amount of elapsed seconds since game start */
  private int currentSecond;
  /** (readonly) Amount of milliseconds we have to wait after each game loop to reach the defined target FPS */
  private long sleepTime;
  
  /** Internal field used to determine, how much time has passend since the last frame */
  private double lastLoopTime;
  
  
  public GameTimer() {
    this.lastLoopTime = this.initialTimestamp = this.createTimestamp();
    this.currentSecond = 0;
    this.currentFrame = 1;
    this.targetFps = 30;  // default value - later use value from stored user config, if any
  }
  
  public double getInitialTimestamp() {
    return initialTimestamp;
  }
  
  public int getCurrentFrame() {
    return this.currentFrame;
  }
  
  public int getCurrentSecond() {
    return this.currentSecond;
  }
  
  public int getTargetFps() {
    return this.targetFps;
  }
  
  public int getFps() {
    return this.fps;
  }
  
  public double getCurrentTimestamp() {
    return this.currentTimestamp;
  }
  
  /**
   * @return Milliseconds we have to wait after each game loop to reach the target FPS
   */
  public long getSleepTime() {
    return sleepTime;
  }
  
  public void setTargetFps(int targetFps) {
    this.targetFps = targetFps;
  }
  
  
  /**
   * Called at the end of each game loop to recalculate all times
   */
  protected void updateTimer() {
    this.currentTimestamp = createTimestamp();
    this.currentFrame++;
  }
  
  
  /**
   * @return Current timestamp in seconds (from system time)
   */
  private double createTimestamp() {
    return glfwGetTime();
  }
  
}


//  public static void updateFPS() {
//    currentFrame++;
//  }

//  public static void updateGameTime() {
//    timeCount = getDelta();
//    if (timeCount > 1f) {
//      GameTimer.currentSecond = GameTimer.currentSecond++;
//      timeCount -= 1f;
//    }
//  }

//  public static float getDelta() {
//    double time = getTime();
//    float delta = (float) (time - lastLoopTime);
//    lastLoopTime = time;
//    timeCount += delta;
//    return delta;
//  }