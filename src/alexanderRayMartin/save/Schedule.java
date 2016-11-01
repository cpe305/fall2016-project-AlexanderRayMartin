package alexanderRayMartin.save;

import java.io.Serializable;
import java.util.ArrayList;

import alexanderRayMartin.searchAlgorithm.Building;

public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<Building> classes;
    public static Schedule schedule;

    private Schedule() {
        classes = new ArrayList<Building>();
    }

    /** returns the instance of schedule */
    public static Schedule getInstance() {
        if (schedule == null)
            schedule = new Schedule();
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        Schedule.schedule = schedule;
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
