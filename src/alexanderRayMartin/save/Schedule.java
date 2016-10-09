package alexanderRayMartin.save;

import java.util.ArrayList;

public class Schedule {

	private ArrayList<Course> classes;

	public Schedule() {
		classes = new ArrayList<Course>();
	}
	
	public void add(Course e){
		classes.add(e);
	}
}
