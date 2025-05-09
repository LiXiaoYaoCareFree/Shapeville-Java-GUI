package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class TaskCard extends JPanel {
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
        JButton startBtn = new JButton("Start");
        startBtn.setBackground(themeColor);
        startBtn.setForeground(Color.WHITE);
        footer.add(startBtn, BorderLayout.EAST);

        // 添加事件监听器
        startBtn.addActionListener(e -> {
            // 当点击 "Start" 按钮时，打开 TaskScreen
            TaskScreen taskScreen = new TaskScreen();
            taskScreen.setVisible(true);  // 显示 TaskScreen 窗口
        });

        add(Box.createVerticalStrut(10));
        add(footer);
    }
}
