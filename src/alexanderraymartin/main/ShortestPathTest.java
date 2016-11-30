package alexanderraymartin.main;

import alexanderraymartin.searchalgorithm.Graph;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * @author Alex Martin.
 *
 */
public class ShortestPathTest extends Applet {

  private static final long serialVersionUID = 1L;

  // array that holds the maze info
  private int[][] maze;

  // graph that holds the edges
  private transient Graph graph;

  // number of rows and columns in the maze
  private int rows;
  private int cols;

  private int currentLocation;
  private int stopLocation;

  // booleans for keeping track of certain states
  private boolean needMaze;
  private boolean needLocations;
  private boolean inTestMode;
  private boolean victory;

  // initial size of maze - if bigger may go off window
  private static final int maxRows = 20;
  private static final int maxCols = 30;
  private static final int winAmount = 8;
  // size of each block in pixels
  private static final int blkSize = 20;

  // color numbers for file
  private static final int black = -1;
  private static final int white = 0;
  private static final int yellow = 1;
  private static final int green = 2;
  private static final int red = 3;

  private static final String messageTip =
      "TIP: 'Test Mode' allows you to replace the start and end blocks using the mouse buttons!";
  // inner class that displays the maze
  private MazeCanvas mazeField;

  // everything is put on these panels
  private JPanel mainPanel;
  private JPanel filePanel;
  private JPanel goPanel;
  private JPanel pathLengthPanel;
  private JPanel mazePanel;
  private JPanel radioButtonPanel;

  // label, textfield, string, and load button for the file
  private JLabel fileLabel;
  private JLabel pathLength;
  private JTextField fileText;
  private String fileName;
  private JButton fileButton;
  private JButton goButton;
  private JRadioButton radioButton1;
  private JRadioButton radioButton2;
  private ButtonGroup radioButtonGroup;

  // this listener object responds to button events
  private transient ButtonActionListener buttonListener;
  private transient MouseEventListener myMouseListener;

  private enum State {
    DISPLAY_SHORTEST_PATH, ADVANCE, ADD_BLOCK, NEED_START, NEED_END
  }

  // holds the current state of the path
  private State pathState;
  private State location;


  /**
   * @Override.
   */
  @Override
  public void init() {
    Main.getLogger().fine("Maze started"); // goes to console
    inTestMode = false;
    needMaze = true;
    needLocations = true;
    victory = false;
    myMouseListener = new MouseEventListener();
    buttonListener = new ButtonActionListener();

    mainPanel = new JPanel(); // panel that holds everything
    filePanel = new JPanel(); // holds file stuff
    goPanel = new JPanel(); // holds go button
    pathLengthPanel = new JPanel(); // used to display path length to user

    mazePanel = new JPanel(); // holds maze
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    // components for loading the filename
    fileLabel = new JLabel("File name:");
    filePanel.add(fileLabel);

    fileText = new JTextField("MazeTest.txt", 20);
    filePanel.add(fileText);
    fileButton = new JButton("Load File");
    filePanel.add(fileButton);
    goButton = new JButton("Go");
    goPanel.add(goButton);

    pathLength = new JLabel(messageTip);
    pathLengthPanel.add(pathLength);

    fileButton.addActionListener(buttonListener);
    goButton.addActionListener(buttonListener);

    mazeField = new MazeCanvas();
    mazePanel.add(mazeField);
    mazeField.addMouseListener(myMouseListener);

    // radio buttons
    radioButtonPanel = new JPanel();
    radioButton1 = new JRadioButton("Standard Mode", true);
    radioButton1.addActionListener(buttonListener);
    radioButton2 = new JRadioButton("Test Mode", false);
    radioButton2.addActionListener(buttonListener);
    radioButtonGroup = new ButtonGroup();
    radioButtonGroup.add(radioButton1);
    radioButtonGroup.add(radioButton2);
    radioButtonPanel.add(radioButton1);
    radioButtonPanel.add(radioButton2);

    // now add the maze panels to the applet
    mainPanel.add(filePanel);
    mainPanel.add(goPanel);
    mainPanel.add(radioButtonPanel);
    mainPanel.add(pathLengthPanel);
    mainPanel.add(mazePanel);
    add(mainPanel);
  }



  private int getMessage() {
    if (!graph.isPath(currentLocation, stopLocation)) {
      pathLength.setText("No path is available!");
      victory = false;
      return -1; // escape value
    } else if (graph.fewestEdgePath(currentLocation, stopLocation) <= winAmount) {
      pathLength.setText("Congratulations, you are within " + winAmount + " spaces of the goal!");
      victory = true;
    } else {
      pathLength.setText("Path Length: " + graph.fewestEdgePath(currentLocation, stopLocation));
      victory = false;
    }
    return 0;

  }

