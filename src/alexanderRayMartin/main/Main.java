package alexanderRayMartin.main;

import alexanderRayMartin.save.Save;
import alexanderRayMartin.searchAlgorithm.Building;
import alexanderRayMartin.ui.Screen;

public class Main {

    public static void main(String[] args) {
        Building.getBuildings();
        new Screen();

        // test loading buildings
        for (int i = 0; i < Building.buildings.size(); i++)
            System.out.println("Name: " + Building.buildings.get(i).name + " | Building Number: "
                    + Building.buildings.get(i).buildingNumber);

        // test adding classes to schedule
        System.out.println("\nSchedule size: " + Save.schedule.classes.size());

        // TODO fix this possible synchronization error
        for (int i = 0; i < Save.schedule.classes.size(); i++) {
            System.out.println(Save.schedule.classes.get(i));
        }

    }
}