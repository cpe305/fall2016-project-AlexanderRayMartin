package alexanderraymartin.main;

import alexanderraymartin.ui.Map;
import alexanderraymartin.ui.Screen;
import alexanderraymartin.util.Mouse;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class MapEditor extends JFrame {

  private static final long serialVersionUID = 1L;

  public static final int WIDTH = Screen.WIDTH;
  public static final int HEIGHT = Screen.HEIGHT;

  public Map map;
  public Mouse mouse;

  // TODO Create Map Editor
  // currently doesn't do anything

  /**
   * Creates the window.
   */
  public MapEditor() {
    super("Map Editor");
    map = new Map("src/map.png", 1, 0, 0);
    mouse = new Mouse();
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
    add(map);
    setVisible(true);
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * @param file The file to open.
   * @return A FileReader with the new file.
   */
  public static FileReader openFile(String file) {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(file);
    } catch (FileNotFoundException exception) {
      exception.printStackTrace();
    }
    return fileReader;
  }

  /**
   * @param fileReader The file to convert to an array.
   * @return The integer array.
   */
  public static int[] getArray(FileReader fileReader) {
    ArrayList<Integer> arrayList = new ArrayList<Integer>();
    int[] array = null;
    String line;
    String[] items;
    BufferedReader bufferedReader;
    try {
      bufferedReader = new BufferedReader(fileReader);
      while ((line = bufferedReader.readLine()) != null) {
        items = line.split(" ");
        for (int i = 0; i < items.length; i++) {
          arrayList.add(Integer.parseInt(items[i].trim()));
        }
      }
      array = arrayList.stream().mapToInt(i -> i).toArray();
      bufferedReader.close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return array;
  }

  public void saveGrid() {

  }

  public void loadGrid() {

  }

  public static void main(String[] args) {
    new MapEditor();
  }

}
