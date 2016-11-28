package alexanderraymartin.ui;

import alexanderraymartin.main.MapEditor;
import alexanderraymartin.save.Save;
import alexanderraymartin.save.Schedule;
import alexanderraymartin.searchalgorithm.Building;
import alexanderraymartin.searchalgorithm.Graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Screen extends JFrame {

  private static final long serialVersionUID = 1L;

  public static final int UI_WIDTH = 400;
  public static final int WIDTH = 1000 + UI_WIDTH;
  public static final int HEIGHT = 1000;
  public static final int NODE_SIZE = 20;

  public Map map;
  private Graph graph;

  private JLabel scheduleLabel;
  private JPanel uiPanel;
  private JPanel schedulePanel;

  private JPanel classPanel;
  private JPanel removeButtonPanel;
  private JPanel classNamePanel;

  private JButton addClass;
  private JButton save;
  private JButton findPath;

  private JButton editorSave;

  private JComboBox<Building> selectClass;

  private ArrayList<JButton> removeClassButtons;
  private ArrayList<JLabel> classLabels;

  private ButtonActionListener buttonListener;
  private MouseEventListener mouseListener;

  /**
   * Creates the window.
   */
  public Screen(Graph graph) {
    super("Poly Path");
    this.graph = graph;
    graph.makeGraph();
    map = new Map("src/mapZoom.png", 1, 0, 0, graph);
    mouseListener = new MouseEventListener();
    buttonListener = new ButtonActionListener();
    buttonListener = new ButtonActionListener();
    uiPanel = new JPanel();
    uiPanel.setPreferredSize(new Dimension(UI_WIDTH, HEIGHT));
    uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));
    add(uiPanel, BorderLayout.WEST);
    add(map);
    createInterface();
    setVisible(true);
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Creates the interface.
   */
  public void createInterface() {
    final Font buttonFont = new Font("Courier", Font.PLAIN, 20);
    final Dimension buttonSize = new Dimension(175, 50);

    removeClassButtons = new ArrayList<JButton>();
    classLabels = new ArrayList<JLabel>();

    schedulePanel = new JPanel();
    classPanel = new JPanel();
    classPanel.setLayout(new BoxLayout(classPanel, BoxLayout.X_AXIS));
    removeButtonPanel = new JPanel();
    removeButtonPanel.setLayout(new BoxLayout(removeButtonPanel, BoxLayout.Y_AXIS));

    classNamePanel = new JPanel();
    classNamePanel.setLayout(new BoxLayout(classNamePanel, BoxLayout.Y_AXIS));

    uiPanel.add(schedulePanel);
    uiPanel.add(classPanel);
    classPanel.add(removeButtonPanel);
    classPanel.add(classNamePanel);

    scheduleLabel = new JLabel("Schedule");
    scheduleLabel.setFont(new Font("Courier", Font.BOLD, 50));
    schedulePanel.add(scheduleLabel);

    addClass = new JButton("Add");
    addClass.setFont(buttonFont);
    addClass.setPreferredSize(buttonSize);
    addClass.addActionListener(buttonListener);

    save = new JButton("Save");
    save.setFont(buttonFont);
    save.setPreferredSize(buttonSize);
    save.addActionListener(buttonListener);

    findPath = new JButton("Find Path");
    findPath.setFont(buttonFont);
    findPath.setPreferredSize(buttonSize);
    findPath.addActionListener(buttonListener);

    selectClass = new JComboBox<Building>(
        Building.buildings.toArray(new Building[Building.buildings.size()]));
    selectClass.setFont(buttonFont);
    selectClass.setMaximumRowCount(10);

    schedulePanel.add(selectClass);
    schedulePanel.add(addClass);
    schedulePanel.add(save);
    if (!MapEditor.editMode) {
      schedulePanel.add(findPath);
    }

    for (int i = 0; i < Schedule.getInstance().classes.size(); i++) {
      addClass(i);
    }

  }

  /**
   * Creates the interface for editor mode.
   */
  public void createEditorInterface() {
    final Font buttonFont = new Font("Courier", Font.PLAIN, 20);
    final Dimension buttonSize = new Dimension(175, 50);
    editorSave = new JButton("Export");
    editorSave.setFont(buttonFont);
    editorSave.setPreferredSize(buttonSize);
    editorSave.addActionListener(buttonListener);
    schedulePanel.add(editorSave);
    map.addMouseListener(mouseListener);
    drawBuildings();
    pack();
  }

  private void addClass(int index) {
    Font buttonFont = new Font("Courier", Font.PLAIN, 20);
    Dimension buttonSize = new Dimension(125, 20);

    removeClassButtons.add(new JButton("X"));
    removeClassButtons.get(index).setFont(buttonFont);
    removeClassButtons.get(index).setPreferredSize(buttonSize);
    removeClassButtons.get(index).addActionListener(buttonListener);
    removeButtonPanel.add(removeClassButtons.get(index));

    classLabels.add(new JLabel(Schedule.getInstance().classes.get(index).toString()));
    classLabels.get(index).setFont(buttonFont);
    classNamePanel.add(classLabels.get(index));

    pack();
  }

  private void removeClass(int index) {
    Schedule.getInstance().classes.remove(index);

    removeClassButtons.get(index).removeActionListener(buttonListener);
    removeClassButtons.remove(index);
    removeButtonPanel.remove(index);

    classLabels.remove(index);
    classNamePanel.remove(index);

    pack();
  }

  /**
   * Checks if the mouse click is in the boundary.
   * 
   * @param ycoord The y coordinate of the mouse.
   * @param xcoord The x coordinate of the mouse.
   * @return True if in bounds, else false.
   */
  private boolean inBoundary(int ycoord, int xcoord) {
    if (ycoord < 0 || ycoord >= graph.rows || xcoord < 0 || xcoord >= graph.cols) {
      return false;
    }
    return true;
  }

  /**
   * Place a node at the mouse click.
   * 
   * @param ycoord The y coordinate of the mouse.
   * @param xcoord The x coordinate of the mouse.
   */
  private void placeLocations(int ycoord, int xcoord) {
    if (inBoundary(ycoord, xcoord)) {
      if (graph.nodes[ycoord][xcoord] == Map.BLOCKED) {
        graph.nodes[ycoord][xcoord] = Map.AVAILABLE_PATH;
      } else if (graph.nodes[ycoord][xcoord] == Map.AVAILABLE_PATH) {
        graph.nodes[ycoord][xcoord] = Map.BLOCKED;
      }
    }
  }

  /**
   * Find the path.
   */
  private void findPath(int start, int end) {
    int[] path = graph.getPath(start, end);
    Graph.getSchedulePaths().add(path);
    for (int i = 0; i < path.length; i++) {
      System.out.println(path[i]);
    }
    repaint();
    System.out.println("Done!");
  }

  private void drawBuildings() {
    for (int i = 0; i < Building.getBuildings().size(); i++) {
      int xcoord = Building.getBuildings().get(i).nodeNumber % graph.cols;
      int ycoord = Building.getBuildings().get(i).nodeNumber / graph.cols;
      graph.nodes[ycoord][xcoord] = Map.BUILDING;
    }
  }

  private class ButtonActionListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      Object source = event.getSource();

      if (source == addClass) {
        Schedule.getInstance().classes.add((Building) selectClass.getSelectedItem());
        addClass(Schedule.getInstance().classes.size() - 1);
        System.out
            .println("Adding class: " + ((Building) selectClass.getSelectedItem()).buildingNumber
                + " " + ((Building) selectClass.getSelectedItem()).name);

      } else if (source == save) {
        System.out.println("Saving...");
        Save.getInstance().saveSchedule();
      } else if (removeClassButtons.contains(source)) {
        removeClass(removeClassButtons.indexOf(source));
      } else if (source == editorSave) {
        graph.saveNodes();
      } else if (source == findPath) {
        // clear old paths
        Graph.clearSchedulePaths();
        for (int i = 0; i < Schedule.getInstance().classes.size() - 1; i++) {
          int classOne = Schedule.getInstance().classes.get(i).nodeNumber;
          int classTwo = Schedule.getInstance().classes.get(i + 1).nodeNumber;
          Graph.getBuildingNodes().add(classOne);
          Graph.getBuildingNodes().add(classTwo);
          findPath(classOne, classTwo);
        }
      }

    }
  }

  private class MouseEventListener implements MouseListener {
    public void mouseClicked(MouseEvent event) {}

    public void mousePressed(MouseEvent event) {
      int startX = event.getX();
      int startY = event.getY();
      int xcoord = (int) (startX / NODE_SIZE);
      int ycoord = (int) (startY / NODE_SIZE);
      System.out.println(ycoord * graph.cols + xcoord);
      placeLocations(ycoord, xcoord);
      repaint();
    }

    public void mouseReleased(MouseEvent event) {}

    public void mouseEntered(MouseEvent event) {}

    public void mouseExited(MouseEvent event) {}
  }
}
