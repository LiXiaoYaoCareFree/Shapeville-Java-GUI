package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Task1Screen extends JFrame {
    private int attempts = 3; // 尝试次数
    private String correctAnswer = "Circle"; // 正确答案

    public Task1Screen() {
        // 设置窗口
        setTitle("Task 1: Shape Recognition");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 进度条面板
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout());
        JLabel progressLabel = new JLabel("Your Progress: 0/8 shapes identified");
        JProgressBar progressBar = new JProgressBar(0, 8);
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);

        // 选择2D或3D
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout());
        JButton basicButton = new JButton("2D Shapes (Basic Level)");
        JButton advancedButton = new JButton("3D Shapes (Advanced Level)");
        levelPanel.add(basicButton);
        levelPanel.add(advancedButton);

        // 形状图标
        JPanel shapePanel = new JPanel();
        shapePanel.setLayout(new FlowLayout());
        JLabel shapeIcon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/angles.png")));
        shapePanel.add(shapeIcon);

        // 问题和答案输入框
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel questionLabel = new JLabel("What shape is this?");
        JTextField answerField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        inputPanel.add(questionLabel);
        inputPanel.add(answerField);
        inputPanel.add(submitButton);

        // 错误提示框
        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new FlowLayout());
        hintPanel.setBackground(Color.YELLOW);
        JLabel hintLabel = new JLabel("Not quite right. Hint: This shape has no corners or edges.");
        hintPanel.add(hintLabel);

        // 布局添加组件
        add(progressPanel, BorderLayout.NORTH);
        add(levelPanel, BorderLayout.CENTER);
        add(shapePanel, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.SOUTH);
        add(hintPanel, BorderLayout.SOUTH);

        // 监听按钮点击事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField.getText().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    JOptionPane.showMessageDialog(null, "Correct! You've identified the shape!");
                } else {
                    attempts--;
                    if (attempts > 0) {
                        hintLabel.setText("Not quite right. Hint: This shape has no corners or edges. Try again! (" + attempts + " attempts left)");
                    } else {
                        hintLabel.setText("Out of attempts! The correct answer is: " + correctAnswer);
                    }
                }
            }
        });

        setLocationRelativeTo(null);  // 居中显示
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
