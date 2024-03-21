package deadzone.shader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;


/**
 * This class represents a shader
 */
public class Shader {
  /** Stores the shader handle */
  private final int id;
  
  
  /**
   * Creates a new shader with specified type.
   * Currently, only supports vertex (GL_VERTEX_SHADER) and fragment (GL_FRAGMENT_SHADER) shaders.
   * @param type Type of the shader
   */
  public Shader(int type) {
    id = glCreateShader(type);
  }
  
  /**
   * Getter for the shader ID.
   * @return Handle of this shader
   */
  public int getID() {
    return id;
  }
  
  /**
   * Sets the source code of this shader
   * @param source GLSL Source Code for the shader
   */
  public void source(CharSequence source) {
    glShaderSource(id, source);
  }
  
  /**
   * Compiles the shader and checks its status afterwards
   * */
  public void compile() {
    glCompileShader(id);
    checkStatus();
  }
  
  /**
   * Deletes the shader
   */
  public void delete() {
    glDeleteShader(id);
  }
  
  /**
   * Creates a shader with specified type and source and compiles it.
   * Currently, only supports vertex (GL_VERTEX_SHADER) and fragment (GL_FRAGMENT_SHADER) shaders.
   * @param type Type of the shader
   * @param source Source of the shader
   * @return Compiled Shader from the specified source
   */
  public static Shader createShader(int type, CharSequence source) {
    Shader shader = new Shader(type);
    shader.source(source);
    shader.compile();
    return shader;
  }
  
  /**
   * Compiles a shader file
   * @param type Type of the shader
   * @param path File path of the shader
   * @return Compiled Shader from given file
   */
  public static Shader loadShader(int type, String path) {
    StringBuilder builder = new StringBuilder();
    try (InputStream in = new FileInputStream(path);
         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line).append("\n");
      }
    } catch (IOException ex) {
      throw new RuntimeException("Failed to load a shader file!" + System.getProperty("file.separator") + ex.getMessage());
    }
    CharSequence source = builder.toString();
    return createShader(type, source);
  }
  
  
  /**
   * Validates shader compilation
   */
  private void checkStatus() {
    int status = glGetShaderi(id, GL_COMPILE_STATUS);
    if (status != GL_TRUE) {
      throw new RuntimeException(glGetShaderInfoLog(id));
    }
  }
  
}