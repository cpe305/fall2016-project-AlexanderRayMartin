package alexanderraymartin.main;

import alexanderraymartin.save.Save;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;
import alexanderraymartin.ui.Screen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
   * Creates and saves a blank node map to a file.
   * 
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public static void createBlankNodeMap(int rows, int cols) {
    File file = new File("src/mapNode_temp.txt");
    FileWriter fw;
    Main.getLogger().fine("Creating blank node map");
    try {
      fw = new FileWriter(file);
      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(String.valueOf(rows));
      bw.write("  ");
      bw.write(String.valueOf(cols));
      bw.newLine();

      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          bw.write("-1 ");
        }
        if (y != rows - 1) {
          bw.newLine();
        }
      }

      bw.close();
      fw.close();
    } catch (IOException exception) {
      Main.getLogger().info(exception.toString());
      exception.printStackTrace();
    }
  }

  /**
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    new MapEditor();
  }

}
