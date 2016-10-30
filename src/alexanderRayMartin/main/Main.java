package alexanderRayMartin.main;

import alexanderRayMartin.save.Save;
import alexanderRayMartin.searchAlgorithm.Building;
import alexanderRayMartin.ui.Screen;

public class Main {

    public static void main(String[] args) {
        Building.getBuildings();
        new Screen();
        Save save = new Save();
        save.loadSchedule();

        // test loading buildings
        for (int i = 0; i < Building.buildings.size(); i++)
            System.out.println("Name: " + Building.buildings.get(i).name + " | Building Number: "
                    + Building.buildings.get(i).buildingNumber);

    }
}