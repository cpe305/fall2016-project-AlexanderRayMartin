package alexanderraymartin.searchalgorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {

  private int[][] adj;
  private boolean[] visited;
  private int numVertices;
  private int rows;
  private int cols;

  /**
   * @param rows The number of rows.
   * @param cols The number of columns.
   */
  public Graph(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    createAdj();
  }

  /**
   * @param file The file's path.
   * @return A FileReader with the file.
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
   * @param fileReader The FileReader for a file that will be converted into an array.
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

  public int vertices() {
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
    for (int i = 0; i < vertices(); i++) {
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
    int current = start;
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
        for (int i = 0; i < vertices(); i++) {
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
        for (int i = 0; i < vertices(); i++) {
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

  public static class Error extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public Error(String message) {
      super(message);
    }
  }
}
