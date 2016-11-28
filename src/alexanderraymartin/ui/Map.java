package alexanderraymartin.ui;

import alexanderraymartin.searchalgorithm.Graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {

  private static final long serialVersionUID = 1L;

  private Graph graph;
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
  public void paintComponent(Graphics graphics) {
    AffineTransform at = AffineTransform.getTranslateInstance(xoffset, yoffset);
    BufferedImage image = loadImage(fileName);
    at.scale(scale, scale);
    Graphics2D g2 = (Graphics2D) graphics;
    int size = Screen.HEIGHT / graph.rows;

    // draw image
    g2.drawImage(image, at, null);

    // draw path
    for (int y = 0; y < graph.rows; y++) {
      for (int x = 0; x < graph.cols; x++) {
        // grid
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(x * size, y * size, size, size);

        if (graph.nodes[y][x] == 0) {
          g2.setColor(new Color(0xC93C3C));
          g2.fillRect((int) ((x + 0.25) * size), (int) ((y + 0.25) * size), size / 2, size / 2);
        }
      }
    }
  }

  private BufferedImage loadImage(String fileName) {
    BufferedImage image = null;

    try {
      image = ImageIO.read(new File(fileName));
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    return image;
  }

}
