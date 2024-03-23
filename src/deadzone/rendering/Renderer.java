package deadzone.rendering;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaders;
  
  /** Stores all handles of VBO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexBufferObject> registeredVboObjects = new ArrayList<>();
  
  /** Stores all handles of VAO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexArrayObject> registeredVaoObjects = new ArrayList<>();
  
  public Renderer() {
    init();
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
    // Create the VAO
    VertexArrayObject vaoTriangle1 = new VertexArrayObject();
    vaoTriangle1.initialize();

   // Create the VBO
    VertexBufferObject vboTriangle1 = new VertexBufferObject(
      new float[]{
      //   x     y     z   R   G   B
        0.5f, 1.0f, 0.0f, 1f, 0f, 0f,  // A
        0.0f, 0.0f, 0.0f, 0f, 1f, 0f,  // B
        1.0f, 0.0f, 0.0f, 0f, 0f, 1f   // C
      }
    );
    vboTriangle1.initialize();
    
    // TODO: Bind the VBO to the VAO ???
    
    
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