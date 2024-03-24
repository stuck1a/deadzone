package deadzone.graphics;

import deadzone.Deadzone;
import deadzone.Util;

import static org.lwjgl.opengl.GL20.*;


/**
 * Class for loading, compiling and managing GLSL shader programs.
 * Each instance represents a concrete shader.
 */
public class ShaderProgram {
  
  private final int programId;
  
  private int vertexShaderId;
  
  private int fragmentShaderId;
  
  
  
  /**
   * Creates a new empty OpenGL program
   */
  public ShaderProgram() throws Exception {
    programId = glCreateProgram();
    if (programId == 0) {
      throw new Exception("Could not create Shader");
    }
  }
  
  
  /**
   * Returns the program handle which points to the shader program in the GPU memory
   */
  public int getProgramId() {
    return programId;
  }
  
  /**
   * Returns the vertex shader handle which points to the vertex shader in the GPU memory
   */
  public int getVertexShaderId() {
    return vertexShaderId;
  }
  
  /**
   * Returns the fragment shader handle which points to the vertex shader in the GPU memory
   */
  public int getFragmentShaderId() {
    return fragmentShaderId;
  }
  
  
  
  /**
   * Activates this program for rendering
   * by adding it to the rendering pipeline
   */
  public void bind() {
    glUseProgram(programId);
  }
  
  /**
   * Deactivates this program for rendering
   * by removing it from the rendering pipeline
   */
  public void unbind() {
    glUseProgram(0);
  }
  
  /**
   * Removes all resources this program uses
   * when they are no longer needed
   */
  public void cleanup() {
    unbind();
    if (programId != 0) {
      glDeleteProgram(programId);
    }
  }
  
  /**
   * Moves all compiled shaders from RAM to GPU
   */
  public void link() throws Exception {
    // Compile and attach shader to the GPU
    glLinkProgram(programId);
    if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
      throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
    }
    
    // Remove shaders from RAM, because we have them now in the GPU
    if (vertexShaderId != 0) {
      glDetachShader(programId, vertexShaderId);
    }
    if (fragmentShaderId != 0) {
      glDetachShader(programId, fragmentShaderId);
    }
    
    // Validate the attached shaders to make sure everything works fine (Debug mode only)
    if (!validateShaders()) {
      System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
    }

  }
  
  
  /**
   * Set up the vertex and fragment shader
   */
  public void initializeBaseShaders() throws Exception {
    String shadersDir = Util.getShadersDir();
    
    String vertexShaderCode = Util.readFullFile(shadersDir + "vertex.glsl");
    vertexShaderId = compileShader(vertexShaderCode, GL_VERTEX_SHADER);
  
    String fragmentShaderCode = Util.readFullFile(shadersDir + "fragment.glsl");
    fragmentShaderId = compileShader(fragmentShaderCode, GL_FRAGMENT_SHADER);
    
    link();
  }
  

  /**
   * Compiles a shader program
   */
  public int compileShader(String shaderCode, int shaderType) throws Exception {
    int shaderId = glCreateShader(shaderType);
    if (shaderId == 0) {
      throw new RuntimeException("Error creating shader. Type: " + shaderType);
    }
    glShaderSource(shaderId, shaderCode);
    glCompileShader(shaderId);
    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
      throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
    }
    glAttachShader(programId, shaderId);
    return shaderId;
  }
  
  
  /**
   * Make sure all shaders are properly linked to the GPU.
   * Will be executed only if we are in debug mode.
   * Note, that this validates against the current OpenGL state and therefore
   * might fail even if the shaders are correct (i.e. if some data not updated yet).
   * So don't throw an error if validation fails, just log it.
   */
  protected boolean validateShaders() {
    if (!Deadzone.getApplication().isDebugMode()) {
      return true;
    }
    if (programId == 0) {
      return false;
    }
    glValidateProgram(programId);
    return glGetProgrami(programId, GL_VALIDATE_STATUS) != 0;
  }
  
}