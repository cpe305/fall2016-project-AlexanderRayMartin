package alexanderraymartin.main.test;

import alexanderraymartin.main.Main;

import org.junit.Test;

import java.util.logging.Logger;



public class MainTest {

  @Test
  public void test() {
    Logger log = Main.getLogger();
    assert (log != null);
  }

}
