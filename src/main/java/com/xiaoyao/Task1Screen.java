package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.xiaoyao.ShapevilleGUI.getJPanel;

public class Task1Screen extends JFrame {
    private int attempts = 3; // 尝试次数
    private String correctAnswer; // 正确答案
    public int score = 0; // 分数
    private JProgressBar progressBar;
    private TopNavBarPanel topPanel;
    private JToggleButton basicButton;
    private JToggleButton advancedButton;
    private JPanel shapePanel;  // 显示形状的面板
    private JPanel progressPanel;
    private JPanel taskPanel;
    private JPanel levelPanel;
    private JPanel inputPanel;
    private JPanel hintPanel;
    private JPanel attemptPanel;
    private JPanel gradientTopWrapper;
    private JLabel hintLabel;
    private JLabel attemptDots;
    private JLabel progressLabel;
    private JLabel questionLabel;
    private JTextField styledTextField;
    private JButton submitButton;
    private JButton nextButton;
    private String attemptsText;
    private Boolean isBasic;



    // 2D 和 3D 形状数组
    private String[] shapes2D = {"Circle", "Rectangle", "Triangle", "Oval", "Octagon", "Square", "Heptagon", "Rhombus", "Pentagon", "Hexagon", "Kite"};
    private String[] shapes3D = {"Cube", "Cuboid", "Cylinder", "Sphere", "Triangular prism", "Square-based pyramid", "Cone", "Tetrahedron"};

    private int currentShapeIndex = 0; // 记录当前加载的图形索引
    private List<String> allShapes = new ArrayList<>(); // 存储所有待加载的图形

    // 颜色常量
    private final Color orange = new Color(245, 158, 11); // 橙色 #f59e0b
    private final Color gray = new Color(229, 231, 235); // 灰色 #e5e7eb
    private final Color red = new Color(239, 68, 68); // 红色 #ef4444
    private final Color green = new Color(34, 197, 94); // 绿色
    private final Color yellow = new Color(254,249,195);

    // 从2D形状数组中随机选择4个
    List<String> selected2DShapes = getRandomElements(shapes2D, 4);
    // 从3D形状数组中随机选择4个
    List<String> selected3DShapes = getRandomElements(shapes3D, 4);

