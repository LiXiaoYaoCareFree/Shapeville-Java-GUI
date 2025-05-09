package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    // 添加 Start 按钮事件监听器
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }
}
