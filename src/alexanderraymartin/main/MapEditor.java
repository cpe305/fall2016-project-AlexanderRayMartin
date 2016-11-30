package alexanderraymartin.main;

import alexanderraymartin.save.Save;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;
import alexanderraymartin.ui.Screen;

/**
 * @author Alex Martin.
 *
 */
public class MapEditor {

  /**
   * The graph for the map editor.
   */
  private Graph graph;
  /**
   * True if the program running is in edit mode.
   */
  private static boolean editMode;

  /**
   * Creates the window.
   */
  public MapEditor() {
    setEditMode(true);
    Building.getBuildings();
    Save.getInstance();
    graph = new Graph();
    Screen screen = new Screen(graph);
    screen.createEditorInterface();
  }


  /**
   * @return True if the program is in edit mode.
   */
  public static boolean inEditMode() {
    return editMode;
  }

  /**
   * Set edit mode.
   */
  public static void setEditMode(boolean value) {
    editMode = value;
  }

  /**
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    new MapEditor();
  }

}
