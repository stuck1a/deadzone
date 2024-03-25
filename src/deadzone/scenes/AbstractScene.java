package deadzone.scenes;

import deadzone.Deadzone;
import deadzone.graphics.IRenderable;
import deadzone.graphics.Renderer;

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
  IRenderable[] renderObjects;
  
  
  /**
   * Base constructor for every scene. Generates the mapping,
   * initializes object catalogue from save game and so on
   */
  public AbstractScene(Scene name) {
    // Only add this scene to the scene class mapping on first initialization of the actual scene
    if (!sceneMappings.containsKey(name)) {
      sceneMappings.put(name, this);
    }
  }
  
  
  public static AbstractScene getActiveScene() {
    return sceneMappings.get(activeScene);
  }
  
  public static void setActiveScene(Scene newScene) {
    AbstractScene.activeScene = newScene;
  }
  

  
  /**
   * Adds a new visible object to the scene
   * @param obj The new object to add
   * @return Succeeded or not
   */
  public boolean addObject(IRenderable obj) {
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
   * Processes all user inputs the active scene provides
   */
  public void processInputs() {
  
  }
  
  
  /**
   * Updates all objects in the active scene
   */
  public void updateRegisteredObjects() {
  
  }
  
  /**
   * Sends a prepared renderable object to the renderer so that the renderer knows what and how to draw
   */
  protected void addObjectToRenderer() {
    Renderer renderer = Deadzone.getApplication().getRenderer();
  }
}