package alexanderraymartin.save.test;

import alexanderraymartin.save.Schedule;

import org.junit.Test;

public class ScheduleTest {

  @Test
  public void test() {
    Schedule schedule = Schedule.getInstance();
    assert (schedule != null);
  }

  @Test
  public void test2() {
    Schedule schedule = Schedule.getInstance();
    assert (schedule.getClasses() != null);
  }

  @Test
  public void test3() {
    Schedule.setSchedule(Schedule.getInstance());
    assert (Schedule.getInstance() != null);
  }

  @Test
  public void test4() {
    Schedule schedule = Schedule.getInstance();
    assert (schedule.getBuildingIndex(null) == -1);
  }

  @Test
  public void test5() {
    Schedule schedule = Schedule.getInstance();
    schedule.removeBuilding(null);
    assert (true);
  }
}
