package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskScreen extends JPanel {
    private String[] shapes2D = {"Circle", "Rectangle", "Triangle", "Oval", "Octagon", "Square", "Heptagon", "Rhombus", "Pentagon", "Hexagon", "Kite"};
    private int currentTaskIndex = 0;
    private int attempts = 0;
    private String currentShape;
    private JLabel shapeLabel;
    private JTextField inputField;
    private JButton submitButton;
    private JButton nextButton;
    private JLabel messageLabel;
    private JProgressBar progressBar;
    private JLabel progressLabel;

    public TaskScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 250, 255)); // 背景颜色

        // 任务标题
        JLabel taskTitle = new JLabel("Task 1: Identification of Shapes");
        taskTitle.setFont(new Font("Arial", Font.BOLD, 20));
        taskTitle.setAlignmentX(CENTER_ALIGNMENT);
        add(taskTitle);

        // 任务说明
        JLabel taskDescription = new JLabel("<html>This task consists of two sub-tasks:<br>• 2D Shapes [Basic level scoring]<br>• 3D Shapes [Advanced level scoring]</html>");
        taskDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        taskDescription.setAlignmentX(CENTER_ALIGNMENT);
        add(taskDescription);

        // 进度条
        progressLabel = new JLabel("Your Progress: 0/8 shapes identified");
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        progressLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(progressLabel);

        progressBar = new JProgressBar(0, 8);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(CENTER_ALIGNMENT);
        add(progressBar);

        // 显示形状
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setPreferredSize(new Dimension(150, 150)); // 设置图片显示区域大小
        add(shapeLabel);

        // 用户输入框
        inputField = new JTextField("Type shape name here...");
        inputField.setAlignmentX(CENTER_ALIGNMENT);
        add(inputField);

        // 提交按钮
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(CENTER_ALIGNMENT);
        submitButton.addActionListener(new SubmitButtonListener());
        add(submitButton);

        // 错误信息
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(messageLabel);

        // 下一形状按钮
        nextButton = new JButton("Next Shape");
        nextButton.setAlignmentX(CENTER_ALIGNMENT);
        nextButton.addActionListener(new NextButtonListener());
        nextButton.setEnabled(false); // 初始不可用
        add(nextButton);

        loadNextShape(); // 加载第一个形状
    }

    private void loadNextShape() {
        if (currentTaskIndex < shapes2D.length) {
            currentShape = shapes2D[currentTaskIndex];
        } else {
            currentShape = null;
        }

        if (currentShape != null) {
            shapeLabel.setText("What shape is this?");
            loadShapeImage(currentShape);
            messageLabel.setText("");
            inputField.setText("");
            attempts = 0;  // 重置尝试次数
        } else {
            shapeLabel.setText("All tasks complete!");
            messageLabel.setText("Congratulations!");
            submitButton.setEnabled(false);  // 禁用提交按钮
        }
    }

    private void loadShapeImage(String shape) {
        String imagePath = "images/" + shape.toLowerCase() + ".png";  // 使用形状名称作为图片文件名
        ImageIcon shapeImage = new ImageIcon(getClass().getClassLoader().getResource(imagePath));

        if (shapeImage != null) {
            // 调整图片大小
            Image img = shapeImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            shapeImage = new ImageIcon(img);
            shapeLabel.setIcon(shapeImage);  // 设置图片
        } else {
            shapeLabel.setText("Image not found for: " + shape);
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
                progressBar.setValue(currentTaskIndex);  // 更新进度条
                progressLabel.setText("Your Progress: " + currentTaskIndex + "/8 shapes identified");
                nextButton.setEnabled(true);  // 启用下一形状按钮
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
                    progressBar.setValue(currentTaskIndex);  // 更新进度条
                    progressLabel.setText("Your Progress: " + currentTaskIndex + "/8 shapes identified");
                    nextButton.setEnabled(true);  // 启用下一形状按钮
                }
            }
        }
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 切换到下一个形状
            nextButton.setEnabled(false);  // 禁用按钮直到用户提交
            loadNextShape();
        }
    }
}
