package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class ShapevilleMainContent extends JPanel {

    public ShapevilleMainContent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255)); // 淡蓝背景

        // 欢迎标题区域
        add(Box.createVerticalStrut(20));
        JLabel welcomeIcon = new JLabel("\u2714", SwingConstants.CENTER); // ✔️ 作为图标
        welcomeIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        welcomeIcon.setAlignmentX(CENTER_ALIGNMENT);
        add(welcomeIcon);

        JLabel welcomeText = new JLabel("Welcome to Shapeville!");
        welcomeText.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeText.setAlignmentX(CENTER_ALIGNMENT);
        add(welcomeText);

        JLabel subtitleText = new JLabel("Let's explore shapes, angles, and geometry together!");
        subtitleText.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleText.setAlignmentX(CENTER_ALIGNMENT);
        add(subtitleText);

        // 进度条区域
        add(Box.createVerticalStrut(30));
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setOpaque(false);

        JLabel progressLabel = new JLabel("Your Progress");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(18);
        progressBar.setPreferredSize(new Dimension(400, 20));
        progressBar.setForeground(new Color(76, 175, 80));
        JLabel scoreLabel = new JLabel("18/100 points");

        progressPanel.add(progressLabel, BorderLayout.WEST);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(scoreLabel, BorderLayout.EAST);

        add(progressPanel);

        // 等级标题
        add(Box.createVerticalStrut(40));
        JLabel chooseLabel = new JLabel("Choose a Game Level");
        chooseLabel.setFont(new Font("Arial", Font.BOLD, 20));
        chooseLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(chooseLabel);

        // 等级选择卡片区域
        add(Box.createVerticalStrut(20));
        JPanel levelsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        levelsPanel.setOpaque(false);
        levelsPanel.setMaximumSize(new Dimension(900, 200));

        levelsPanel.add(createLevelCard("Level 1", "Shape Recognition", "Learn to identify basic shapes like circles, squares, triangles, and more!", "Beginner", new Color(76, 175, 80)));
        levelsPanel.add(createLevelCard("Level 2", "Angles & Lines", "Discover different types of angles and learn how to measure them!", "Intermediate", new Color(33, 150, 243)));
        levelsPanel.add(createLevelCard("Level 3", "Shape Properties", "Learn about sides, vertices, and special properties of different shapes!", "Advanced", new Color(156, 39, 176)));

        add(levelsPanel);
        add(Box.createVerticalStrut(20));
    }

    private JPanel createLevelCard(String level, String title, String description, String difficulty, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // 顶部色块
        JPanel header = new JPanel();
        header.setBackground(color);
        header.setMaximumSize(new Dimension(300, 40));
        JLabel levelLabel = new JLabel(level);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(levelLabel);
        card.add(header);

        // 标题
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);

        // 图形（可改为图标）
        JLabel icon = new JLabel("⬛⬜🔺", SwingConstants.CENTER);
        icon.setFont(new Font("Arial", Font.PLAIN, 24));
        icon.setAlignmentX(CENTER_ALIGNMENT);
        card.add(icon);

        // 描述
        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setAlignmentX(CENTER_ALIGNMENT);
        card.add(descLabel);

        // 难度 + 按钮
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setOpaque(false);
        bottom.add(new JLabel(difficulty));
        JButton startButton = new JButton("Start");
        startButton.setBackground(color);
        startButton.setForeground(Color.WHITE);
        bottom.add(startButton);
        card.add(bottom);

        return card;
    }
}
