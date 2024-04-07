package deadzone.scenes;

import deadzone.Deadzone;
import deadzone.GameTimer;
import deadzone.Window;
import deadzone.assets.AssetManager;
import deadzone.graphics.IRenderable;
import deadzone.graphics.Renderer;

import java.util.EnumMap;
import java.util.LinkedHashMap;


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
  LinkedHashMap<String, IRenderable> renderObjects = new LinkedHashMap<>();
  
  protected final Deadzone game = Deadzone.getApplication();
  protected final GameTimer timer = Deadzone.getApplication().getTimer();
  protected final Window window = Deadzone.getApplication().getWindow();
  protected final AssetManager assets = Deadzone.getApplication().getAssetManager();
  
  
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
  public void addObject(String id, IRenderable obj) {
    renderObjects.put(id, obj);
  }
  
  
  /**
   * Removes an existing object from the scene
   */
  public void removeObject(String id) {
    renderObjects.remove(id);
  }
  
  
  /**
   * This is the input part of the current loop for the actual scene.
   */
  public void processInputs() {
  
  }
  
  
  /**
   * This is the update loop for the actual scene.
   */
  public void updateScene() {
    sendToRenderer();
  }
  
  
  /**
   * Sends the current scene data to the renderer
   */
  protected void sendToRenderer() {
    // Now after all objects are adjusted accordingly, they are sent to the renderer with all their updated data for the next render loop
    Renderer renderer = Deadzone.getApplication().getRenderer();
    renderObjects.forEach((id, obj) -> {
      renderer.registerObject(obj);
    });
  }
  
  
}