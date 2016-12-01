package alexanderraymartin.save.test;

import alexanderraymartin.save.Save;

import org.junit.Test;

public class SaveTest {

  @Test
  public void test() {
    Save save = Save.getInstance();
    assert (save != null);
  }

}
