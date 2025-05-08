package com.xiaoyao;

import javax.swing.*;
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

        // 创建顶部面板（包含标题和进度）
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Welcome to Shapeville!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        // 创建进度条和进度标签
        progressLabel = new JLabel("Your Progress: " + currentProgress + "/100 points");
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(currentProgress);  // 当前进度
        progressBar.setStringPainted(true);  // 显示百分比

        topPanel.add(progressLabel);
        topPanel.add(progressBar);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShapevilleGUI frame = new ShapevilleGUI();
                frame.setVisible(true);
            }
        });
    }
}
