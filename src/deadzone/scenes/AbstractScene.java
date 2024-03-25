package deadzone.scenes;

import deadzone.Deadzone;
import deadzone.graphics.IRenderable;
import deadzone.graphics.Renderer;

import java.util.ArrayList;
import java.util.EnumMap;


/**
 * Base class for any existing scene.
 * Provides general functions for scene management and their mapping and the object catalogue every scene must have.
 * TODO: Ist ein Interface hier vielleicht besser geeignet?
 */
public abstract class AbstractScene {
  
  /** This is the scene which the render loop will currently render */
  static Scene activeScene;
  
  /** Contains the mapping info between scenes and their related classes */
  static EnumMap<Scene, AbstractScene> sceneMappings = new EnumMap<>(Scene.class);
  
  /**
   * All visible objects of the scene are registered here.
   * They will be rendered in the order they were added to the list.
   */
  ArrayList<IRenderable> renderObjects = new ArrayList<>();
  
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
  public void addObject(IRenderable obj) {
    renderObjects.add(obj);
  }
  
  /**
   * Removes an existing object from the scene
   * @param id The object id
   * @return Succeeded or not
   */
  public void removeObject(int id) {
    // TODO: Introduce a ID/Name or any other possibility to find and detect the objects later again
    //       so we can remove them at runtime if necessary.
    //       (for example if a world object shall disappear from the screen)
  }
  
  
  /**
   * This is the input part of the current loop for the actual scene.
   */
  public void processInputs() {
  
  }
  
  
  /**
   * This is the update loop for the actual scene.
   */
  public void updateRegisteredObjects() {
    // add/change/remove any registered objects based on user inputs and object internal game mechanics...
    // ...
    
    // Now after all objects are adjusted accordingly, they are sent to the renderer with all their updated data for the next render loop
    Renderer renderer = Deadzone.getApplication().getRenderer();
    for (IRenderable obj : renderObjects) {
      renderer.registerObject(obj);
    }
  }

  
}