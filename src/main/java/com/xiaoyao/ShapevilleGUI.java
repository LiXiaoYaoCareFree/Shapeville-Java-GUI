package com.xiaoyao;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShapevilleGUI extends JFrame {

    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JButton homeButton, endSessionButton;
    private JButton level1Button, level2Button, level3Button;
    private JCheckBox colorBlindModeCheckBox;

    private boolean isColorBlindMode = false;  // 色盲模式标志
    private int currentProgress = 18;  // 当前进度

    public ShapevilleGUI() {
        // 设置窗口标题
        setTitle("Shapeville");

        // 设置窗口大小
        setSize(1000, 600);

        // 设置默认关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口布局
        setLayout(new BorderLayout());

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

        // 创建左侧的应用程序图标和名称
//        JPanel leftPanel = new JPanel();
//        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        leftPanel.setBackground(new Color(0, 0, 0, 0));  // 设置左侧面板背景透明
//        JLabel iconLabel = new JLabel(icon);
//        JLabel appName = new JLabel("Shapeville");
//        appName.setFont(new Font("Arial", Font.BOLD, 24));
//        appName.setForeground(Color.WHITE);
//        leftPanel.add(appName);
//        leftPanel.add(iconLabel);
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

        // 设置窗口居中显示
        setLocationRelativeTo(null);


//        topPanel.setLayout(new BorderLayout());
//        JLabel titleLabel = new JLabel("Welcome to Shapeville!");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        topPanel.add(titleLabel);

        // 创建进度条和进度标签
//        progressLabel = new JLabel("Your Progress: " + currentProgress + "/100 points");
//        progressBar = new JProgressBar(0, 100);
//        progressBar.setValue(currentProgress);  // 当前进度
//        progressBar.setStringPainted(true);  // 显示百分比
//
//        topPanel.add(progressLabel);
//        topPanel.add(progressBar);

        // 创建关卡选择区域
        JPanel levelsPanel = new JPanel();
        levelsPanel.setLayout(new GridLayout(1, 3, 10, 10));

        level1Button = new JButton("Level 1\nShape Recognition");
        level1Button.setFont(new Font("Arial", Font.PLAIN, 14));
        setButtonColor(level1Button, "green");

        level2Button = new JButton("Level 2\nAngles & Lines");
        level2Button.setFont(new Font("Arial", Font.PLAIN, 14));
        setButtonColor(level2Button, "blue");

        level3Button = new JButton("Level 3\nShape Properties");
        level3Button.setFont(new Font("Arial", Font.PLAIN, 14));
        setButtonColor(level3Button, "purple");

        levelsPanel.add(level1Button);
        levelsPanel.add(level2Button);
        levelsPanel.add(level3Button);

        // 创建色盲模式复选框
        colorBlindModeCheckBox = new JCheckBox("Enable Color Blind Mode");
        colorBlindModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        colorBlindModeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleColorBlindMode();
            }
        });

        // 创建底部按钮面板
        JPanel bottomPanel = new JPanel();
        homeButton = new JButton("Home");
        endSessionButton = new JButton("End Session");

        bottomPanel.add(homeButton);
        bottomPanel.add(endSessionButton);
        bottomPanel.add(colorBlindModeCheckBox);

        // 添加面板到窗口
        add(topPanel, BorderLayout.NORTH);
        add(levelsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(new ShapevilleMainContent(), BorderLayout.CENTER);


        // 设置按钮事件
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Returning to Home Screen...");
            }
        });

        endSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You earned " + currentProgress + " points in this session. Goodbye!");
                System.exit(0);  // 退出程序
            }
        });

        // 设置窗口居中显示
        setLocationRelativeTo(null);

    }

    // 设置按钮颜色
    private void setButtonColor(JButton button, String color) {
        if (isColorBlindMode) {
            // 色盲模式下采用更高对比度的颜色
            switch (color) {
                case "green":
                    button.setBackground(new Color(0, 128, 0));  // 深绿色
                    break;
                case "blue":
                    button.setBackground(new Color(0, 0, 255));  // 深蓝色
                    break;
                case "purple":
                    button.setBackground(new Color(128, 0, 128));  // 深紫色
                    break;
            }
        } else {
            // 默认颜色
            switch (color) {
                case "green":
                    button.setBackground(new Color(76, 175, 80));  // 绿色
                    break;
                case "blue":
                    button.setBackground(new Color(33, 150, 243));  // 蓝色
                    break;
                case "purple":
                    button.setBackground(new Color(156, 39, 176));  // 紫色
                    break;
            }
        }
        button.setForeground(Color.WHITE);  // 设置按钮文字颜色为白色
    }

    // 切换色盲模式
    private void toggleColorBlindMode() {
        isColorBlindMode = colorBlindModeCheckBox.isSelected();
        // 更新按钮颜色
        setButtonColor(level1Button, "green");
        setButtonColor(level2Button, "blue");
        setButtonColor(level3Button, "purple");
    }

    // 更新进度条
    private void updateProgress(int progress) {
        currentProgress = progress;
        progressBar.setValue(currentProgress);
        progressLabel.setText("Your Progress: " + currentProgress + "/100 points");
    }
}


