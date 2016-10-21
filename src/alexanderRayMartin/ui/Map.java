package alexanderRayMartin.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;
	private int scale;
	private int xOffset, yOffset;

	public Map(String fileName, int scale, int xOffset, int yOffset) {
		this.fileName = fileName;
		this.scale = scale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void paintComponent(Graphics g) {
		AffineTransform at = AffineTransform.getTranslateInstance(xOffset, yOffset);
		BufferedImage image = loadImage(fileName);
		at.scale(scale, scale);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, at, null);
	}

	private BufferedImage loadImage(String fileName) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

}
