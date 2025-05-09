package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskScreen extends JFrame {
    private String[] shapes2D = {"Circle", "Rectangle", "Triangle", "Square"};
    private String[] shapes3D = {"Cube", "Sphere", "Cone", "Pyramid"};
    private int currentTaskIndex = 0;
    private int attempts = 0;
    private String currentShape;
    private JLabel shapeLabel;
    private JTextField inputField;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel messageLabel;

    public TaskScreen() {
        setTitle("Shape Recognition Task");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置布局
        setLayout(new BorderLayout());
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new GridLayout(4, 1));

        // 显示图形
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        taskPanel.add(shapeLabel);

        // 用户输入框
        inputField = new JTextField();
        taskPanel.add(inputField);

        // 提交按钮
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        taskPanel.add(submitButton);

        // 主页按钮
        homeButton = new JButton("Home");
        homeButton.addActionListener(new HomeButtonListener());
        taskPanel.add(homeButton);

        // 错误信息
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        taskPanel.add(messageLabel);

        add(taskPanel, BorderLayout.CENTER);
        loadNextShape(); // 加载第一个形状
    }

    private void loadNextShape() {
        if (currentTaskIndex < shapes2D.length) {
            currentShape = shapes2D[currentTaskIndex];
        } else if (currentTaskIndex < shapes2D.length + shapes3D.length) {
            currentShape = shapes3D[currentTaskIndex - shapes2D.length];
        } else {
            currentShape = null;
        }

        if (currentShape != null) {
            shapeLabel.setText("Identify the shape: " + currentShape);
            messageLabel.setText("");
            inputField.setText("");
            attempts = 0;  // 重置尝试次数
        } else {
            shapeLabel.setText("All tasks complete!");
            messageLabel.setText("Congratulations!");
            submitButton.setEnabled(false);  // 禁用提交按钮
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = inputField.getText().trim();
            if (userInput.equalsIgnoreCase(currentShape)) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Correct!");
                currentTaskIndex++;
                loadNextShape();
            } else {
                attempts++;
                if (attempts < 3) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Incorrect, try again!");
                } else {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("The correct answer is: " + currentShape);
                    currentTaskIndex++;
                    loadNextShape();
                }
            }
        }
    }

    private class HomeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 返回主页的逻辑
            dispose(); // 关闭当前窗口
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskScreen taskScreen = new TaskScreen();
            taskScreen.setVisible(true);
        });
    }
}
