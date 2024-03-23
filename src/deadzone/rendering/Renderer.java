package deadzone.rendering;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaderProgram;
  
  /** Stores all handles of VBO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexBufferObject> registeredVboObjects = new ArrayList<>();
  
  /** Stores all handles of VAO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexArrayObject> registeredVaoObjects = new ArrayList<>();
  
  public Renderer() {
    init();
  }
  
  public ShaderProgram getShaderProgram() {
    return shaderProgram;
  }
  
  public void init() {
    if (shaderProgram == null) {
      try {
        shaderProgram = new ShaderProgram();
        shaderProgram.initializeBaseShaders();
        glBindFragDataLocation(shaderProgram.getProgramId(), 0, "outColor");
        shaderProgram.bind();
      } catch (Exception e) {
        System.err.println("Could not initialize shaders.\n" + e.getMessage());
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
    // Create the VAO
    VertexArrayObject vaoTriangle1 = new VertexArrayObject();
    vaoTriangle1.bind();

   // Create the VBO
    VertexBufferObject vboTriangle1 = new VertexBufferObject(new float[]{
     //  x     y   R   G   B
      0.5f, 0.9f, 1f, 0f, 0f,  // Point A
      0.1f, 0.1f, 1f, 0f, 0f,  // Point B
      0.9f, 0.1f, 1f, 0f, 0f   // Point C
    });
    vboTriangle1.initialize();
    
    // Register all objects related to the triangle
    registeredVboObjects.add(vboTriangle1);
    registeredVaoObjects.add(vaoTriangle1);
    
    // Draw the Triangle
    vboTriangle1.render();
  }
  
  
  /**
   * Free up all resources from GPU memory
   */
  public void cleanup() {
  
    // Iterate through all registered objects and remove their graphic data from the GPU
    for (VertexBufferObject vbo : registeredVboObjects) {
      vbo.delete();
    }
    
    for (VertexArrayObject vao : registeredVaoObjects) {
      vao.delete();
    }
    
    // Remove the shaders from the GPU memory
    int vertexShader = shaderProgram.getVertexShaderId();
    int fragmentShader = shaderProgram.getFragmentShaderId();

    if (vertexShader != 0) {
      glDeleteShader(vertexShader);
    }
    if (fragmentShader != 0) {
      glDeleteShader(fragmentShader);
    }
    this.shaderProgram.cleanup();
  }
  
}