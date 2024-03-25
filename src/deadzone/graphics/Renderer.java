package deadzone.graphics;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;


public class Renderer {
  /** Stores the shader program in which is the connector object to all shader programs in the GPU memory */
  protected ShaderProgram shaderProgram;
  
  /** Stores all handles of VBO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexBufferObject> attachedVboObjects = new ArrayList<>();
  
  /** Stores all handles of VAO objects which are currently stored in the GPU memory for rendering */
  ArrayList<VertexArrayObject> attachedVaoObjects = new ArrayList<>();
  
  ArrayList<IRenderable> registeredRenderableObjects = new ArrayList<>();
  
  
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
  
  /**
   * The main render loop.
   * At this point all objects VAOs are ready and have their VBOs attached and all vertex attribute pointer are defined.
   * So we only render each VAO with the proper render type and the remove all registered objects as preparation for the
   * next render loop.
   */
  public void renderRegisteredObjects() {
    int totalVerticesInCurrentVAO;
    int vertexPointer = 0;
    
    // Create the VAO with all attached VBOs for render type GL_TRIANGLES
    totalVerticesInCurrentVAO = attachAllTriangles();
    
    // Render loop for VAO of type GL_TRIANGLES
    for ( VertexArrayObject vao : attachedVaoObjects ) {
      glDrawArrays(vao.GL_RENDER_TYPE, vertexPointer, totalVerticesInCurrentVAO);
      vertexPointer += totalVerticesInCurrentVAO;
    }
  
    // Render loop for VAO of type GL_LINE
    // ... coming soon ...
    
    // TODO: Ist es vielleicht doch einfacher, auch die verschiedenen GL_TYPES alle in ein zentrales VAO zu stecken?
    //       Denn das mit dem Pointer könnte sonst noch Schwierigkeiten bereiten je nachden...
  
    glDisableVertexAttribArray(0);   // TODO: Mal checken, das hatte nämlich gefehlt. Muss aber vermutlich 4x insgesamt aufgerufen werden für die 2 Triangles...

    
    // Remove all graphics data for the next turn
    for (VertexBufferObject vbo : attachedVboObjects) {
      vbo.delete();
    }
    for (VertexArrayObject vao : attachedVaoObjects) {
      vao.delete();
    }
    attachedVboObjects.clear();
    attachedVaoObjects.clear();
    registeredRenderableObjects.clear();
  }
  
  
  /**
   * Creates a single VAO and attaches all VBOs for the registered objects which uses GL_TRIANGLES.
   *
   * Note, that the render type GL_TRIANGLES is not the same as the shape class TriangleShape. For example the shape
   * class RectangleShape also uses the render type GL_TRIANGLE because it uses two triangles to form a rectangle.
   *
   * @return The total count of vertices to render for that VAO.
   */
  private int attachAllTriangles() {
    // Create the VAO
    VertexArrayObject vaoTriangle = new VertexArrayObject(GL_TRIANGLES);
    vaoTriangle.bind();

    int vertexCount = 0;
    
    // Create, prepare and bind all VBOs of all renderable objects which use GL_TRIANGLES
    for ( IRenderable obj : registeredRenderableObjects ) {
      if (obj.getGL_TYPE() == GL_TRIANGLES) {
        ArrayList<VertexBufferObject> vboList = obj.prepareVBOs();
        vertexCount += obj.getVertexCount();
        for ( VertexBufferObject vbo : vboList ) {
          vbo.initialize();
          attachedVboObjects.add(vbo);
        }
      }
    }
    attachedVaoObjects.add(vaoTriangle);
    
    return vertexCount;
  }
  
  
  
  public void registerObject(IRenderable obj) {
    registeredRenderableObjects.add(obj);
  }
  
  
  /**
   * Free up all resources from GPU memory
   */
  public void dispose() {
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