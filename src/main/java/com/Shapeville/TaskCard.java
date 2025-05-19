package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 * Reusable card component shown in the Stage-Switcher grid.
 * <p>
 * Each card advertises one geometry task: a coloured badge, title,
 * optional icon, two-line description, target age/level, and a coloured
 * “Start” button that the caller can wire to any {@link ActionListener}.
 * Layout uses a vertical BoxLayout so width is fixed (260 px) while
 * height stretches just enough to fit the supplied text and icon.
 * Background is white; a one-pixel darker border plus a theme-colour
 * header panel makes every card visually consistent with the task’s
 * difficulty colour (green, blue, purple, …).
 * Perfect for dropping into any container that uses standard Swing
 * layout managers.
 * <p>
 * Author : Lingyuan Li
 */
public class TaskCard extends JPanel {
    private JButton startButton;

    public TaskCard(String badge, String title, String description, String age, Color themeColor, ImageIcon icon) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(260, 230));

        // Badge
        JPanel badgePanel = new JPanel();
        badgePanel.setBackground(themeColor);
        badgePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        JLabel badgeLabel = new JLabel(badge);
        badgeLabel.setForeground(Color.WHITE);
        badgePanel.add(badgeLabel);
        add(badgePanel);

        // Title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(8));
        add(titleLabel);

        // Icon
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setAlignmentX(CENTER_ALIGNMENT);
            add(iconLabel);
        }

        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align:center;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(5));
        add(descLabel);

        // Bottom info
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(new JLabel(age), BorderLayout.WEST);

        // Start Button
        startButton = new JButton("Start");
        startButton.setBackground(themeColor);
        startButton.setForeground(Color.WHITE);
        footer.add(startButton, BorderLayout.EAST);

        add(Box.createVerticalStrut(10));
        add(footer);
    }

    // Add the Start button event listener
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }
}
