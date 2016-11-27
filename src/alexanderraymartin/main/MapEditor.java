package alexanderraymartin.main;

import alexanderraymartin.save.Save;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;
import alexanderraymartin.ui.Map;
import alexanderraymartin.ui.Screen;
import alexanderraymartin.util.Mouse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapEditor {

  public Map map;
  public Mouse mouse;
  public Graph graph;

  /**
   * Creates the window.
   */
  public MapEditor() {
    Building.getBuildings();
    Save.getInstance();
    new Screen();
    graph = new Graph();

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
