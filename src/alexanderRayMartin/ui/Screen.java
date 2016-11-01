package alexanderRayMartin.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import alexanderRayMartin.save.Save;
import alexanderRayMartin.save.Schedule;
import alexanderRayMartin.searchAlgorithm.Building;

public class Screen extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final int UI_WIDTH = 400;
    public static final int WIDTH = 1000 + UI_WIDTH;
    public static final int HEIGHT = 1000;

    public Map map;

    private JPanel uiPanel;
    private JPanel schedulePanel;
    private JPanel savePanel;

    private JButton addClass;
    private JButton save;

    private JComboBox<Building> selectClass;

    private JLabel scheduleLabel;

    private ButtonActionListener buttonListener;
    private MouseEventListener mouseListener;

    public Screen() {
        super("Poly Path");
        map = new Map("src/mapZoom.png", 1, 0, 0);
        mouseListener = new MouseEventListener();
        buttonListener = new ButtonActionListener();
        buttonListener = new ButtonActionListener();
        uiPanel = new JPanel();
        uiPanel.setPreferredSize(new Dimension(UI_WIDTH, HEIGHT));
        uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));
        addMouseListener(mouseListener);
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

    public void createInterface() {
        Font buttonFont = new Font("Courier", Font.BOLD, 13);
        Dimension buttonSize = new Dimension(125, 40);

        schedulePanel = new JPanel();
        savePanel = new JPanel();
        uiPanel.add(schedulePanel);
        uiPanel.add(savePanel);

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

        selectClass = new JComboBox<Building>(Building.buildings.toArray(new Building[Building.buildings.size()]));
        selectClass.setFont(buttonFont);
        selectClass.setMaximumRowCount(10);

        schedulePanel.add(selectClass);
        schedulePanel.add(addClass);

        savePanel.add(save);
    }

    private class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            Object source = event.getSource();

            if (source == addClass) {
                Schedule.getInstance().classes.add((Building) selectClass.getSelectedItem());
                System.out.println("Adding class: " + ((Building) selectClass.getSelectedItem()).buildingNumber + " "
                        + ((Building) selectClass.getSelectedItem()).name);

            } else if (source == save) {
                System.out.println("Saving...");
                Save.getInstance().saveSchedule();
            }

        }
    }

    private class MouseEventListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
