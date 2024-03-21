package deadzone.render;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * This class is performing the rendering process.
 *
 * @author Heiko Brumme
 */
public class Renderer {
 
  private FloatBuffer vertices;
  private int numVertices;
  private boolean drawing;
  
  private FontRenderer font;
  private FontRenderer debugFont;
  
  /** Initializes the renderer. */
  public void init() {
    
    /* Enable blending */
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    /* Create fonts */
    font = new FontRenderer(Fonts.PLAY);
  }
  
  /**
   * Clears the drawing area.
   */
  public void clear() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }
  
  /**
   * Begin rendering.
   */
  public void begin() {
    if (drawing) {
      throw new IllegalStateException("Renderer is already drawing!");
    }
    drawing = true;
    numVertices = 0;
  }
  
  /**
   * End rendering.
   */
  public void end() {
    if (!drawing) {
      throw new IllegalStateException("Renderer isn't drawing!");
    }
    drawing = false;
  }
  

  
  /**
   * Calculates total width of a text.
   *
   * @param text The text
   *
   * @return Total width of the text
   */
  public int getTextWidth(CharSequence text) {
    return font.getWidth(text);
  }
  
  /**
   * Calculates total height of a text.
   *
   * @param text The text
   *
   * @return Total width of the text
   */
  public int getTextHeight(CharSequence text) {
    return font.getHeight(text);
  }
  
  /**
   * Calculates total width of a debug text.
   *
   * @param text The text
   *
   * @return Total width of the text
   */
  public int getDebugTextWidth(CharSequence text) {
    return debugFont.getWidth(text);
  }
  
  /**
   * Calculates total height of a debug text.
   *
   * @param text The text
   *
   * @return Total width of the text
   */
  public int getDebugTextHeight(CharSequence text) {
    return debugFont.getHeight(text);
  }
  
  /**
   * Draw text at the specified position.
   *
   * @param text Text to draw
   * @param x    X coordinate of the text position
   * @param y    Y coordinate of the text position
   */
  public void drawText(CharSequence text, float x, float y) {
    font.writeText(50, 50, "hallo");
  }
  


  
  /**
   * Draws a texture region with the currently bound texture on specified
   * coordinates.
   *
   * @param texture   Used for getting width and height of the texture
   * @param x         X position of the texture
   * @param y         Y position of the texture
   * @param regX      X position of the texture region
   * @param regY      Y position of the texture region
   * @param regWidth  Width of the texture region
   * @param regHeight Height of the texture region
   */
  public void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight) {
    drawTextureRegion(texture, x, y, regX, regY, regWidth, regHeight, Color.WHITE);
  }
  
  /**
   * Draws a texture region with the currently bound texture on specified
   * coordinates.
   *
   * @param texture   Used for getting width and height of the texture
   * @param x         X position of the texture
   * @param y         Y position of the texture
   * @param regX      X position of the texture region
   * @param regY      Y position of the texture region
   * @param regWidth  Width of the texture region
   * @param regHeight Height of the texture region
   * @param c         The color to use
   */
  public void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight, Color c) {
    /* Vertex positions */
    float x1 = x;
    float y1 = y;
    float x2 = x + regWidth;
    float y2 = y + regHeight;
    
    /* Texture coordinates */
    float s1 = regX / texture.getWidth();
    float t1 = regY / texture.getHeight();
    float s2 = (regX + regWidth) / texture.getWidth();
    float t2 = (regY + regHeight) / texture.getHeight();
    
    drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2, c);
  }
  
  /**
   * Draws a texture region with the currently bound texture on specified
   * coordinates.
   *
   * @param x1 Bottom left x position
   * @param y1 Bottom left y position
   * @param x2 Top right x position
   * @param y2 Top right y position
   * @param s1 Bottom left s coordinate
   * @param t1 Bottom left t coordinate
   * @param s2 Top right s coordinate
   * @param t2 Top right t coordinate
   */
  public void drawTextureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2) {
    drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2, Color.WHITE);
  }
  
  /**
   * Draws a texture region with the currently bound texture on specified
   * coordinates.
   *
   * @param x1 Bottom left x position
   * @param y1 Bottom left y position
   * @param x2 Top right x position
   * @param y2 Top right y position
   * @param s1 Bottom left s coordinate
   * @param t1 Bottom left t coordinate
   * @param s2 Top right s coordinate
   * @param t2 Top right t coordinate
   * @param c  The color to use
   */
  public void drawTextureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, Color c) {

    
    float r = c.getRed();
    float g = c.getGreen();
    float b = c.getBlue();
    float a = c.getAlpha();
    
    vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(s1).put(t1);
    vertices.put(x1).put(y2).put(r).put(g).put(b).put(a).put(s1).put(t2);
    vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);
    
    vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(s1).put(t1);
    vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);
    vertices.put(x2).put(y1).put(r).put(g).put(b).put(a).put(s2).put(t1);
    
    numVertices += 6;
  }
  
  /**
   * Dispose renderer and clean up its used data.
   */
  public void dispose() {
    MemoryUtil.memFree(vertices);
  }
  
  
}