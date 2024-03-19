package deadzone.scenes;

import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;



/**
 * This is the first rendered scene when the game is launched.
 * Here the user can logn/register his account, do some basic options
 * and start the initial in-game scene (Compound)
 */
public class Launcher extends AbstractScene {
  
  private static final Scene name = Scene.LAUNCHER;
  
  
  public Launcher() {
    super(name);
  }
  
  
  public void renderScene() {
    testRenderTriangle();
  }
  
  
  private void testRenderTriangle() {

  }
  
  
}