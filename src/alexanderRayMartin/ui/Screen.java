package alexanderRayMartin.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alexanderRayMartin.util.Mouse;

public class Screen extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1300;
	public static final int HEIGHT = 1000;

	public Map map;
	public Mouse mouse;

	private JPanel mainPanel;
	private JPanel schedulePanel;
	private JPanel savePanel;

	private JTextField classOneName;
	private JTextField classOneNumber;

	private JTextField classTwoName;
	private JTextField classTwoNumber;

	private JTextField classThreeName;
	private JTextField classThreeNumber;

	private JTextField classFourName;
	private JTextField classFourNumber;

	private JTextField classFiveName;
	private JTextField classFiveNumber;

	private JButton addClass;
	private JButton save;

	public Screen() {
		super("Poly Path");
		map = new Map("src/mapZoom.png", 1, 300, 0);
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
		init();
	}

	public void init() {
		mainPanel = new JPanel();
		schedulePanel = new JPanel();
		savePanel = new JPanel();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));

		addClass = new JButton("Add");
		save = new JButton("Save");

		mainPanel.add(schedulePanel);
		mainPanel.add(savePanel);

		schedulePanel.add(addClass);

		savePanel.add(save);

		mainPanel.add(schedulePanel, BorderLayout.LINE_START);
		mainPanel.add(savePanel, BorderLayout.PAGE_END);
		add(mainPanel);
	}

}
