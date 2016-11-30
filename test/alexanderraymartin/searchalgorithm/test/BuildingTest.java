package alexanderraymartin.searchalgorithm.test;

import alexanderraymartin.searchalgorithm.Building;

import org.junit.Test;

public class BuildingTest {

  private static final int LENGTH = 5;
  private static final int BUFFER = 4;

  @Test
  public void test() {
    assert (Building.getBuildings() != null);
  }

  @Test
  public void test2() {
    Building building = Building.getBuildings().get(0);
    assert (building.abbreviateString(LENGTH).length() <= LENGTH + BUFFER);
  }

  @Test
  public void test3() {
    Building building = Building.getBuildings().get(0);
    int tempLength = building.getName().length();
    assert (building.padString().length() >= tempLength);
  }

}
