package com.Shapeville;

import javax.swing.*;
import java.awt.*;

/**
 * Central dashboard shown at the top of the centre column. Presents a friendly
 * welcome header, an illustrative tick icon and a live progress bar that turns
 * the six Shapeville mini-tasks into a single percentage and score read-out.
 * Every call to {@link #updateProgress()} increments an internal counter and
 * refreshes the bar; the last call forces 100 % so the user always sees full
 * completion. Static fields are used because each task window talks directly to
 * this panel without holding an instance reference.
 *
 * @author Lingyuan Li
 */
public class ShapevilleMainContent extends JPanel {

    /** Flags set by the individual TaskX screens. */
    public static int flag1, flag2, flag3, flag4, flag5, flag6;

    /** Shared widgets so tasks can update the UI from afar. */
    public static JProgressBar progressBar;
    public static JLabel       scoreLabel;

    private static int tasksCompleted = 0;
    private static final int TOTAL_TASKS = 6;

    /** Builds the header, icon, subtitle and progress bar. */
    public ShapevilleMainContent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255)); // light blue backdrop

        /* --- Welcome section ------------------------------------------------ */
        add(Box.createVerticalStrut(20));

        ImageIcon tick = new ImageIcon(getClass().getClassLoader().getResource("images/tick.png"));
        Image scaled  = tick.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel icon   = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
        icon.setAlignmentX(CENTER_ALIGNMENT);
        add(icon);

        JLabel title = new JLabel("Welcome to Shapeville!");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);

        JLabel subtitle = new JLabel("Let's explore shapes, angles, and geometry together!");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        add(subtitle);

        /* --- Progress section ---------------------------------------------- */
        add(Box.createVerticalStrut(30));
        JPanel barPanel = new JPanel(new BorderLayout());
        barPanel.setOpaque(false);

        JLabel label = new JLabel("Your Progress");
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(400, 20));
        progressBar.setForeground(new Color(76, 175, 80));
        scoreLabel  = new JLabel("0/100 points");

        barPanel.add(label, BorderLayout.WEST);
        barPanel.add(progressBar, BorderLayout.CENTER);
        barPanel.add(scoreLabel, BorderLayout.EAST);
        add(barPanel);
    }

    /**
     * Increments the <em>tasks completed</em> counter, recalculates the
     * percentage and refreshes the bar and score label.
     */
    public static void updateProgress() {
        tasksCompleted++;
        int percentage = (int) ((tasksCompleted * 100.0) / TOTAL_TASKS);
        if (tasksCompleted == TOTAL_TASKS) percentage = 100; // show full bar
        progressBar.setValue(percentage);
        scoreLabel.setText(percentage + "/100 points");
    }
}