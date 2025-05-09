package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.xiaoyao.ShapevilleGUI.getJPanel;

public class Task1Screen extends JFrame {
    private int attempts = 3; // 尝试次数
    private String correctAnswer = "Circle"; // 正确答案
    private JProgressBar progressBar;
    private JLabel hintLabel;
    private JLabel attemptDots;
    private TopNavBarPanel topPanel;

    // 颜色常量
    private final Color orange = new Color(245, 158, 11); // 橙色 #f59e0b
    private final Color gray = new Color(229, 231, 235); // 灰色 #e5e7eb
    private final Color red = new Color(239, 68, 68); // 红色 #ef4444
    private final Color green = new Color(34, 197, 94); // 绿色

    public Task1Screen() {
        // 设置窗口
        setTitle("Task 1: Shape Recognition");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顶部导航栏
        JPanel gradientTopWrapper = getJPanel();
        topPanel = new TopNavBarPanel();
        gradientTopWrapper.add(topPanel);
        add(gradientTopWrapper, BorderLayout.NORTH);

        // 任务显示面板
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(new Color(240, 248, 255));  // 设置背景颜色

        // 进度条面板
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout());
        JLabel progressLabel = new JLabel("Your Progress: 0/8 shapes identified");
        progressBar = new JProgressBar(0, 8);
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);
        taskPanel.add(progressPanel);

        // 选择 2D 或 3D 形状
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout());
        JButton basicButton = new JButton("2D Shapes (Basic Level)");
        basicButton.setBackground(new Color(33, 150, 243));  // 按钮颜色
        basicButton.setForeground(Color.WHITE);
        JButton advancedButton = new JButton("3D Shapes (Advanced Level)");
        advancedButton.setBackground(new Color(33, 150, 243));  // 按钮颜色
        advancedButton.setForeground(Color.WHITE);
        levelPanel.add(basicButton);
        levelPanel.add(advancedButton);
        taskPanel.add(levelPanel);

        // 形状显示区域
        JPanel shapePanel = new JPanel();
        shapePanel.setLayout(new FlowLayout());
        JLabel shapeIcon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/task1/circle.png")));
        shapePanel.add(shapeIcon);
        taskPanel.add(shapePanel);

        // 问题和答案输入框
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel questionLabel = new JLabel("What shape is this?");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField answerField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        inputPanel.add(questionLabel);
        inputPanel.add(answerField);
        inputPanel.add(submitButton);
        taskPanel.add(inputPanel);

        // 错误提示框
        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new FlowLayout());
        hintPanel.setBackground(Color.YELLOW);
        hintLabel = new JLabel("Not quite right. Hint: This shape has no corners or edges.");
        hintPanel.add(hintLabel);
        taskPanel.add(hintPanel);

        // 尝试次数显示
        JPanel attemptPanel = new JPanel();
        attemptPanel.setLayout(new FlowLayout());
        attemptDots = new JLabel("Attempts: ");
        attemptDots.setFont(new Font("Arial", Font.PLAIN, 14));
        attemptPanel.add(attemptDots);
        taskPanel.add(attemptPanel);

        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField.getText().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    // 显示正确答案界面
                    hintLabel.setText("Correct! ✅ This is indeed a circle.");
                    hintLabel.setForeground(green);  // 设置提示为绿色
                    attempts = 3;  // 重置尝试次数
                    updateAttempts();
                    submitButton.setEnabled(false); // 禁用提交按钮
                    taskPanel.add(createNextShapeButton());  // 添加下一题按钮
                } else {
                    attempts--;
                    if (attempts > 0) {
                        hintLabel.setText("Not quite right. Hint: This shape has no corners or edges. Try again! (" + attempts + " attempts left)");
                    } else {
                        hintLabel.setText("No more attempts. The correct answer is: " + correctAnswer);
                        submitButton.setEnabled(false);  // 禁用提交按钮
                        taskPanel.add(createNextShapeButton());  // 添加下一题按钮
                    }
                    updateAttempts();
                }
            }
        });

        add(taskPanel, BorderLayout.CENTER);  // 将任务面板添加到主窗口

        setLocationRelativeTo(null);  // 居中显示
    }

    // 获取颜色的Hex值
    private String getColorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    // 更新尝试次数显示
    private void updateAttempts() {
        String attemptsText = "<html>Attempts: ";
        // 每次都有三个圆点，颜色会根据错误次数变化
        for (int i = 0; i < 3; i++) {
            if (attempts == 3) { // 初始状态
                if (i == 0) attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>"; // 橙色
                else attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>"; // 灰色
            } else if (attempts == 2) { // 第一次错误
                if (i == 0) attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 红色
                else if (i == 1) attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>"; // 橙色
                else attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>"; // 灰色
            } else if (attempts == 1) { // 第二次错误
                if (i == 0) attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 红色
                else if (i == 1) attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 红色
                else attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>"; // 橙色
            } else { // 第三次错误
                attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 全部红色
            }
        }
        attemptsText += "</html>";
        attemptDots.setText(attemptsText);
    }

    private JButton createNextShapeButton() {
        JButton nextButton = new JButton("Next Shape");
        nextButton.setBackground(new Color(33, 150, 243));
        nextButton.setForeground(Color.WHITE);
        nextButton.setPreferredSize(new Dimension(10, 40));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 使用 BoxLayout 来确保按钮居中
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // 使用垂直方向的 BoxLayout

        // 将按钮添加到面板
        buttonPanel.add(nextButton);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在此处理下一题的逻辑
                JOptionPane.showMessageDialog(null, "Loading next shape...");
            }
        });
        // 将按钮面板添加到框架的底部
        this.add(buttonPanel, BorderLayout.SOUTH);
        return nextButton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Task1Screen().setVisible(true);
            }
        });
    }
}
