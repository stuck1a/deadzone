package deadzone.scenes;

import deadzone.rendering.Renderable;
import deadzone.rendering.Renderer;
import deadzone.rendering.ShaderProgram;

import java.util.EnumMap;


/**
 * Base class for any existing scene.
 * Provides general functions for scene management and their mapping and the object catalogue every scene must have.
 */
public abstract class AbstractScene {
  
  /** This is the scene which the render loop will currently render */
  static Scene activeScene;
  
  /** Contains the mapping info between scenes and their related classes */
  static EnumMap<Scene, AbstractScene> sceneMappings = new EnumMap<>(Scene.class);
  
  /**
   * All visible objects of the scene are registered here
   */
  Renderable[] renderObjects;
  
  protected Renderer renderer;
  
  
  /**
   * Base constructor for every scene. Generates the mapping,
   * initializes object catalogue from save game and so on
   */
  public AbstractScene(Scene name) {
    // Only add this scene to the scene class mapping on first initialization of the actual scene
    // TODO: Move into static initializer block
    if (!sceneMappings.containsKey(name)) {
      sceneMappings.put(name, this);
    }
    // Initialize renderer
    renderer = new Renderer();
    renderer.init();
  }
  
  
  public static AbstractScene getActiveScene() {
    return sceneMappings.get(activeScene);
  }
  
  public static void setActiveScene(Scene newScene) {
    AbstractScene.activeScene = newScene;
  }
  
  
  public Renderer getRenderer() {
    return renderer;
  }
  
  
  /**
   * Adds a new visible object to the scene
   * @param obj The new object to add
   * @return Succeeded or not
   */
  public boolean addObject(Renderable obj) {
    return true;
  }
  
  /**
   * Removes an existing object from the scene
   * @param id The object id
   * @return Succeeded or not
   */
  public boolean removeObject(int id) {
    return true;
  }
  
  
  /**
   * Default renderer for scenes, called in every render loop
   * If a scene doesn't provide an own scene rendere, this one is used which simply
   * renders every registered object
   */
  public void renderScene() {
  
  }
  
}