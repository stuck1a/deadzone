package deadzone;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * Temporary Test class for testing OpenGL rendering with LWJGL.
 */
public class TestForOpenGL_1 {
  private long windowId;
  
  private int shaderProgramId = 0;
  private int vertexShaderId = 0;
  private int fragmentShaderId = 0;
  
  private int vaoId = 0;
  private int vboId = 0;
  
  

  public void executeTest() throws InterruptedException {
    // Create render context
    createWindow();
    
    // Create shader program
    generateShaderProgram();
    
    // Render loop
    while ( !glfwWindowShouldClose(windowId) ) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      glUseProgram(shaderProgramId);
      render();
      glfwPollEvents();
      glfwSwapBuffers(windowId);
      sleep(1000L / 30L);
    }
  
    dispose();
  }
  
  
  

  
  
  private void generateShaderProgram() {
    // Create and compile the vertex shader
    String vertexShaderCode =
      "#version 150 core\n" +
      "in vec2 position;\n" +
      "in vec3 color;\n" +
      "out vec3 Color;\n" +
      "void main() {\n" +
      "  Color = color;\n" +
      "  gl_Position = vec4(position, 0.0, 1.0);\n" +
      "}";
    vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
    if (vertexShaderId == 0) throw new RuntimeException("Failed to create a new vertex shader!");
    glShaderSource(vertexShaderId, vertexShaderCode);
    glCompileShader(vertexShaderId);
    if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling vertex shader!\n" + glGetShaderInfoLog(vertexShaderId, 1024));
    
    // Create and compile the fragment shader
    String fragmentShaderCode =
      "#version 150 core\n" +
      "in vec3 Color;\n" +
      "out vec4 outColor;\n" +
      "void main() {\n" +
      "  outColor = vec4(Color, 1.0);\n" +
      "}";
    fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
    if (fragmentShaderId == 0) throw new RuntimeException("Failed to create a new fragment shader!");
    glShaderSource(fragmentShaderId, fragmentShaderCode);
    glCompileShader(fragmentShaderId);
    if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling fragment shader!\n" + glGetShaderInfoLog(fragmentShaderId, 1024));
    
    // Create a new shader program
    shaderProgramId = glCreateProgram();
    if (shaderProgramId == 0) throw new RuntimeException("Failed to create a new shader program!");
    
    // Attach the compiled shaders to the shader program
    glAttachShader(shaderProgramId, vertexShaderId);
    glAttachShader(shaderProgramId, fragmentShaderId);
    
    // Define the out variable "outColor" of the fragment shader as the RGB color value which shall be used for rendering
    glBindFragDataLocation(shaderProgramId, 0, "outColor");
    
    // Link the shader program itself
    glLinkProgram(shaderProgramId);
    if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == 0) throw new RuntimeException("Failed to link shader program!");
  
    // Validate the shader program
    glValidateProgram(shaderProgramId);
    if (glGetProgrami(shaderProgramId, GL_VALIDATE_STATUS) == 0) throw new RuntimeException("Failed to validate shaders!");
    
    // Remove the shaders because we have them now in the executable shader program itself
    glDetachShader(shaderProgramId, vertexShaderId);
    glDetachShader(shaderProgramId, fragmentShaderId);
    glDeleteShader(vertexShaderId);
    glDeleteShader(fragmentShaderId);
  }
  
  
  
  private void render() throws RuntimeException {
    // Create and bind a new VAO
    vaoId = glGenVertexArrays();
    glBindVertexArray(vaoId);
  
    // Specify the input attributes for the shader program
    int positionAttribute = glGetAttribLocation(shaderProgramId, "position");
    glEnableVertexAttribArray(positionAttribute);
    glVertexAttribPointer(positionAttribute, 2, GL_FLOAT, false, 0, 0);
    int colorAttribute = glGetAttribLocation(shaderProgramId, "color");
    glEnableVertexAttribArray(colorAttribute);
    glVertexAttribPointer(colorAttribute, 3, GL_FLOAT, false, 0, 2 * Float.BYTES);
    
    // Create a new VBO
    float[] triangleInput = {
       0.0f,  0.5f, 1.0f, 0.0f, 0.0f,
       0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
      -0.5f, -0.5f, 0.0f, 0.0f, 1.0f
    };
    
    // Bind the VBO
    vboId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    
    // Add a new stack frame and allocate the required amount of GPU memory for it
    MemoryStack stack = MemoryStack.stackPush();
    FloatBuffer vertices = stack.mallocFloat(triangleInput.length);
  
    // Put the triangle data into the new stack frame and prepare the buffer for reading
    for (float val : triangleInput) { vertices.put(val); }
    vertices.flip();
    
    // Upload the triangle vertices to the GPU, remove our stack frame and restore the original stack pointer
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    MemoryStack.stackPop();
    
    // Draw the triangle
    glDrawArrays(GL_TRIANGLES, 0, 3);   // THIS ONE FAILS
    
    // Free resources
    glDeleteBuffers(vboId);
    glDeleteVertexArrays(vaoId);

  }
  
  
  private void dispose() {
    // Dispose render data
    glUseProgram(0);
    if (vertexShaderId != 0) glDeleteShader(vertexShaderId);
    if (fragmentShaderId != 0) glDeleteShader(fragmentShaderId);
    if (shaderProgramId != 0) glDeleteProgram(shaderProgramId);
  
    // Dispose window data
    glfwFreeCallbacks(windowId);
    glfwDestroyWindow(windowId);
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
  
  
  
  private void createWindow() {
    // Initialize LWJGL
    GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    glfwSetErrorCallback(errorCallback);
    if ( !glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");
    
    // Create render context (the application window)
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    windowId = glfwCreateWindow(800, 600, "Render test", NULL, NULL);
    if ( windowId == NULL ) {
      glfwTerminate();
      throw new RuntimeException("Failed to create the GLFW window");
    }
    try ( MemoryStack stack = stackPush() ) {
      IntBuffer pWidth = stack.mallocInt(1);
      IntBuffer pHeight = stack.mallocInt(1);
      glfwGetWindowSize(windowId, pWidth, pHeight);
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      assert vidmode != null;
      glfwSetWindowPos(windowId, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
    }
    glfwMakeContextCurrent(windowId);
    glfwSwapInterval(1);
    glfwShowWindow(windowId);
    GL.createCapabilities();
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
  }
  
  
}