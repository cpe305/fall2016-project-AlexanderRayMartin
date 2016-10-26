package alexanderRayMartin.main;

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

import alexanderRayMartin.searchAlgorithm.Graph;

public class Maze extends Applet {

	private static final long serialVersionUID = 1L;

	// array that holds the maze info
	private int[][] maze;

	// graph that holds the edges
	private Graph graph;

	// number of rows and columns in the maze
	private int rows, cols;

	private int currentLocation, stopLocation;

	// booleans for keeping track of certain states
	private boolean needMaze;
	private boolean needLocations;
	private boolean inTestMode;
	private boolean victory;

	// initial size of maze - if bigger may go off window
	private final int MAXROWS = 20;
	private final int MAXCOLS = 30;
	private final int WIN_AMOUNT = 8;
	// size of each block in pixels
	private final int blkSize = 20;

	// color numbers for file
	private final int black = -1;
	private final int white = 0;
	private final int yellow = 1;
	private final int green = 2;
	private final int red = 3;

	private final String messageTip = "TIP: 'Test Mode' allows you to replace the start and end blocks using the left and right mouse buttons!";
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
	private JRadioButton radioButton1, radioButton2;
	private ButtonGroup radioButtonGroup;

	// this listener object responds to button events
	private ButtonActionListener buttonListener;
	private MouseEventListener mouseListener;

	private enum State {
		DISPLAY_SHORTEST_PATH, ADVANCE, ADD_BLOCK, NEED_START, NEED_END
	}

	// holds the current state of the path
	private State pathState;
	private State location;

	// this method sets up the canvas and starts it off
	public void init() {
		System.out.println("Maze started"); // goes to console
		inTestMode = false;
		needMaze = true;
		needLocations = true;
		victory = false;
		mouseListener = new MouseEventListener();
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
		mazeField.addMouseListener(mouseListener);

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

	private void advanceLocation() {
		if (getMessage() == -1)
			return; // update message and return if no path
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

				float random = (float) Math.random(); // random number between 0
														// and 1
				int index = Math.round((shortestPath.length - 1) * random);
				int location = shortestPath[index];
				if (shortestPath[index] == stopLocation)
					location = shortestPath[index - 1]; // upper bound
				if (shortestPath[index] == currentLocation)
					location = shortestPath[index + 1]; // lower bound
				replaceBlock(location, black); // add block in path
				pathState = State.DISPLAY_SHORTEST_PATH;
				removeEdges(location); // updates the graph
				break;
			default:
				break;

			}
		}
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

	private void replaceBlock(int location, int color) { // replaces block with
															// certain color
		int y = location / cols;
		int x = location % cols;
		maze[y][x] = color;
		mazeField.paintSingle(y, x, mazeField.getGraphics()); // paints new
																// block
	}

