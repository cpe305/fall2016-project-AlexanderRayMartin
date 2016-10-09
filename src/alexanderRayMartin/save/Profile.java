package alexanderRayMartin.save;

public class Profile {

	public Schedule schedule;

	public Profile() {
		schedule = new Schedule();
	}
	
	public void addClass(int buildingNumber){
		schedule.add(new Course(buildingNumber));
	}
}
