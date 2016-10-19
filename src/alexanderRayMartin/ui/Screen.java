package alexanderRayMartin.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Screen {

	public static final int WIDTH = 1000;
	public static final int HEIGHT = WIDTH / 16 * 9;

	public JFrame frame;

	public Screen() {
		frame = new JFrame("Poly Path");
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

}