	private void removeColor(int color) { // removes all of a certain color from
											// array
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				if (maze[y][x] == color) {
					maze[y][x] = white;
					mazeField.paintSingle(y, x, mazeField.getGraphics()); // paints
																			// new
																			// block
				}
			}
		}
	}

	private void placeColor(int y, int x, int color) { // places a single color
		removeColor(color);
		maze[y][x] = color;
		mazeField.paintSingle(y, x, mazeField.getGraphics()); // paints new
																// block
	}

	private void drawPath(int[] shortestPath) { // draw the shortest path
		if (!needLocations) {
			if (!needLocations && getMessage() != -1) {
				for (int i = 0; i < shortestPath.length - 1; i++) {
					if (shortestPath[i] != currentLocation && shortestPath[i] != stopLocation) {
						replaceBlock(shortestPath[i], yellow);
					}
				}
			}
		}
	}

	private void testMode(int y, int x, MouseEvent e) { // test mode for
														// debugging
		// I left this mode in the final product because it's fun to use!
		if (!needMaze && inBoundary(y, x)) {
			if (maze[y][x] == white || maze[y][x] == yellow) {
				pathState = State.ADVANCE;
				if (e.getButton() == MouseEvent.BUTTON1) {
					placeColor(y, x, green);
					currentLocation = y * cols + x;
				} else {
					placeColor(y, x, red);
					stopLocation = y * cols + x;
					needLocations = false;
				}
				removeColor(yellow);
				drawPath(graph.getPath(currentLocation, stopLocation));
			}
		}
	}

	private void placeLocations(int y, int x) { // places start and end
												// locations
		if (!needMaze && needLocations && inBoundary(y, x)) {
			if (maze[y][x] == white) {
				switch (location) {
				case NEED_START:
					placeColor(y, x, green);
					currentLocation = y * cols + x;
					location = State.NEED_END;
					break;
				case NEED_END:
					placeColor(y, x, red);
					stopLocation = y * cols + x;
					location = State.NEED_START;
					needLocations = false;
				default:
					break;
				}
			}
		}
	}

	private void removeEdges(int location) { // removes old edges
		int y = location / cols;
		int x = location % cols;
		graph.removeEdge((y - 1) * cols + x, y * cols + x); // above
		graph.removeEdge((y + 1) * cols + x, y * cols + x); // below
		graph.removeEdge(y * cols + (x - 1), y * cols + x); // left
		graph.removeEdge(y * cols + (x + 1), y * cols + x); // right
	}

	private boolean inBoundary(int y, int x) { // return true if valid area
		if (y < 0 || y >= rows || x < 0 || x >= cols) {
			return false;
		}
		return true;
	}

	private void checkEdge(int y, int x, int y0, int x0) { // checks for
															// adjacency
		if (inBoundary(y, x)) { // check boundary
			if (maze[y][x] != black) {
				graph.addEdge(y0 * cols + x0, y * cols + x);
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
			mazeField.paint(mazeField.getGraphics());
			needMaze = false;
			needLocations = true;
			scanner.close();
			makeGraph(); // make the graph
			return true;
		} catch (Exception e) {
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
	}

	private class MouseEventListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {

			if (radioButton1.isSelected()) {
				inTestMode = false;
			} else if (radioButton2.isSelected()) {
				inTestMode = true;
			}

			// location on the mazeCanvas where mouse was clicked
			// upper-left is (0,0)
			int startX = e.getX();
			int startY = e.getY();
			int x = (int) (startX / blkSize);
			int y = (int) (startY / blkSize);

			if (inTestMode)
				testMode(y, x, e); // test mode
			else if (needLocations)
				placeLocations(y, x); // standard mode

		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	class MazeCanvas extends Canvas {
		// this class paints the output window

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// the constructor sets it up
		MazeCanvas() {
			rows = MAXROWS;
			cols = MAXCOLS;
			maze = new int[MAXROWS][MAXCOLS];
			setSize(cols * blkSize, rows * blkSize);
			setBackground(Color.white);
		}

		public void paintSingle(int y, int x, Graphics g) {
			paintSingle(y * cols + x, g);

		}

		public void paintSingle(int location, Graphics g) { // paints a single
															// location
			int y = location / cols;
			int x = location % cols;
			if (maze[y][x] == white) {
				// location is clear
				g.setColor(Color.white);
			} else if (maze[y][x] == black) {
				// location is a wall
				g.setColor(Color.black);
			} else if (maze[y][x] == yellow) {
				// path - yellow
				g.setColor(Color.yellow);
			} else if (maze[y][x] == green) {
				// start location - green
				g.setColor(Color.green);
			} else if (maze[y][x] == red) {
				// end location - red
				g.setColor(Color.red);
			}
			g.fillRect(x * blkSize, y * blkSize, blkSize, blkSize);
		}

		public void paint(Graphics g) { // paints entire canvas
			g.setColor(Color.white);
			g.fillRect(0, 0, cols * blkSize, rows * blkSize);

			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					if (maze[y][x] == white) {
						// location is clear
						g.setColor(Color.white);
					} else if (maze[y][x] == black) {
						// location is a wall
						g.setColor(Color.black);
					} else if (maze[y][x] == yellow) {
						// path - yellow
						g.setColor(Color.yellow);
					} else if (maze[y][x] == green) {
						// start location - green
						g.setColor(Color.green);
					} else if (maze[y][x] == red) {
						// end location - red
						g.setColor(Color.red);
					}
					// draw the location
					g.fillRect(x * blkSize, y * blkSize, blkSize, blkSize);
				}
			}
		}
	}
}