package deadzone;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * Temporary Test class - target is to render a red triangle and a green triangle above it with 50% opacity.
 */
public class TestForOpenGL_MultipleVBOs {
  
  public static void main(String[] args) throws InterruptedException {
    // Initialize LWJGL
    GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    glfwSetErrorCallback(errorCallback);
    if ( !glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");
    
    // Create window
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    long windowId = glfwCreateWindow(800, 600, "Render test", NULL, NULL);
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
    
    
    // Initialize shaders
    String vertexShaderCode =
      "#version 150 core\n" +
      "in vec2 position;\n" +
      "in vec4 color;\n" +
      "out vec4 vertexColor;\n" +
      "void main() {\n" +
      "  vertexColor = color;\n" +
      "  gl_Position = vec4(position, 0.0, 1.0);\n" +
      "}";
    int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
    if (vertexShaderId == 0) throw new RuntimeException("Failed to create a new vertex shader!");
    glShaderSource(vertexShaderId, vertexShaderCode);
    glCompileShader(vertexShaderId);
    if (glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling vertex shader!\n" + glGetShaderInfoLog(vertexShaderId, 1024));
    String fragmentShaderCode =
      "#version 150 core\n" +
      "in vec4 vertexColor;\n" +
      "out vec4 outColor;\n" +
      "void main() {\n" +
      "  outColor = vertexColor;\n" +
      "}";
    int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
    if (fragmentShaderId == 0) throw new RuntimeException("Failed to create a new fragment shader!");
    glShaderSource(fragmentShaderId, fragmentShaderCode);
    glCompileShader(fragmentShaderId);
    if (glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == 0) throw new RuntimeException("Error compiling fragment shader!\n" + glGetShaderInfoLog(fragmentShaderId, 1024));
    int shaderProgramId = glCreateProgram();
    if (shaderProgramId == 0) throw new RuntimeException("Failed to create a new shader program!");
    glAttachShader(shaderProgramId, vertexShaderId);
    glAttachShader(shaderProgramId, fragmentShaderId);
    glBindFragDataLocation(shaderProgramId, 0, "outColor");
    glLinkProgram(shaderProgramId);
    if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == 0) throw new RuntimeException("Failed to link shader program!");
    glValidateProgram(shaderProgramId);
    if (glGetProgrami(shaderProgramId, GL_VALIDATE_STATUS) == 0) throw new RuntimeException("Failed to validate shaders!");
    glDetachShader(shaderProgramId, vertexShaderId);
    glDetachShader(shaderProgramId, fragmentShaderId);
    glDeleteShader(vertexShaderId);
    glDeleteShader(fragmentShaderId);
  

    glUseProgram(shaderProgramId);
    
    
    // Render loop
    while ( !glfwWindowShouldClose(windowId) ) {
      glfwPollEvents();
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      
      // Initialize the VAO for the triangles
      int vao_Triangles = glGenVertexArrays();
      glBindVertexArray(vao_Triangles);
  
  
      // Initialize the red triangle
      float[] redTriangle = {
        0.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f,
        -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f,
        0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f
      };
      int vbo_RedTriangle = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vbo_RedTriangle);
      FloatBuffer vertices_RedTriangle = MemoryUtil.memAllocFloat(redTriangle.length);
      vertices_RedTriangle.put(redTriangle).flip();
      glBufferData(GL_ARRAY_BUFFER, vertices_RedTriangle, GL_STATIC_DRAW);
      glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
      glEnableVertexAttribArray(0);
      glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
      glEnableVertexAttribArray(1);
  
      // Draw the red triangle
      glDrawArrays(GL_TRIANGLES, 0, 3);
  
      // Initialize the green triangle
      float[] greenTriangle = {
        0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.5f,
        -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f,
        0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f
      };
      int vbo_GreenTriangle = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vbo_GreenTriangle);
      FloatBuffer vertices_GreenTriangle = MemoryUtil.memAllocFloat(greenTriangle.length);
      vertices_GreenTriangle.put(greenTriangle).flip();
      glBufferData(GL_ARRAY_BUFFER, vertices_GreenTriangle, GL_STATIC_DRAW);
      glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
      glEnableVertexAttribArray(0);
      glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
      glEnableVertexAttribArray(1);
  
  

      
      // Draw the green triangle
      glDrawArrays(GL_TRIANGLES, 0, 3);
      
      
      // Clean up
      glDeleteBuffers(vbo_RedTriangle);
      glDeleteBuffers(vbo_GreenTriangle);
      glDeleteVertexArrays(vao_Triangles);
      
      // Clear graphic buffers
      glfwSwapBuffers(windowId);
      sleep(1000L);
    }
    

    // Dispose
    glUseProgram(0);
    glDeleteShader(vertexShaderId);
    glDeleteShader(fragmentShaderId);
    glDeleteProgram(shaderProgramId);
    glfwFreeCallbacks(windowId);
    glfwDestroyWindow(windowId);
    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
  
}
