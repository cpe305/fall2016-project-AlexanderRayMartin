package alexanderraymartin.ui;

import alexanderraymartin.main.Main;
import alexanderraymartin.main.MapEditor;
import alexanderraymartin.searchalgorithm.Graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * @author Alex Martin.
 *
 */
public class Map extends JPanel {

  private static final long serialVersionUID = 1L;

  /**
   * Path is blocked.
   */
  public static final int BLOCKED = -1;
  /**
   * Path is available.
   */
  public static final int AVAILABLE_PATH = 0;
  /**
   * Building.
   */
  public static final int BUILDING = 1;

  private transient Graph graph;
  private String fileName;
  private double scale;
  private int xoffset;
  private int yoffset;

  /**
   * @param fileName The file name.
   * @param scale The amount to scale the image.
   * @param xoffset The offset along the x axis.
   * @param yoffset The offset along the y axis.
   */
  public Map(String fileName, double scale, int xoffset, int yoffset, Graph graph) {
    this.fileName = fileName;
    this.scale = scale;
    this.xoffset = xoffset;
    this.yoffset = yoffset;
    this.graph = graph;
  }

  /**
   * @Override.
   */
  @Override
  public void paintComponent(Graphics graphics) {
    AffineTransform at = AffineTransform.getTranslateInstance(xoffset, yoffset);
    BufferedImage image = loadImage(fileName);
    at.scale(scale, scale);
    Graphics2D g2 = (Graphics2D) graphics;
    int size = Screen.HEIGHT / graph.getRows();

    // draw image
    g2.drawImage(image, at, null);

    // draw path
    if (MapEditor.inEditMode()) {
      for (int y = 0; y < graph.getRows(); y++) {
        for (int x = 0; x < graph.getCols(); x++) {
          // grid
          g2.setColor(Color.LIGHT_GRAY);
          g2.drawRect(x * size, y * size, size, size);

          if (graph.getNodes(x, y) == AVAILABLE_PATH) {
            drawSquare(graphics, new Color(0xC93C3C), x, y);
          }

          if (graph.getNodes(x, y) == BUILDING) {
            drawSquare(graphics, new Color(0x155b84), x, y);
          }
        }
      }
    }

    // if node in path, draw node to create path
    for (int i = 0; i < Graph.getSchedulePaths().size(); i++) {
      for (int j = 0; j < Graph.getSchedulePaths().get(i).length; j++) {
        if (Graph.getSchedulePaths().get(i)[j] != 0) {
          int xcoord = Graph.getSchedulePaths().get(i)[j] % graph.getCols();
          int ycoord = Graph.getSchedulePaths().get(i)[j] / graph.getCols();
          Color color = new Color(0xc65555);
          drawSquare(graphics, color, xcoord, ycoord);
        }
      }
    }

    // draw buildings
    for (int i = 0; i < Graph.getBuildingNodes().size(); i++) {
      int xcoord = Graph.getBuildingNodes().get(i) % graph.getCols();
      int ycoord = Graph.getBuildingNodes().get(i) / graph.getCols();
      drawSquare(graphics, new Color(0x0000ff << (i * 5)), xcoord, ycoord);
    }
  }

  /**
   * @param graphics The graphics to draw on.
   * @param color The square's color.
   * @param xcoord The x coordinate.
   * @param ycoord The y coordinate.
   */
  public void drawSquare(Graphics graphics, Color color, int xcoord, int ycoord) {
    Graphics2D g2 = (Graphics2D) graphics;
    int size = Screen.HEIGHT / graph.getRows();
    g2.setColor(color);
    g2.fillRect((int) ((xcoord + 0.25) * size), (int) ((ycoord + 0.25) * size), size / 2, size / 2);
  }

  private BufferedImage loadImage(String fileName) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
    } catch (IOException exception) {
      Main.getLogger().fine(exception.toString());
      exception.printStackTrace();
    }

    return image;
  }

}
