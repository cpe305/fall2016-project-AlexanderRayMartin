package alexanderraymartin.ui;

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
  public Map(String fileName, double scale, int xoffset, int yoffset) {
    this.fileName = fileName;
    this.scale = scale;
    this.xoffset = xoffset;
    this.yoffset = yoffset;
  }

  /**
   * @Override.
   */
  public void paintComponent(Graphics graphics) {
    AffineTransform at = AffineTransform.getTranslateInstance(xoffset, yoffset);
    BufferedImage image = loadImage(fileName);
    at.scale(scale, scale);
    Graphics2D g2 = (Graphics2D) graphics;
    g2.drawImage(image, at, null);
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
