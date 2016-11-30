package alexanderraymartin.main;

import alexanderraymartin.save.Save;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;
import alexanderraymartin.ui.Screen;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Alex Martin.
 *
 */
public class Main {

  private static Logger log;

  private Main() {
    MapEditor.setEditMode(false);
    Building.getBuildings();
    Save.getInstance();
    Graph graph = new Graph();
    new Screen(graph);
  }

  /**
   * @return A logger to the console.
   */
  public static Logger getLogger() {
    if (log == null) {
      log = Logger.getLogger("MyLogger");
      log.setLevel(Level.ALL);
      ConsoleHandler handler = new ConsoleHandler();
      handler.setLevel(Level.ALL);
      handler.setFormatter(new SimpleFormatter());
      log.addHandler(handler);
    }
    return log;
  }

  /**
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    new Main();
  }
}
