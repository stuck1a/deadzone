package deadzone.rendering;

import deadzone.rendering.ShaderProgram;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaders;
  
  /** Stores all handles of objects which are currently stored in the GPU memory for rendering */
  int[] registeredObjects;
  
  
  public Renderer() {
  
  }
  
  public ShaderProgram getShaders() {
    return shaders;
  }
  
  public void init() {
    if (shaders == null) {
      try {
        shaders = new ShaderProgram();
        shaders.initializeBaseShaders();
        shaders.bind();
      } catch (Exception e) {
        System.err.println("Could not initialize shaders.\n" + e.getMessage());
        return;
      }
    }
  }
  
  
  public void renderRegisteredObjects() {
    // Render a triangle for testing. Later we will iterate through all registered objects here and only reinitialize them if they have changed
    drawTriangle();
  }
  
  
  /**
   * Renders a predefined triangle for testing purposes
   */
  private void drawTriangle() {
    // Initialize the triangle
    VertexBufferObject vboTriangle = new VertexBufferObject(
      new float[]{
      //  x      y    z   r   g   b
        -0.6f, -0.4f, 0f, 1f, 0f, 0f,
         0.6f, -0.4f, 0f, 0f, 1f, 0f,
         0.0f,  0.6f, 0f, 0f, 0f, 1f
      }
    );
    
    // Register the triangle, so we can clean up its graphic data on shutdown (we need at least the VAO and VBO handles)
    
    
  }
  
  
  /**
   * Free up all resources from GPU memory
   */
  public void cleanup() {
  
    // Iterate through all registered objects and remove their graphic data from the GPU
    //glDeleteVertexArrays(vao);
    //glDeleteBuffers(vbo);
    
    // Remove the shaders from the GPU memory
    int vertexShader = shaders.getVertexShaderId();
    int fragmentShader = shaders.getFragmentShaderId();
    int shaderProgram = shaders.getProgramId();
    
    if (vertexShader != 0) {
      glDeleteShader(vertexShader);
    }
    if (fragmentShader != 0) {
      glDeleteShader(fragmentShader);
    }
    if (shaderProgram != 0) {
      glDeleteProgram(shaderProgram);
    }
  }
  
}