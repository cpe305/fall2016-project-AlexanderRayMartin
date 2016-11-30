package alexanderraymartin.main.test;

import alexanderraymartin.main.MapEditor;

import org.junit.Test;

public class MapEditorTest {

  @Test
  public void test() {
    MapEditor.setEditMode(true);
    assert (MapEditor.inEditMode());
  }

}
