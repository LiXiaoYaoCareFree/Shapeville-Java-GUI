package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class ShapevilleMainContent extends JPanel {

    public ShapevilleMainContent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255)); // 淡蓝背景


        // 欢迎标题区域
        add(Box.createVerticalStrut(20));

        // 加载图片图标
        ImageIcon welcomeIcon = new ImageIcon(getClass().getClassLoader().getResource("images/tick.png"));

        // 设置图片图标为指定大小（可选，视情况而定）
        Image img = welcomeIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 调整为 100x100
        welcomeIcon = new ImageIcon(img);

        // 创建 JLabel 用于显示图片
        JLabel welcomeIconLabel = new JLabel(welcomeIcon, SwingConstants.CENTER);
        welcomeIconLabel.setAlignmentX(CENTER_ALIGNMENT);  // 让图片居中

        add(welcomeIconLabel);

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
    }
}
