package deadzone.scenes;

/**
 * All existing game scenes are listed here
 */
public enum Scene {
  NONE,        // Abstract placeholder scene
  LAUNCHER,    // Initial application scene, player can login/register here, adjust basic settings and then start the game itself
  LOADING,     // Intermediate scene, might be used to load resources if necessary
  COMPOUND,    // Central and first in-game scene
  MAP,         // Accessed from the compound scene and used to start missions
}