package deadzone.rendering;

import static org.lwjgl.opengl.GL20.*;


/**
 * Class for loading, compiling and managing GLSL shader programs.
 * Each instance represents a concrete shader.
 *
 * Initialization steps:
 * (1) Create an empty OpenGL program (constructor)
 * (2) Compile all necessary shaders (createXYShader)
 * (3) Load compiled shaders into graphic card (link)
 * (4) Remove compiled shaders from memory (link)
 * (5) Render stuff
 */
public class ShaderProgram {
  
  private final int programId;
  
  private int vertexShaderId;
  
  private int fragmentShaderId;
  
  
  // Compile all basic shaders which are required from the beginning
  static {
    compileBaseShaders();
  }
  
  
  /**
   * Creates a new empty OpenGL program
   */
  public ShaderProgram() throws Exception {
    programId = glCreateProgram();
    if (programId == 0) {
      throw new Exception("Could not create Shader");
    }
  }
  
  public void bind() {
    glUseProgram(programId);
  }
  
  public void unbind() {
    glUseProgram(0);
  }
  
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
    // Validate the attached shaders to make sure everything works fine
    glValidateProgram(programId);
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
      System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
    }
  }
  
  
  /**
   * Compiles all basic shader programs which are needed from the very beginning
   */
  public void compileBaseShaders(String shaderCode) throws Exception {
    vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
  }
  

  
  /**
   * Compiles a shader program
   */
  protected int createShader(String shaderCode, int shaderType) throws Exception {
    int shaderId = glCreateShader(shaderType);
    if (shaderId == 0) {
      throw new Exception("Error creating shader. Type: " + shaderType);
    }
    glShaderSource(shaderId, shaderCode);
    glCompileShader(shaderId);
    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
      throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
    }
    glAttachShader(programId, shaderId);
    return shaderId;
  }
}