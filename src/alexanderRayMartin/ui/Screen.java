package alexanderRayMartin.ui;

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

import alexanderRayMartin.save.Save;
import alexanderRayMartin.save.Schedule;
import alexanderRayMartin.searchAlgorithm.Building;

public class Screen extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final int UI_WIDTH = 400;
    public static final int WIDTH = 1000 + UI_WIDTH;
    public static final int HEIGHT = 1000;

    public Map map;

    private JLabel scheduleLabel;
    private JPanel uiPanel;
    private JPanel schedulePanel;

    private JPanel classPanel;
    private JPanel removeButtonPanel;
    private JPanel classNamePanel;

    private JButton addClass;
    private JButton save;

    private JComboBox<Building> selectClass;

    private ArrayList<JButton> removeClassButtons;
    private ArrayList<JLabel> classLabels;

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

        selectClass = new JComboBox<Building>(Building.buildings.toArray(new Building[Building.buildings.size()]));
        selectClass.setFont(buttonFont);
        selectClass.setMaximumRowCount(10);

        schedulePanel.add(selectClass);
        schedulePanel.add(addClass);
        schedulePanel.add(save);

        for (int i = 0; i < Schedule.getInstance().classes.size(); i++) {
            addClass(i);
        }

    }

    private void addClass(int index) {
        Font buttonFont = new Font("Courier", Font.PLAIN, 20);
        Dimension buttonSize = new Dimension(125, 20);

        removeClassButtons.add(new JButton("X"));
        removeClassButtons.get(index).setFont(buttonFont);
        removeClassButtons.get(index).setPreferredSize(buttonSize);
        removeClassButtons.get(index).addActionListener(buttonListener);
        removeButtonPanel.add(removeClassButtons.get(index));

        classLabels.add(new JLabel(Schedule.getInstance().classes.get(index).abreviateString(25)));
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

    private class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            Object source = event.getSource();

            if (source == addClass) {
                Schedule.getInstance().classes.add((Building) selectClass.getSelectedItem());
                addClass(Schedule.getInstance().classes.size() - 1);
                System.out.println("Adding class: " + ((Building) selectClass.getSelectedItem()).buildingNumber + " "
                        + ((Building) selectClass.getSelectedItem()).name);

            } else if (source == save) {
                System.out.println("Saving...");
                Save.getInstance().saveSchedule();
            } else if (removeClassButtons.contains(source)) {
                removeClass(removeClassButtons.indexOf(source));
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
