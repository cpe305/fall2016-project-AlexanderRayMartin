package alexanderraymartin.searchalgorithm;

import alexanderraymartin.main.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

/**
 * @author Alex Martin.
 *
 */
public class Graph {


  /**
   * Path for the text file containing the map node information.
   */
  private final String mapNodeFile = "mapNode.txt";
  /**
   * Adjacency matrix.
   */
  private int[][] adj;
  /**
   * 2-D array of integers representing the nodes.
   */
  private int[][] nodes;
  /**
   * ArrayList that contains each path for the entire schedule.
   */
  private static ArrayList<int[]> schedulePaths;
  /**
   * ArrayList that contains building nodes for the schedule.
   */
  private static ArrayList<Integer> buildingNodes;

  private boolean[] visited;
  private int numVertices;

  /**
   * The number of rows.
   */
  private int rows;
  /**
   * The number of columns.
   */
  private int cols;

  /**
   * Graph constructor for ShortestPathTest.
   * 
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public Graph(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    createAdj();
  }

  /**
   * Graph constructor.
   */
  public Graph() {
    createNodes();
    createAdj();
  }

  /**
   * @return Nodes array.
   */
  public int getNodes(int xcoord, int ycoord) {
    return nodes[ycoord][xcoord];
  }

  /**
   * @param xcoord The x index.
   * @param ycoord The y index.
   * @param data The data to set.
   */
  public void setNode(int xcoord, int ycoord, int data) {
    nodes[ycoord][xcoord] = data;
  }

  /**
   * @return The number of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   * @return The number of columns.
   */
  public int getCols() {
    return cols;
  }

  /**
   * @return Creates an arrayList if schedulePaths is null, and returns it.
   */
  public static ArrayList<int[]> getSchedulePaths() {
    if (schedulePaths == null) {
      schedulePaths = new ArrayList<int[]>();
    }
    return schedulePaths;
  }

  /**
   * @return Creates an arrayList if buildingNodes is null, and returns it.
   */
  public static ArrayList<Integer> getBuildingNodes() {
    if (buildingNodes == null) {
      buildingNodes = new ArrayList<Integer>();
    }
    return buildingNodes;
  }

  /**
   * Creates a new arrayList for schedulePaths.
   */
  public static void clearSchedulePaths() {
    schedulePaths = new ArrayList<int[]>();
    buildingNodes = new ArrayList<Integer>();
  }

