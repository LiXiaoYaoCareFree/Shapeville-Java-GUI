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
    private final Color yellow = new Color(254,249,195);
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
        progressPanel.setFont(new Font("Arial", Font.BOLD, 24));

        // 自定义进度条
        progressBar = new JProgressBar(0, 8);
        progressBar.setValue(0);  // 设置初始进度为0
        progressBar.setPreferredSize(new Dimension(300, 20)); // 设置进度条的尺寸
        progressBar.setStringPainted(true); // 显示进度文本

        // 设置进度条的前景色和背景色
        progressBar.setForeground(new Color(23, 181, 67));  // 设置前景色（进度条颜色）
        progressBar.setBackground(new Color(229, 231, 235)); // 设置背景色（进度条背景色）

        // 设置进度条的圆角效果
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(progressBar.getForeground());
                g2d.fillRoundRect(0, 0, progressBar.getWidth(), progressBar.getHeight(), 20, 20); // 绘制圆角进度条
                super.paintDeterminate(g, c);
            }
        });

        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);
        taskPanel.add(progressPanel);

        // 选择 2D 或 3D 形状
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout());
        JButton basicButton = new JButton("2D Shapes (Basic Level)");
        basicButton.setFont(new Font("Roboto", Font.BOLD, 12));
        basicButton.setBackground(new Color(33, 150, 243));  // 按钮颜色
        basicButton.setForeground(Color.WHITE);
        JButton advancedButton = new JButton("3D Shapes (Advanced Level)");
        advancedButton.setFont(new Font("Roboto", Font.BOLD, 12));
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
        questionLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        JTextField styledTextField = new JTextField(20);
        styledTextField.setPreferredSize(new Dimension(300, 40));
        // 设置字体
        styledTextField.setFont(new Font("Roboto", Font.PLAIN, 16));  // 设置字体大小
        styledTextField.setForeground(Color.BLACK);  // 设置文本颜色

        // 设置背景颜色和边框
        styledTextField.setBackground(new Color(245, 245, 245));  // 设置背景色
        styledTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));  // 设置边框颜色和厚度

        // 设置圆角效果
        styledTextField.setCaretColor(Color.BLACK);  // 设置输入光标的颜色
        styledTextField.setSelectionColor(yellow);  // 设置选中文本的背景色
        styledTextField.setSelectionColor(Color.WHITE);  // 设置选中文本的前景色

        // 鼠标悬停效果，改变边框颜色
        styledTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                styledTextField.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2)); // 聚焦时改变边框颜色
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                styledTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2)); // 失去焦点时恢复边框颜色
            }
        });
        JButton submitButton = new JButton("Submit");
        // 设置按钮颜色和样式
        submitButton.setBackground(new Color(33, 150, 243));  // 背景颜色（蓝色）
        submitButton.setForeground(Color.WHITE);  // 文本颜色（白色）
        submitButton.setFont(new Font("Roboto", Font.BOLD, 16));  // 设置字体大小和加粗
        submitButton.setPreferredSize(new Dimension(100, 40));  // 设置按钮尺寸
        submitButton.setFocusPainted(false);  // 去掉按钮的焦点框
        submitButton.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));  // 设置边框颜色和宽度

        // 设置圆角和阴影
        submitButton.setBorder(BorderFactory.createCompoundBorder(
                submitButton.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // 增加内边距，圆角效果会更好
        ));

        submitButton.setBackground(new Color(33, 150, 243));
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false); // 去除按钮边框
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 鼠标样式

        // 鼠标悬停效果
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(23, 132, 204)); // 鼠标悬停时变暗
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(33, 150, 243)); // 恢复默认颜色
            }
        });
        inputPanel.add(questionLabel);
        inputPanel.add(styledTextField);
        inputPanel.add(submitButton);
        taskPanel.add(inputPanel);

        // 错误提示框
        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new FlowLayout());
        hintPanel.setBackground(yellow);
        hintLabel = new JLabel("Not quite right. Hint: This shape has no corners or edges.");
        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        hintLabel.setForeground(red);  // 设置提示为绿色
        hintPanel.add(hintLabel);
        taskPanel.add(hintPanel);

        // 尝试次数显示
        JPanel attemptPanel = new JPanel();
        attemptPanel.setLayout(new FlowLayout());
        attemptDots = new JLabel("Attempts: ");
        attemptDots.setFont(new Font("Roboto", Font.BOLD, 14));
        attemptPanel.add(attemptDots);
        taskPanel.add(attemptPanel);

        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = styledTextField.getText().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    // 显示正确答案界面
                    hintLabel.setText("Correct! ✅ This is indeed a circle.");
                    hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));  // 设置字体为 Arial，字体加粗，大小为 18
                    hintLabel.setForeground(green);  // 设置提示为绿色
                    attempts = 3;  // 重置尝试次数
                    updateAttempts();
                    submitButton.setEnabled(false); // 禁用提交按钮
                    taskPanel.add(createNextShapeButton());  // 添加下一题按钮
                } else {
                    attempts--;
                    if (attempts > 0) {
                        hintLabel.setText("Not quite right. Hint: This shape has no corners or edges. Try again! (" + attempts + " attempts left)");
                        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
                        hintLabel.setForeground(red);
                    } else {
                        hintLabel.setText("No more attempts. The correct answer is: " + correctAnswer);
                        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
                        hintLabel.setForeground(red);
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
