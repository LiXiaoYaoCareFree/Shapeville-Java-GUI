package com.xiaoyao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Window;

public class TopPanel extends JPanel {

    public TopPanel() {
        // 创建自定义面板，绘制渐变色背景
        JPanel topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(63, 81, 181), getWidth(), 0, new Color(156, 39, 176));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        topPanel.setLayout(new BorderLayout());

        // 加载并调整 "Shapeville" 按钮的图标
        ImageIcon rawIcon = new ImageIcon(getClass().getClassLoader().getResource("images/img.png"));
        Image scaledImage = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // 调整大小为 30x30
        ImageIcon icon = new ImageIcon(scaledImage); // 创建新的 ImageIcon 对象

        // 创建带图标和文字的 JLabel
        JLabel logoLabel = new JLabel("Shapeville", icon, JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setHorizontalTextPosition(SwingConstants.RIGHT);  // 文字在图标右边
        logoLabel.setVerticalTextPosition(SwingConstants.CENTER);    // 垂直居中

        // 添加到左侧面板
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);  // 背景透明
        leftPanel.add(logoLabel);
        leftPanel.setBorder(new EmptyBorder(5, 20, 0, 0)); // 向右移动 20 像素

        // 创建右侧的按钮面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(0, 0, 0, 0));  // 设置左侧面板背景透明
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // 加载并调整 "Home" 按钮的图标
        ImageIcon homeIcon = new ImageIcon(getClass().getClassLoader().getResource("images/home.png"));
        Image homeImage = homeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // 调整大小为 30x30
        homeIcon = new ImageIcon(homeImage); // 创建新的 ImageIcon 对象

        // 创建 "Home" 按钮
        JButton homeButton = new JButton("Home");
        homeButton.setBackground(Color.WHITE);
        homeButton.setForeground(Color.BLUE);
        homeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        homeButton.setIcon(homeIcon);
        homeButton.setHorizontalTextPosition(SwingConstants.RIGHT); // 设置图标在文本的左边

        // 加载并调整 "End Session" 按钮的图标
        ImageIcon endSessionIcon = new ImageIcon(getClass().getClassLoader().getResource("images/logout.png"));
        Image endSessionImage = endSessionIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // 调整大小为 30x30
        endSessionIcon = new ImageIcon(endSessionImage); // 创建新的 ImageIcon 对象

        // 创建 "End Session" 按钮
        JButton endSessionButton = new JButton("End Session");
        endSessionButton.setBackground(Color.WHITE);
        endSessionButton.setForeground(Color.RED);
        endSessionButton.setFont(new Font("Arial", Font.PLAIN, 14));
        endSessionButton.setIcon(endSessionIcon);
        endSessionButton.setHorizontalTextPosition(SwingConstants.RIGHT); // 设置图标在文本的左边

        // 将按钮添加到右侧面板
        rightPanel.add(homeButton);
        rightPanel.add(endSessionButton);

        // 将左右面板添加到顶部面包屑面板
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // 添加面包屑面板到窗口
        add(topPanel, BorderLayout.NORTH);
    }
}