  private int[][] createNodes() {
    int[] array = getArray(openFile(mapNodeFile));
    int index = 2;
    this.rows = array[0];
    this.cols = array[1];
    nodes = new int[rows][cols];

    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        nodes[y][x] = array[index++];
      }
    }
    return nodes;
  }

  /**
   * Saves the node grid to the file.
   */
  public void saveNodes() {
    File file = new File("src/mapNode.txt");
    FileWriter fw;
    Main.getLogger().fine("Saving nodes");
    try {
      fw = new FileWriter(file);
      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(String.valueOf(rows));
      bw.write("  ");
      bw.write(String.valueOf(cols));
      bw.newLine();

      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          bw.write(String.valueOf(nodes[y][x]));
          if (nodes[y][x] == -1) {
            bw.write(" ");
          } else {
            bw.write("  ");
          }
        }
        if (y != rows - 1) {
          bw.newLine();
        }
      }

      bw.close();
      fw.close();
    } catch (IOException exception) {
      Main.getLogger().log(Level.FINE, "Exception", exception);
      exception.printStackTrace();
    }
  }

  /**
   * @param file The file's path.
   * @return A BufferedReader with the file.
   */
  public static BufferedReader openFile(String file) {
    BufferedReader bufferedReader;
    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(file);
    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    return bufferedReader;
  }

  /**
   * @param bufferedReader The FileReader for a file that will be converted into an array.
   * @return The integer array.
   */
  public static int[] getArray(BufferedReader bufferedReader) {
    ArrayList<Integer> arrayList = new ArrayList<Integer>();
    int[] array = null;
    String line;
    String[] items;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        items = line.split("\\s+");
        for (int i = 0; i < items.length; i++) {
          arrayList.add(Integer.parseInt(items[i].trim()));
        }
      }
      array = arrayList.stream().mapToInt(i -> i).toArray();
      bufferedReader.close();
    } catch (IOException exception) {
      Main.getLogger().log(Level.FINE, "Exception", exception);
      exception.printStackTrace();
    }
    return array;
  }

  /**
   * Creates the adjacency matrix.
   */
  public void createAdj() {
    numVertices = rows * cols;
    visited = new boolean[numVertices];
    adj = new int[numVertices][numVertices];
    for (int y = 0; y < numVertices; y++) {
      for (int x = 0; x < numVertices; x++) {
        adj[y][x] = 0;
      }
    }
  }

  /**
   * @return The number of vertices.
   */
  public int getNumVertices() {
    return numVertices;
  }

  /**
   * Finds the shortest path by checking adjacent blocks and choosing the one with the shortest path
   * to add to the array.
   * 
   * @param start The starting index.
   * @param stop The ending index.
   * @return The array of integers representing the path.
   */
  public int[] getPath(int start, int stop) {

    int shortestPath = fewestEdgePath(start, stop);
    if (shortestPath == -1) {
      return new int[0]; // return if no path
    }
    if (start == stop) {
      return new int[0];
    }
    int[] path = new int[shortestPath];
    int index = 0;
    int distance = -1;
    int closestLocation = 0;
    int ycoord = start;
    boolean foundEdge;
    while (distance != 1) {
      foundEdge = false;
      for (int xcoord = 0; xcoord < numVertices; xcoord++) {
        if (adj[ycoord][xcoord] == 1) { // checks for adjacency
          distance = fewestEdgePath(xcoord, stop);
          if (distance != -1 && distance < shortestPath) {
            closestLocation = xcoord;
            shortestPath = distance;
            foundEdge = true;
          }
        }
      }
      if (foundEdge) {
        path[index] = closestLocation;
        ycoord = closestLocation;
        index++;
      }
    }
    return path;
  }

  /**
   * Checks if the mouse click is in the boundary.
   * 
   * @param ycoord The y coordinate of the mouse.
   * @param xcoord The x coordinate of the mouse.
   * @return True if in bounds, else false.
   */
  private boolean inBoundary(int ycoord, int xcoord) {
    if (ycoord < 0 || ycoord >= rows || xcoord < 0 || xcoord >= cols) {
      return false;
    }
    return true;
  }


  /**
   * @param ycoord Y coordinate.
   * @param xcoord Y coordinate.
   * @param y0 Y initial.
   * @param x0 X initial.
   */
  private void checkEdge(int ycoord, int xcoord, int y0, int x0) {
    if (inBoundary(ycoord, xcoord)) { // check boundary
      if (nodes[ycoord][xcoord] != -1) {
        addEdge(y0 * cols + x0, ycoord * cols + xcoord);
      }
    }
  }

  /**
   * Creates the graph's adjacency matrix.
   */
  public void makeGraph() {
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        if (nodes[y][x] != -1) {
          checkEdge(y - 1, x, y, x); // above
          checkEdge(y + 1, x, y, x); // below
          checkEdge(y, x - 1, y, x); // left
          checkEdge(y, x + 1, y, x); // right

        }
      }
    }
  }

  /**
   * @param from The starting edge.
   * @param to The destination edge.
   */
  public void addEdge(int from, int to) { // adds an edge to adj matrix
    if (from < 0 || from >= numVertices || to < 0 || to >= numVertices) {
      return; // Invalid vertex
    }
    adj[from][to] = 1;
    adj[to][from] = 1;
  }

  /**
   * @param from The starting edge.
   * @param to The destination edge.
   */
  public void removeEdge(int from, int to) {
    if (from < 0 || from >= numVertices || to < 0 || to >= numVertices) {
      return; // Invalid vertex
    }
    adj[from][to] = 0;
    adj[to][from] = 0;
  }

  /**
   * Returns true if a path exists.
   * 
   * @param start The start of the path.
   * @param stop The end of the path.
   * @return True if a path exists from start to stop.
   */
  public boolean isPath(int start, int stop) { //
    visitHelper(start);
    if (visited[stop] == true) {
      return true;
    }
    return false;
  }

  private void visitHelper(int start) { // helper function for visit
    for (int i = 0; i < visited.length; i++) {
      visited[i] = false;
    }
    visit(start);
  }

  private void visit(int start) { // visits each element
    visited[start] = true;
    for (int i = 0; i < getNumVertices(); i++) {
      if (adj[start][i] == 1 && visited[i] == false) {
        visit(i);
      }
    }
  }

  /**
   * Returns shortest path length.
   * 
   * @param start The start of the path.
   * @param stop The end of the path.
   * @return The length of the shortest path.
   */
  public int fewestEdgePath(int start, int stop) { //
    Queue<Integer> queue = new LinkedList<>();
    Queue<Integer> tempQueue = new LinkedList<>();
    queue.add(start);
    int current;
    int count = 1;
    if (!isPath(start, stop)) {
      return -1; // no path: -1 is escape value for other functions
    }
    if (start == stop) {
      return 0;
    }
    for (int i = 0; i < visited.length; i++) {
      visited[i] = false;
    }
    visited[start] = true;
    while (true) {
      while (!queue.isEmpty()) {
        current = queue.remove();
        for (int i = 0; i < getNumVertices(); i++) {
          if (adj[current][i] == 1) {
            if (i == stop) {
              return count;
            }
            if (!visited[i]) {
              tempQueue.add(i);
              visited[i] = true;
            }
          }
        }
      }
      count++;
      while (!tempQueue.isEmpty()) {
        current = tempQueue.remove();
        for (int i = 0; i < getNumVertices(); i++) {
          if (adj[current][i] == 1) {
            if (i == stop) {
              return count;
            }
            if (!visited[i]) {
              queue.add(i);
              visited[i] = true;
            }

          }
        }
      }
      count++;
    }
  }
}
