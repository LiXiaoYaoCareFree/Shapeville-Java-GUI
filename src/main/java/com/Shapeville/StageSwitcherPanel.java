package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Two-stage navigator embedded below the main dashboard. A pair of toggle
 * buttons (Key&nbsp;Stage&nbsp;1 and Key&nbsp;Stage&nbsp;2) drive a
 * {@link CardLayout} that swaps the corresponding grid of {@link TaskCard}s.
 * Each task button spawns its own <code>TaskXScreen</code> window; while that
 * window is open the owner frame is temporarily disabled and re-enabled on
 * close, preventing simultaneous interaction. Basic colour decoration marks
 * the active stage.
 *
 * <p>Usage: the panel is self-contained and exposes no public API besides its
 * constructor.</p>
 *
 * @author Lingyuan Li
 */
public class StageSwitcherPanel extends JPanel {

    /** CardLayout manager for switching between different stages */
    private final CardLayout cardLayout;
    /** Container panel that holds the stage content cards */
    private final JPanel     cardContainer;

    public static TaskCard task1;
    public static TaskCard task2;
    public static TaskCard task3;
    public static TaskCard task4;
    public static TaskCard task5;
    public static TaskCard task6;

    /**
     * Constructs a new StageSwitcherPanel with two stages and their respective task cards.
     * Initializes the UI components, sets up the layout, and configures the stage switching logic.
     */
    public StageSwitcherPanel() {
        setLayout(new BorderLayout());

        /* ----------- Stage selector buttons ------------------------------- */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JToggleButton ks1 = new JToggleButton("<html><b>Key Stage 1</b><br>(Years 1–2)</html>");
        JToggleButton ks2 = new JToggleButton("<html><b>Key Stage 2</b><br>(Years 3–4)</html>");

        ButtonGroup group = new ButtonGroup();
        group.add(ks1);
        group.add(ks2);
        ks1.setSelected(true);
        decorateButton(ks1, true);
        decorateButton(ks2, false);
        buttonPanel.add(ks1);
        buttonPanel.add(ks2);
        add(buttonPanel, BorderLayout.NORTH);

        /* ----------- Content cards --------------------------------------- */
        cardLayout     = new CardLayout();
        cardContainer  = new JPanel(cardLayout);
        cardContainer.add(createStage1(), "KS1");
        cardContainer.add(createStage2(), "KS2");
        add(cardContainer, BorderLayout.CENTER);

        /* ----------- Listeners ------------------------------------------- */
        ks1.addActionListener(e -> {
            cardLayout.show(cardContainer, "KS1");
            decorateButton(ks1, true);
            decorateButton(ks2, false);
        });
        ks2.addActionListener(e -> {
            cardLayout.show(cardContainer, "KS2");
            decorateButton(ks1, false);
            decorateButton(ks2, true);
        });
    }

    /**
     * Applies visual styling to a toggle button based on its active state.
     * @param btn the toggle button to decorate
     * @param active true if the button is selected, false otherwise
     */
    private void decorateButton(JToggleButton btn, boolean active) {
        if (active) {
            btn.setBackground(new Color(66, 133, 244));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createMatteBorder(0, 0, 8, 0, new Color(66, 133, 244)));
        } else {
            btn.setBackground(new Color(230, 230, 230));
            btn.setForeground(Color.DARK_GRAY);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
    }

    /* ----------------------------- Stage builders ------------------------ */

