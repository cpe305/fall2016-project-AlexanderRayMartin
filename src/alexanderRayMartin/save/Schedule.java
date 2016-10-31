package alexanderRayMartin.save;

import java.io.Serializable;
import java.util.ArrayList;

import alexanderRayMartin.searchAlgorithm.Building;

public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<Building> classes;

    public Schedule() {
        classes = new ArrayList<Building>();
    }

    public int getBuildingIndex(Building building) {
        for (int i = 0; i < classes.size(); i++)
            if (classes.get(i).equals(building))
                return i;
        return -1;
    }

    public void removeBuilding(Building building) {
        classes.remove(building);
    }
}
