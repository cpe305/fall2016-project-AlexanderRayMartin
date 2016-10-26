package alexanderRayMartin.main;

import java.awt.Dimension;

import javax.swing.JFrame;

import alexanderRayMartin.ui.Map;
import alexanderRayMartin.util.Mouse;

public class MapEditor extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1500;
	public static final int HEIGHT = WIDTH / 16 * 9;

	public Map map;
	public Mouse mouse;

	public MapEditor() {
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

	public void saveGrid() {

	}

	public void loadGrid() {

	}

	public static void main(String[] args) {

	}

}
