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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Temporary Test class for testing OpenGL rendering with LWJGL.
 */
public class TestForOpenGL {
  private long windowId;
  
  private int shaderProgramId = 0;
  private int vertexShaderId = 0;
  private int fragmentShaderId = 0;
  
  private int vaoId = 0;
  private int vboId = 0;
  
  
  /**
   * Executes the render test - the target is to display a triangle
   */
  public void executeTest() throws Exception {
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
    
    // Render loop
    while ( !glfwWindowShouldClose(windowId) ) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      glfwSwapBuffers(windowId);
      glfwPollEvents();
      render();
      sleep(1000L / 30L);
    }

    // Shutdown
    glfwFreeCallbacks(windowId);
    glfwDestroyWindow(windowId);
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
  
  
  private void render() throws Exception {
    /* Set up the shader program, if not done yet */
    if (shaderProgramId == 0) {
      try {
        shaderProgramId = glCreateProgram();
        if (shaderProgramId == 0) throw new Exception("Could not create shader program");
        
        // Create, compile and attach vertex shader
        String vertexShaderCode =
          "#version 150 core\n" +
          "in vec2 position;\n" +
          "in vec3 color;\n" +
          "out vec3 vertexColor;\n" +
          "void main() { vertexColor = color; gl_Position = vec4(position, 0.0, 1.0); }";
        vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        if (vertexShaderId == 0) throw new RuntimeException("Failed to create vertex shader!");
        glShaderSource(vertexShaderId, vertexShaderCode);
        glCompileShader(vertexShaderId);
        if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling vertex shader!\n" + glGetShaderInfoLog(vertexShaderId, 1024));
        glAttachShader(shaderProgramId, vertexShaderId);
        
        // Create, compile and attach fragment shader
        String fragmentShaderCode =
          "#version 150 core\n\n" +
          "in vec3 vertexColor;\n\n" +
          "out vec4 outColor;\n\n" +
          "void main() { outColor = vec4(vertexColor, 1.0);}";
        fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        if (fragmentShaderId == 0) throw new RuntimeException("Failed to create fragment shader!");
        glShaderSource(fragmentShaderId, fragmentShaderCode);
        glCompileShader(fragmentShaderId);
        if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling fragment shader!\n" + glGetShaderInfoLog(fragmentShaderId, 1024));
        glAttachShader(shaderProgramId, fragmentShaderId);
      } catch (Exception e) {
        System.err.println("Could not initialize shaders.\n" + e.getMessage());
      }
    }
    
    // Link shader program and detach shaders from RAM
    glLinkProgram(shaderProgramId);
    if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == 0) throw new Exception("Error linking shader");
    glDetachShader(shaderProgramId, vertexShaderId);
    glDetachShader(shaderProgramId, fragmentShaderId);
  
    // Validate shaders
    glValidateProgram(shaderProgramId);
    if (glGetProgrami(shaderProgramId, GL_VALIDATE_STATUS) == 0) {
      System.err.println("Failed to validate shaders");
    }
    
    // Prepare and use shader program
    glBindFragDataLocation(shaderProgramId, 0, "outColor");
    glUseProgram(shaderProgramId);
    
    /* VAO */
    vaoId = glGenVertexArrays();
    glBindVertexArray(vaoId);
    
    /* VBO */
    float[] triangleInput = {
      // x     y   R   G   B
      0.5f, 0.9f, 1f, 0f, 0f,  // Point A
      0.1f, 0.1f, 1f, 0f, 0f,  // Point B
      0.9f, 0.1f, 1f, 0f, 0f   // Point C
    };
    MemoryStack stack = MemoryStack.stackPush();
    FloatBuffer vertices = stack.mallocFloat(triangleInput.length);
    for (float val : triangleInput) {
      vertices.put(val);
    }
    vertices.flip();
    
    // Bind VBO
    vboId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    
    // Upload vertices to the GPU
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    MemoryStack.stackPop();
  
    // Specify vertex attributes
    int positionAttribute = glGetAttribLocation(shaderProgramId, "position");
    glEnableVertexAttribArray(positionAttribute);
    glVertexAttribPointer(positionAttribute, 2, GL_FLOAT, false, 0, 0);
    int colorAttribute = glGetAttribLocation(shaderProgramId, "color");
    glEnableVertexAttribArray(colorAttribute);
    glVertexAttribPointer(colorAttribute, 3, GL_FLOAT, false, 0, 2 * Float.BYTES);
    
    /* Draw the triangle */
    glClear(GL_COLOR_BUFFER_BIT);
    glDrawArrays(GL_TRIANGLES, 0, 3);
  
    /* Free resources */
    glDeleteBuffers(vboId);
    glDeleteVertexArrays(vaoId);
    if (vertexShaderId != 0) glDeleteShader(vertexShaderId);
    if (fragmentShaderId != 0) glDeleteShader(fragmentShaderId);
    glUseProgram(0);
    if (shaderProgramId != 0) glDeleteProgram(shaderProgramId);
  }
  
}