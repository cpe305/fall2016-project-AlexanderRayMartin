package alexanderRayMartin.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

import alexanderRayMartin.util.Mouse;

public class Screen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1500;
	public static final int HEIGHT = WIDTH / 16 * 9;

	public Map map;
	public Mouse mouse;

	public Screen() {
		super("Poly Path");
		map = new Map("src/map.png", 1, 0, 0);
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		add(map);
		setVisible(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

}
