package deadzone.graphics;

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
    // Render a red triangle for testing
    drawTriangle(new float[] {
       0.0f,  0.5f, 1.0f, 0.0f, 0.0f, 0.5f,
      -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f,
       0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f
    });
    
    // Render a green triangle a bit above the red one for testing
//    drawTriangle(new float[] {
//       0.0f,  1.0f, 0.0f, 1.0f, 0.0f, 0.5f,
//      -0.5f,  0.0f, 0.0f, 1.0f, 0.0f, 0.5f,
//       0.5f,  0.0f, 0.0f, 1.0f, 0.0f, 0.5f
//    });
  }
  
  
  /**
   * Renders a predefined triangle for testing purposes
   */
  private void drawTriangle(float[] input) {
    // Create the VAO
    VertexArrayObject vaoTriangle = new VertexArrayObject();
    vaoTriangle.bind();

   // Create the VBO
    VertexBufferObject vboTriangle = new VertexBufferObject(input);
    vboTriangle.initialize();
    
    // Register all objects related to the triangle
    registeredVboObjects.add(vboTriangle);
    registeredVaoObjects.add(vaoTriangle);
    
    // Draw the Triangle
    vboTriangle.render();
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