    private void CreateTask1Screen() {
        // 设置窗口
        setTitle("Task 1: Shape Recognition");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void CreateTask1TopNavigationBar() {
        // 顶部导航栏
        gradientTopWrapper = getJPanel();
        topPanel = new TopNavBarPanel();
        gradientTopWrapper.add(topPanel);
        add(gradientTopWrapper, BorderLayout.NORTH);
    }

    private void CreateTask1ProgressBarPanel() {
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
    }

    private void CreateTask1levelPanel() {
        //创建按钮组，用于管理互斥选择
        basicButton = new JToggleButton("2D Shapes (Basic Level)");
        basicButton.setPreferredSize(new Dimension(300, 40));  // 设置按钮尺寸
        basicButton.setFont(new Font("Roboto", Font.BOLD, 16));
        basicButton.setBackground(new Color(33, 150, 243));  // 按钮颜色
        basicButton.setForeground(Color.WHITE);
        advancedButton = new JToggleButton("3D Shapes (Advanced Level)");
        advancedButton.setPreferredSize(new Dimension(300, 40));  // 设置按钮尺寸
        advancedButton.setFont(new Font("Roboto", Font.BOLD, 16));
        advancedButton.setBackground(new Color(33, 150, 243));  // 按钮颜色
        advancedButton.setForeground(Color.WHITE);
    }

    private void CreateTask1InputPanel() {
        // 问题和答案输入框
        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        questionLabel = new JLabel("What shape is this?");
        questionLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        styledTextField = new JTextField(20);
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
    }

    private void CreateTask1submitButton() {
        submitButton = new JButton("Submit");
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
        SubmitButtonEvent();
    }

    private void SubmitButtonEvent() {
        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = styledTextField.getText().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    // 显示正确答案界面
                    hintLabel.setText("Correct! ✅ This is indeed a circle.");
                    if (currentShapeIndex <= 3) {
                        isBasic = true;
                    } else {
                        isBasic = false;
                    }
                    score += calculateScore(isBasic, attempts);
                    System.out.println(score);
                    // 清空输入框（三次错误后）
                    styledTextField.setText("");
                    JOptionPane.showMessageDialog(null, "Correct! Your current score: " + score);
                    hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));  // 设置字体为 Arial，字体加粗，大小为 18
                    hintLabel.setForeground(green);  // 设置提示为绿色
                    attempts = 3;  // 重置尝试次数
                    updateAttempts();
                    submitButton.setEnabled(false); // 禁用提交按钮
                    taskPanel.add(createNextShapeButton());  // 添加下一题按钮
                } else {
                    attempts--;
                    if (attempts > 0) {
                        hintLabel.setText("Not quite right.Try again! (" + attempts + " attempts left)");
                        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
                        hintLabel.setForeground(red);
                    } else {
                        hintLabel.setText("No more attempts. The correct answer is: " + correctAnswer);
                        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
                        hintLabel.setForeground(red);
                        // 清空输入框（三次错误后）
                        styledTextField.setText("");
                        submitButton.setEnabled(false);  // 禁用提交按钮
                        taskPanel.add(createNextShapeButton());  // 添加下一题按钮
                    }
                    updateAttempts();
                }
            }
        });
    }

    private void CreateTask1HintPanel() {
        // 错误提示框
        hintPanel = new JPanel();
        hintPanel.setLayout(new FlowLayout());
        hintPanel.setBackground(yellow);
        hintLabel = new JLabel("You are allowed three attempts.");
        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        hintLabel.setForeground(red);  // 设置提示为绿色
        hintPanel.add(hintLabel);
    }

    private void CreateTask1AttemptPanel() {
        attemptPanel = new JPanel();
        attemptPanel.setLayout(new FlowLayout());
        attemptDots = new JLabel("Attempts: ");
        attemptDots.setFont(new Font("Roboto", Font.BOLD, 14));
        attemptPanel.add(attemptDots);
    }

    // 根据级别和尝试次数计算分数
    private int calculateScore(boolean isBasic, int attempts) {
        switch (attempts) {
            case 1:
                return isBasic? 1 : 2;
            case 2:
                return isBasic? 2 : 4;
            case 3:
                return isBasic? 3 : 6;
            default:
                return 0;
        }
    }

    // 获取颜色的Hex值
    private String getColorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    // 更新尝试次数显示
    private void updateAttempts() {
        attemptsText = "<html>Attempts: ";
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
        nextButton = new JButton("Next Shape");
        nextButton.setBackground(new Color(33, 150, 243));
        nextButton.setForeground(Color.WHITE);
        nextButton.setPreferredSize(new Dimension(10, 40));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在此处理下一题的逻辑
//                JOptionPane.showMessageDialog(null, "Loading next shape...");
                loadNextShape();  // 调用重新加载形状的方法

                // 重置 submitButton 按钮的状态
                submitButton.setEnabled(true); // 启用提交按钮
                submitButton.setBackground(new Color(33, 150, 243)); // 恢复默认背景颜色
                hintLabel.setText("You are allowed three attempts."); // 清空提示信息
                hintLabel.setForeground(red); // 恢复提示信息颜色
                attempts = 3; // 重置尝试次数
                updateAttempts(); // 更新尝试次数显示

                // 移除下一题按钮
                taskPanel.remove(nextButton);
                taskPanel.revalidate();
                taskPanel.repaint();
            }
        });
        return nextButton;
    }



    // 加载下一个形状
    private void loadNextShape() {
        if (currentShapeIndex < allShapes.size()) {
            String nextShapeName = allShapes.get(currentShapeIndex);
            try {
                // 加载图片
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/task1/" + nextShapeName + ".png"));
                JLabel nextShape = new JLabel(icon);

                shapePanel.removeAll();  // 清除当前形状
                shapePanel.add(nextShape);  // 添加新形状
                shapePanel.revalidate();  // 重新验证面板，以更新显示
                shapePanel.repaint();  // 重新绘制面板以显示新形状

                correctAnswer = nextShapeName; // 更新正确答案
                attempts = 3; // 重置尝试次数
                updateAttempts();

                // 重置 submitButton 按钮的状态
                submitButton.setEnabled(true); // 启用提交按钮
                submitButton.setBackground(new Color(33, 150, 243)); // 恢复默认背景颜色
                hintLabel.setText("Not quite right. Hint: Try to identify the shape."); // 重置提示信息
                hintLabel.setForeground(red); // 恢复提示信息颜色

                currentShapeIndex++; // 移动到下一个图形

                // 更新进度条
                progressBar.setValue(currentShapeIndex - 1);
                progressLabel.setText("Your Progress: " + (currentShapeIndex - 1) + "/8 shapes identified");

                // 如果所有图形都已加载，移除下一题按钮
                if (currentShapeIndex >= allShapes.size()) {
                    taskPanel.remove(nextButton);
                    taskPanel.revalidate();
                    taskPanel.repaint();
                }
            } catch (Exception e) {
                // 图片加载失败时显示默认文本
                JLabel errorLabel = new JLabel("图片未找到: " + nextShapeName, JLabel.CENTER);
                shapePanel.removeAll();
                shapePanel.add(errorLabel);
                shapePanel.revalidate();
                shapePanel.repaint();
                e.printStackTrace();
            }
        }
    }



    // 从数组中随机选择指定数量的元素
    private java.util.List<String> getRandomElements(String[] array, int count) {
        List<String> list = new ArrayList<>(Arrays.asList(array));
        Collections.shuffle(list);
        return list.subList(0, Math.min(count, list.size()));
    }


    private void configureButton(JToggleButton button) {
        button.setPreferredSize(new Dimension(300, 40));
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setForeground(Color.WHITE);

        // 配置按钮样式
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // 自定义按钮状态颜色
        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBackground(new Color(25, 118, 210)); // 深蓝色选中状态
            } else {
                button.setBackground(new Color(33, 150, 243)); // 浅蓝色默认状态
            }
        });

        // 默认背景颜色
        button.setBackground(new Color(33, 150, 243));
    }


    public Task1Screen() {
        // 设置窗口
        CreateTask1Screen();

        // 顶部导航栏
        CreateTask1TopNavigationBar();

        // 任务显示面板
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(new Color(240, 248, 255));  // 设置背景颜色

        // 进度条面板
        progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout());
        progressLabel = new JLabel("Your Progress: 0/8 shapes identified");
        progressPanel.setFont(new Font("Arial", Font.BOLD, 24));

        // 自定义进度条
        CreateTask1ProgressBarPanel();

        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);

        taskPanel.add(progressPanel);

         //选择 2D 或 3D 形状
        levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout());

        //创建按钮组，用于管理互斥选择
        CreateTask1levelPanel();

        levelPanel.add(basicButton);
        levelPanel.add(advancedButton);
        taskPanel.add(levelPanel);


        // 形状显示区域
        shapePanel = new JPanel();
        shapePanel.setLayout(new FlowLayout());
        taskPanel.add(shapePanel);

        // 问题和答案输入框
        CreateTask1InputPanel();

        // 创建 submitButton
        CreateTask1submitButton();

        inputPanel.add(questionLabel);
        inputPanel.add(styledTextField);
        inputPanel.add(submitButton);
        taskPanel.add(inputPanel);

        // 错误提示框
        CreateTask1HintPanel();
        taskPanel.add(hintPanel);

        // 尝试次数显示
        CreateTask1AttemptPanel();
        taskPanel.add(attemptPanel);

        // 将2D和3D形状合并到一个列表中
        allShapes.addAll(selected2DShapes);
        allShapes.addAll(selected3DShapes);

        // 加载第一个形状
        loadNextShape();

        add(taskPanel, BorderLayout.CENTER);  // 将任务面板添加到主窗口

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
