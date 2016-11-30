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
import java.util.logging.Level;

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
  private static final int MAX_ROWS = 20;
  private static final int MAX_COLS = 30;
  private static final int WIN_AMOUNT = 8;
  // size of each block in pixels
  private static final int BLOCK_SIZE = 20;

  // color numbers for file
  private static final int BLACK = -1;
  private static final int WHITE = 0;
  private static final int YELLOW = 1;
  private static final int GREEN = 2;
  private static final int RED = 3;

  private static final String MESSAGE_TIP =
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

    pathLength = new JLabel(MESSAGE_TIP);
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
    } else if (graph.fewestEdgePath(currentLocation, stopLocation) <= WIN_AMOUNT) {
      pathLength.setText("Congratulations, you are within " + WIN_AMOUNT + " spaces of the goal!");
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
          maze[y][x] = WHITE;
          mazeField.paintSingle(y, x, mazeField.getGraphics());
        }
      }
    }
  }

  private void drawPath(int[] shortestPath) { // draw the shortest path
    if (!needLocations && getMessage() != -1) {
      for (int i = 0; i < shortestPath.length - 1; i++) {
        if (shortestPath[i] != currentLocation && shortestPath[i] != stopLocation) {
          replaceBlock(shortestPath[i], YELLOW);
        }
      }
    }
  }

  private boolean inBoundary(int ycoord, int xcoord) { // return true if valid area
    if (ycoord < 0 || ycoord >= rows || xcoord < 0 || xcoord >= cols) {
      return false;
    }
    return true;
  }

  private void checkEdge(int ycoord, int xcoord, int y0, int x0) {
    if (inBoundary(ycoord, xcoord) && maze[ycoord][xcoord] != BLACK) {
      graph.addEdge(y0 * cols + x0, ycoord * cols + xcoord);
    }
  }

  private void makeGraph() { // creates the graph (adj matrix)
    graph = new Graph(rows, cols);
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        if (maze[y][x] != BLACK) {
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
      Main.getLogger().log(Level.FINE, "Exception", exception);
      pathLength.setText("Error: File not found or error in file!");
      needMaze = true;
      needLocations = true;
      return false;
    }
  }

  // this object is triggered whenever a button is clicked
  private class ButtonActionListener implements ActionListener {
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {

      Object source = event.getSource();

      if (source == fileButton) {
        fileName = fileText.getText();
        pathState = State.DISPLAY_SHORTEST_PATH;
        location = State.NEED_START;
        needLocations = true;
        pathLength.setText(MESSAGE_TIP);
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
            removeColor(YELLOW);
            drawPath(shortestPath);
            pathState = State.ADVANCE;
            getMessage(); // update message
            break;
          case ADVANCE: // moves along path
            replaceBlock(currentLocation, WHITE);
            currentLocation = shortestPath[0];
            replaceBlock(currentLocation, GREEN);
            pathState = State.ADD_BLOCK;
            getMessage(); // update message
            break;
          case ADD_BLOCK:// add random block along path
            float random = (float) Math.random();
            int index = Math.round((shortestPath.length - 1) * random);
            int blockLocation = shortestPath[index];
            if (shortestPath[index] == stopLocation) {
              blockLocation = shortestPath[index - 1]; // upper bound
            }
            if (shortestPath[index] == currentLocation) {
              blockLocation = shortestPath[index + 1]; // lower bound
            }
            replaceBlock(blockLocation, BLACK); // add block in path
            pathState = State.DISPLAY_SHORTEST_PATH;
            removeEdges(blockLocation); // updates the graph
            break;
          default:
            break;

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
  }

  private class MouseEventListener implements MouseListener {
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent event) {
      /*
       * Not used.
       */
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
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
      int xcoord = startX / BLOCK_SIZE;
      int ycoord = startY / BLOCK_SIZE;

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
    @Override
    public void mouseReleased(MouseEvent event) {
      /*
       * Not used.
       */
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent event) {
      /*
       * Not used.
       */
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent event) {
      /*
       * Not used.
       */
    }

    private void testMode(int ycoord, int xcoord, MouseEvent event) {
      if (!needMaze && inBoundary(ycoord, xcoord)
          && (maze[ycoord][xcoord] == WHITE || maze[ycoord][xcoord] == YELLOW)) {
        pathState = State.ADVANCE;
        if (event.getButton() == MouseEvent.BUTTON1) {
          placeColor(ycoord, xcoord, GREEN);
          currentLocation = ycoord * cols + xcoord;
        } else {
          placeColor(ycoord, xcoord, RED);
          stopLocation = ycoord * cols + xcoord;
          needLocations = false;
        }
        removeColor(YELLOW);
        drawPath(graph.getPath(currentLocation, stopLocation));
      }
    }

    private void placeLocations(int ycoord, int xcoord) {
      if (!needMaze && needLocations && inBoundary(ycoord, xcoord)) {
        if (maze[ycoord][xcoord] == WHITE) {
          switch (location) {
            case NEED_START:
              placeColor(ycoord, xcoord, GREEN);
              currentLocation = ycoord * cols + xcoord;
              location = State.NEED_END;
              break;
            case NEED_END:
              placeColor(ycoord, xcoord, RED);
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

    private void placeColor(int ycoord, int xcoord, int color) { // places a single color
      removeColor(color);
      maze[ycoord][xcoord] = color;
      mazeField.paintSingle(ycoord, xcoord, mazeField.getGraphics());
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
      rows = MAX_ROWS;
      cols = MAX_COLS;
      maze = new int[MAX_ROWS][MAX_COLS];
      setSize(cols * BLOCK_SIZE, rows * BLOCK_SIZE);
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
      if (maze[ycoord][xcoord] == WHITE) {
        // location is clear
        graphics.setColor(Color.white);
      } else if (maze[ycoord][xcoord] == BLACK) {
        // location is a wall
        graphics.setColor(Color.black);
      } else if (maze[ycoord][xcoord] == YELLOW) {
        // path - yellow
        graphics.setColor(Color.yellow);
      } else if (maze[ycoord][xcoord] == GREEN) {
        // start location - green
        graphics.setColor(Color.green);
      } else if (maze[ycoord][xcoord] == RED) {
        // end location - red
        graphics.setColor(Color.red);
      }
      graphics.fillRect(xcoord * BLOCK_SIZE, ycoord * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Canvas#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics graphics) { // paints entire canvas
      graphics.setColor(Color.white);
      graphics.fillRect(0, 0, cols * BLOCK_SIZE, rows * BLOCK_SIZE);

      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          if (maze[y][x] == WHITE) {
            // location is clear
            graphics.setColor(Color.white);
          } else if (maze[y][x] == BLACK) {
            // location is a wall
            graphics.setColor(Color.black);
          } else if (maze[y][x] == YELLOW) {
            // path - yellow
            graphics.setColor(Color.yellow);
          } else if (maze[y][x] == GREEN) {
            // start location - green
            graphics.setColor(Color.green);
          } else if (maze[y][x] == RED) {
            // end location - red
            graphics.setColor(Color.red);
          }
          // draw the location
          graphics.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
      }
    }
  }
}