  private void replaceBlock(int location, int color) {
    int ycoord = location / cols;
    int xcoord = location % cols;
    maze[ycoord][xcoord] = color;
    mazeField.paintSingle(ycoord, xcoord, mazeField.getGraphics());
  }

  private void removeColor(int color) {
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        if (maze[y][x] == color) {
          maze[y][x] = white;
          mazeField.paintSingle(y, x, mazeField.getGraphics());
        }
      }
    }
  }

  private void placeColor(int ycoord, int xcoord, int color) { // places a single color
    removeColor(color);
    maze[ycoord][xcoord] = color;
    mazeField.paintSingle(ycoord, xcoord, mazeField.getGraphics());
  }

  private void drawPath(int[] shortestPath) { // draw the shortest path
    if (!needLocations && getMessage() != -1) {
      for (int i = 0; i < shortestPath.length - 1; i++) {
        if (shortestPath[i] != currentLocation && shortestPath[i] != stopLocation) {
          replaceBlock(shortestPath[i], yellow);
        }
      }
    }
  }

  private void removeEdges(int location) { // removes old edges
    int ycoord = location / cols;
    int xcoord = location % cols;
    graph.removeEdge((ycoord - 1) * cols + xcoord, ycoord * cols + xcoord); // above
    graph.removeEdge((ycoord + 1) * cols + xcoord, ycoord * cols + xcoord); // below
    graph.removeEdge(ycoord * cols + (xcoord - 1), ycoord * cols + xcoord); // left
    graph.removeEdge(ycoord * cols + (xcoord + 1), ycoord * cols + xcoord); // right
  }

  private boolean inBoundary(int ycoord, int xcoord) { // return true if valid area
    if (ycoord < 0 || ycoord >= rows || xcoord < 0 || xcoord >= cols) {
      return false;
    }
    return true;
  }

  private void checkEdge(int ycoord, int xcoord, int y0, int x0) {
    if (inBoundary(ycoord, xcoord)) { // check boundary
      if (maze[ycoord][xcoord] != black) {
        graph.addEdge(y0 * cols + x0, ycoord * cols + xcoord);
      }
    }
  }

  private void makeGraph() { // creates the graph (adj matrix)
    graph = new Graph(rows, cols);
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        if (maze[y][x] != black) {
          checkEdge(y - 1, x, y, x); // above
          checkEdge(y + 1, x, y, x); // below
          checkEdge(y, x - 1, y, x); // left
          checkEdge(y, x + 1, y, x); // right

        }
      }
    }
  }

  /**
   * @param fileName The file's path.
   * @return True if maze creation is successful.
   */
  public boolean makeMaze(String fileName) { // create maze array from file
    try {
      Scanner scanner = new Scanner(new File(fileName));
      rows = scanner.nextInt();
      cols = scanner.nextInt();
      maze = new int[rows][cols];
      // fill out maze matrix
      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          maze[y][x] = scanner.nextInt();
        }
      }
      scanner.close();
      mazeField.paint(mazeField.getGraphics());
      needMaze = false;
      needLocations = true;
      makeGraph(); // make the graph
      return true;
    } catch (Exception exception) {
      Main.getLogger().fine(exception.toString());
      pathLength.setText("Error: File not found or error in file!");
      needMaze = true;
      needLocations = true;
      return false;
    }
  }

  // this object is triggered whenever a button is clicked
  private class ButtonActionListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      Object source = event.getSource();

      if (source == fileButton) {
        fileName = fileText.getText();
        pathState = State.DISPLAY_SHORTEST_PATH;
        location = State.NEED_START;
        needLocations = true;
        pathLength.setText(messageTip);
        makeMaze(fileName);
      } else if (source == goButton && !needLocations) {
        advanceLocation();
      }
    }

    private void advanceLocation() {
      if (getMessage() == -1) {
        return; // update message and return if no path
      }
      if (!needMaze && !victory) {
        int[] shortestPath = graph.getPath(currentLocation, stopLocation);

        switch (pathState) {
          case DISPLAY_SHORTEST_PATH: // shows the shortest path
            removeColor(yellow);
            drawPath(shortestPath);
            pathState = State.ADVANCE;
            getMessage(); // update message
            break;
          case ADVANCE: // moves along path

            replaceBlock(currentLocation, white);
            currentLocation = shortestPath[0];
            replaceBlock(currentLocation, green);
            pathState = State.ADD_BLOCK;
            getMessage(); // update message
            break;
          case ADD_BLOCK:// add random block along path

            float random = (float) Math.random();
            int index = Math.round((shortestPath.length - 1) * random);
            int location = shortestPath[index];
            if (shortestPath[index] == stopLocation) {
              location = shortestPath[index - 1]; // upper bound
            }
            if (shortestPath[index] == currentLocation) {
              location = shortestPath[index + 1]; // lower bound
            }
            replaceBlock(location, black); // add block in path
            pathState = State.DISPLAY_SHORTEST_PATH;
            removeEdges(location); // updates the graph
            break;
          default:
            break;

        }
      }
    }
  }

  private class MouseEventListener implements MouseListener {
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent event) {}

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent event) {

      if (radioButton1.isSelected()) {
        inTestMode = false;
      } else if (radioButton2.isSelected()) {
        inTestMode = true;
      }

      // location on the mazeCanvas where mouse was clicked
      // upper-left is (0,0)
      int startX = event.getX();
      int startY = event.getY();
      int xcoord = startX / blkSize;
      int ycoord = startY / blkSize;

      if (inTestMode) {
        testMode(ycoord, xcoord, event); // test mode
      } else if (needLocations) {
        placeLocations(ycoord, xcoord); // standard mode
      }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent event) {}

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent event) {}

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent event) {}

    private void testMode(int ycoord, int xcoord, MouseEvent event) {
      if (!needMaze && inBoundary(ycoord, xcoord)) {
        if (maze[ycoord][xcoord] == white || maze[ycoord][xcoord] == yellow) {
          pathState = State.ADVANCE;
          if (event.getButton() == MouseEvent.BUTTON1) {
            placeColor(ycoord, xcoord, green);
            currentLocation = ycoord * cols + xcoord;
          } else {
            placeColor(ycoord, xcoord, red);
            stopLocation = ycoord * cols + xcoord;
            needLocations = false;
          }
          removeColor(yellow);
          drawPath(graph.getPath(currentLocation, stopLocation));
        }
      }
    }

    private void placeLocations(int ycoord, int xcoord) {
      if (!needMaze && needLocations && inBoundary(ycoord, xcoord)) {
        if (maze[ycoord][xcoord] == white) {
          switch (location) {
            case NEED_START:
              placeColor(ycoord, xcoord, green);
              currentLocation = ycoord * cols + xcoord;
              location = State.NEED_END;
              break;
            case NEED_END:
              placeColor(ycoord, xcoord, red);
              stopLocation = ycoord * cols + xcoord;
              location = State.NEED_START;
              needLocations = false;
              break;
            default:
              break;
          }
        }
      }
    }
  }

  /**
   * @author Alex Martin.
   *
   */
  class MazeCanvas extends Canvas {
    // this class paints the output window


    private static final long serialVersionUID = 1L;

    // the constructor sets it up
    MazeCanvas() {
      rows = maxRows;
      cols = maxCols;
      maze = new int[maxRows][maxCols];
      setSize(cols * blkSize, rows * blkSize);
      setBackground(Color.white);
    }

    /**
     * @param ycoord The x coordinate.
     * @param xcoord The y coordinate.
     * @param graphics The graphics to draw on.
     */
    public void paintSingle(int ycoord, int xcoord, Graphics graphics) {
      paintSingle(ycoord * cols + xcoord, graphics);

    }

    /**
     * @param location The location.
     * @param graphics The graphics to draw on.
     */
    public void paintSingle(int location, Graphics graphics) {
      int ycoord = location / cols;
      int xcoord = location % cols;
      if (maze[ycoord][xcoord] == white) {
        // location is clear
        graphics.setColor(Color.white);
      } else if (maze[ycoord][xcoord] == black) {
        // location is a wall
        graphics.setColor(Color.black);
      } else if (maze[ycoord][xcoord] == yellow) {
        // path - yellow
        graphics.setColor(Color.yellow);
      } else if (maze[ycoord][xcoord] == green) {
        // start location - green
        graphics.setColor(Color.green);
      } else if (maze[ycoord][xcoord] == red) {
        // end location - red
        graphics.setColor(Color.red);
      }
      graphics.fillRect(xcoord * blkSize, ycoord * blkSize, blkSize, blkSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Canvas#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics graphics) { // paints entire canvas
      graphics.setColor(Color.white);
      graphics.fillRect(0, 0, cols * blkSize, rows * blkSize);

      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          if (maze[y][x] == white) {
            // location is clear
            graphics.setColor(Color.white);
          } else if (maze[y][x] == black) {
            // location is a wall
            graphics.setColor(Color.black);
          } else if (maze[y][x] == yellow) {
            // path - yellow
            graphics.setColor(Color.yellow);
          } else if (maze[y][x] == green) {
            // start location - green
            graphics.setColor(Color.green);
          } else if (maze[y][x] == red) {
            // end location - red
            graphics.setColor(Color.red);
          }
          // draw the location
          graphics.fillRect(x * blkSize, y * blkSize, blkSize, blkSize);
        }
      }
    }
  }
}
