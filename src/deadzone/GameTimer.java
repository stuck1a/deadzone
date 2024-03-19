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
  
  
  public GameTimer() {
    currentTimestamp = initialTimestamp = createTimestamp();
    currentSecond = 0;
    currentFrame = 1;
    targetFps = 30;  // default value - later use value from stored user config, if any
    sleepTime = 1000L / (long) (targetFps + 1);  // must be recalculated whenever the user changes the target fps
  }
  
  public double getInitialTimestamp() {
    return initialTimestamp;
  }
  
  public int getCurrentFrame() {
    return currentFrame;
  }
  
  public int getCurrentSecond() {
    return currentSecond;
  }
  
  public int getTargetFps() {
    return targetFps;
  }
  
  public int getFps() {
    return fps;
  }
  
  public double getCurrentTimestamp() {
    return currentTimestamp;
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
   * Called at the end of each game loop to recalculate all timer values
   */
  protected void updateTimer() {
    double newTimestamp = createTimestamp();
    currentSecond = (int) newTimestamp;
    fps = (int) Math.round(currentFrame / currentTimestamp);
    currentFrame++;
    currentTimestamp = newTimestamp;
  }
  
  
  /**
   * @return Difference between two timestamps
   */
  private double getTimeDiff(double timestamp1, double timestamp2) {
    return Math.abs(timestamp1 - timestamp2);
  }
  
  /**
   * @return Current timestamp in seconds (from system time)
   */
  private double createTimestamp() {
    return glfwGetTime();
  }
  
}