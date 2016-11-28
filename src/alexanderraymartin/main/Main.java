package alexanderraymartin.main;

import alexanderraymartin.save.Save;
import alexanderraymartin.save.Schedule;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;
import alexanderraymartin.ui.Screen;

/**
 * @author Alex Martin.
 *
 */
public class Main {

  /**
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    MapEditor.setEditMode(false);
    Building.getBuildings();
    Save.getInstance();
    Graph graph = new Graph();
    new Screen(graph);

    // test loading buildings
    for (int i = 0; i < Building.getBuildings().size(); i++) {
      System.out.println("Name: " + Building.getBuildings().get(i).getName()
          + " | Building Number: " + Building.getBuildings().get(i).getBuildingNumber()
          + " | Node Number: " + Building.getBuildings().get(i).getNodeNumber());
    }

    // test adding classes to schedule
    System.out.println("\nSchedule: ");
    for (int i = 0; i < Schedule.getInstance().getClasses().size(); i++) {
      System.out.println(Schedule.getInstance().getClasses().get(i));
    }

  }
}
