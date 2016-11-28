package alexanderraymartin.main;

import alexanderraymartin.save.Save;
import alexanderraymartin.save.Schedule;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;
import alexanderraymartin.ui.Screen;

public class Main {

  /**
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    Building.getBuildings();
    Save.getInstance();
    Graph graph = new Graph();
    new Screen(graph);

    // test loading buildings
    for (int i = 0; i < Building.buildings.size(); i++) {
      System.out.println("Name: " + Building.buildings.get(i).name + " | Building Number: "
          + Building.buildings.get(i).buildingNumber);
    }

    // test adding classes to schedule
    System.out.println("\nSchedule: ");
    for (int i = 0; i < Schedule.getInstance().classes.size(); i++) {
      System.out.println(Schedule.getInstance().classes.get(i));
    }

  }
}