    /**
     * Creates and configures the panel for Key Stage 1 tasks.
     * @return a JPanel containing Task 1 and Task 2 cards
     */
    private JPanel createStage1() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(240, 250, 255));

        task1 = new TaskCard("Task 1", "Shape Identification",
                "Identify basic 2-D shapes: circles, squares, triangles…", "Ages 5–7",
                new Color(76, 175, 80), loadIcon("shapes.png"));
        task1.addStartButtonListener(e -> startTask1());

        task2 = new TaskCard("Task 2", "Angle Types",
                "Recognise right, acute and obtuse angles.", "Ages 5–7",
                new Color(76, 175, 80), loadIcon("angles.png"));
        task2.addStartButtonListener(e -> startTask2());

        panel.add(task1);
        panel.add(task2);
        return panel;
    }

    /**
     * Creates and configures the panel for Key Stage 2 tasks.
     * @return a JPanel containing Task 3, Task 4, and two challenge tasks
     */
    private JPanel createStage2() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 250, 255));

        JPanel row1 = new JPanel(new FlowLayout());
        row1.setOpaque(false);
        task3 = new TaskCard("Task 3", "Area of Shapes",
                "Calculate area of rectangles, triangles and more.", "Ages 7–10",
                new Color(33, 150, 243), loadIcon("area.png"));
        task3.addStartButtonListener(e -> startTask3());

        task4 = new TaskCard("Task 4", "Circle Area & Circumference",
                "Use π to find area and circumference of circles.", "Ages 7–10",
                new Color(33, 150, 243), loadIcon("Circle.png"));
        task4.addStartButtonListener(e -> startTask4());

        row1.add(task3);
        row1.add(task4);

        JPanel row2 = new JPanel(new FlowLayout());
        row2.setOpaque(false);
        task5 = new TaskCard("Challenge 1", "Compound Shapes",
                "Break compound shapes into simpler ones to get area.", "Advanced",
                new Color(156, 39, 176), loadIcon("compound.png"));
        task5.addStartButtonListener(e -> startTask5());

        task6 = new TaskCard("Challenge 2", "Sector Areas & Arcs",
                "Find area of sectors and length of arcs.", "Advanced",
                new Color(156, 39, 176), loadIcon("sectors.png"));
        task6.addStartButtonListener(e -> startTask6());

        row2.add(task5);
        row2.add(task6);

        panel.add(row1);
        panel.add(row2);
        return panel;
    }

    /**
     * Loads and scales an icon from the classpath resources.
     * @param name the filename of the icon to load
     * @return a scaled ImageIcon, or null if loading fails
     */
    private ImageIcon loadIcon(String name) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getClassLoader().getResource("images/" + name));
            Image img = raw.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    /* --------------------- Task launching & modal logic ------------------ */

    /**
     * Configures a task window to behave modally by disabling the owner window.
     * @param taskWindow the window to configure
     */
    private void configureTaskWindow(JFrame taskWindow) {
        taskWindow.setAlwaysOnTop(true);
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (owner instanceof Frame) {
            final Frame ownerFrame = (Frame) owner;
            ownerFrame.setEnabled(false);

            taskWindow.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e) { enableOwnerWindow(ownerFrame); }
                @Override public void windowClosing(WindowEvent e) { enableOwnerWindow(ownerFrame); }
            });

            new Timer(100, e -> {
                if (!taskWindow.isVisible() || !taskWindow.isDisplayable()) {
                    ((Timer) e.getSource()).stop();
                    enableOwnerWindow(ownerFrame);
                }
            }).start();
        }
    }

    /**
     * Re-enables the owner window and brings it to the front.
     * @param ownerFrame the frame to re-enable
     */
    private void enableOwnerWindow(Frame ownerFrame) {
        SwingUtilities.invokeLater(() -> {
            ownerFrame.setEnabled(true);
            ownerFrame.requestFocus();
            ownerFrame.toFront();
        });
    }

    /* ----------------------------- Task starters ------------------------- */

    /**
     * Launches Task 1 screen for shape identification.
     */
    public void startTask1() { Task1Screen s = new Task1Screen(); configureTaskWindow(s); s.setVisible(true); }

    /**
     * Launches Task 2 screen for angle types.
     */
    public void startTask2() { Task2Screen s = new Task2Screen(); configureTaskWindow(s); s.setVisible(true); }

    /**
     * Launches Task 3 screen for area calculations.
     */
    public void startTask3() { Task3Screen s = new Task3Screen(); configureTaskWindow(s); s.setVisible(true); }

    /**
     * Launches Task 4 screen for circle measurements.
     */
    public void startTask4() { Task4Screen s = new Task4Screen(); configureTaskWindow(s); s.setVisible(true); }

    /**
     * Launches Challenge 1 screen for compound shapes.
     */
    public void startTask5() { Task5Screen s = new Task5Screen(); configureTaskWindow(s); s.setVisible(true); }

    /**
     * Launches Challenge 2 screen for sector areas and arcs.
     */
    public void startTask6() { Task6Screen s = new Task6Screen(); configureTaskWindow(s); s.setVisible(true); }
}