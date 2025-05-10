package com.xiaoyao;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Task2Screen extends JFrame {
    private int attempts = 3; // 尝试次数
    private int score = 0; // 分数
    private JTextField angleInput;
    private JTextArea resultArea;
    private JButton submitButton;
    private JButton homeButton;
    private List<String> identifiedAngles = new ArrayList<>(); // 已识别的角度类型
    private Set<Integer> usedAngles = new HashSet<>(); // 已使用的角度值
    private int currentAngle; // 当前角度
    private JLabel angleVisualization; // 角度可视化组件

    // 角度类型及其范围
    private static final Map<String, int[]> ANGLE_TYPES = new LinkedHashMap<>();
    static {
        ANGLE_TYPES.put("锐角", new int[]{0, 89});
        ANGLE_TYPES.put("直角", new int[]{90, 90});
        ANGLE_TYPES.put("钝角", new int[]{91, 179});
        ANGLE_TYPES.put("平角", new int[]{180, 180});
        ANGLE_TYPES.put("优角", new int[]{181, 359});
        ANGLE_TYPES.put("周角", new int[]{360, 360});
    }

    public Task2Screen() {
        // 设置窗口
        setTitle("Task 2: Identification of Angle Types");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 标题
        JLabel titleLabel = new JLabel("Task 2: Identification of Angle Types");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        // 分数显示
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scoreLabel);

        // 已识别的角度类型
        JLabel identifiedLabel = new JLabel("已识别的角度类型: " + identifiedAngles);
        identifiedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        identifiedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(identifiedLabel);

        // 角度可视化面板
        JPanel visualizationPanel = new JPanel(new BorderLayout());
        visualizationPanel.setPreferredSize(new Dimension(400, 400));
        visualizationPanel.setBorder(BorderFactory.createTitledBorder("角度可视化"));

        angleVisualization = new JLabel();
        angleVisualization.setHorizontalAlignment(JLabel.CENTER);
        angleVisualization.setVerticalAlignment(JLabel.CENTER);
        visualizationPanel.add(angleVisualization, BorderLayout.CENTER);

        mainPanel.add(visualizationPanel);

        // 输入面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel inputLabel = new JLabel("输入角度值 (0-360, 10的倍数):");
        angleInput = new JTextField(10);
        submitButton = new JButton("提交");

        inputPanel.add(inputLabel);
        inputPanel.add(angleInput);
        inputPanel.add(submitButton);

        mainPanel.add(inputPanel);

        // 结果显示区域
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        mainPanel.add(scrollPane);

        // 提示
        JLabel hintLabel = new JLabel("提示: 角度类型包括 锐角, 直角, 钝角, 平角, 优角, 周角");
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(hintLabel);

        // 尝试次数
        JLabel attemptsLabel = new JLabel("尝试次数: " + attempts);
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(attemptsLabel);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        homeButton = new JButton("Home");
        buttonPanel.add(homeButton);
        mainPanel.add(buttonPanel);

        // 添加主面板
        add(mainPanel, BorderLayout.CENTER);

        // 初始化第一个角度
        generateNewAngle();

        // 注册事件监听器
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAngle();
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前窗口
            }
        });

        setVisible(true);
    }

    // 生成新的角度
    private void generateNewAngle() {
        // 如果所有角度类型都已识别，结束任务
        if (identifiedAngles.size() >= ANGLE_TYPES.size()) {
            showCompletionDialog();
            return;
        }

        // 生成一个未使用过的角度值
        Random random = new Random();
        do {
            currentAngle = random.nextInt(37) * 10; // 0, 10, 20, ..., 360
        } while (usedAngles.contains(currentAngle));

        usedAngles.add(currentAngle);

        // 更新界面
        updateAngleVisualization();
        resultArea.setText("请输入角度值并识别其类型");
        attempts = 3;
        repaint();
    }

    // 更新角度可视化
    private void updateAngleVisualization() {
        // 创建一个自定义面板来绘制角度
        JPanel anglePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int centerX = width / 2;
                int centerY = height / 2;
                int radius = Math.min(width, height) / 2 - 20;

                // 绘制圆
                g.setColor(Color.BLACK);
                g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

                // 绘制基准线（水平向右）
                g.setColor(Color.BLACK);
                g.drawLine(centerX, centerY, centerX + radius, centerY);

                // 绘制角度线
                double angleRadians = Math.toRadians(currentAngle);
                int endX = centerX + (int) (radius * Math.cos(angleRadians));
                int endY = centerY + (int) (radius * Math.sin(angleRadians));
                g.setColor(Color.RED);
                g.drawLine(centerX, centerY, endX, endY);

                // 绘制角度弧线
                g.setColor(Color.BLUE);
                if (currentAngle <= 180) {
                    g.drawArc(centerX - radius/3, centerY - radius/3,
                            radius*2/3, radius*2/3, 0, -currentAngle);
                } else {
                    g.drawArc(centerX - radius/3, centerY - radius/3,
                            radius*2/3, radius*2/3, 0, -(360 - currentAngle));
                }

                // 标注角度值
                g.setColor(Color.BLACK);
                g.drawString(currentAngle + "°", centerX + radius/4, centerY - radius/4);
            }
        };

        angleVisualization.removeAll();
        angleVisualization.add(anglePanel);
        angleVisualization.revalidate();
        angleVisualization.repaint();
    }

    // 检查用户输入的角度类型
    private void checkAngle() {
        String userInput = angleInput.getText().trim();

        // 验证输入是否为有效整数
        int userAngle;
        try {
            userAngle = Integer.parseInt(userInput);
        } catch (NumberFormatException ex) {
            resultArea.setText("错误: 请输入有效的整数角度值!");
            return;
        }

        // 验证输入范围
        if (userAngle < 0 || userAngle > 360 || userAngle % 10 != 0) {
            resultArea.setText("错误: 角度值必须在0-360之间，并且是10的倍数!");
            return;
        }

        // 检查输入的角度是否与当前角度匹配
        if (userAngle != currentAngle) {
            attempts--;
            resultArea.setText("不正确! 当前角度不是 " + userAngle + "°\n" +
                    "剩余尝试次数: " + attempts);

            if (attempts <= 0) {
                showCorrectAnswer();
                generateNewAngle();
            }
            return;
        }

        // 如果角度匹配，提示用户输入角度类型
        String angleType = JOptionPane.showInputDialog(
                this,
                "角度值正确! 请输入这个角度的类型:",
                "角度类型识别",
                JOptionPane.QUESTION_MESSAGE
        );

        if (angleType == null) {
            return; // 用户取消了输入
        }

        angleType = angleType.trim();

        // 检查角度类型是否正确
        String correctType = getAngleType(currentAngle);
        if (angleType.equals(correctType)) {
            score += 10; // 答对加10分
            resultArea.setText("正确! " + currentAngle + "° 是 " + correctType + "\n" +
                    "你的分数: " + score);

            // 如果该类型未被识别过，添加到已识别列表
            if (!identifiedAngles.contains(correctType)) {
                identifiedAngles.add(correctType);
            }

            // 延迟生成新角度，让用户有时间查看结果
            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generateNewAngle();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            attempts--;
            resultArea.setText("不正确! " + currentAngle + "° 不是 " + angleType + "\n" +
                    "剩余尝试次数: " + attempts);

            if (attempts <= 0) {
                showCorrectAnswer();
                generateNewAngle();
            }
        }
    }

    // 获取角度的类型
    private String getAngleType(int angle) {
        for (Map.Entry<String, int[]> entry : ANGLE_TYPES.entrySet()) {
            int[] range = entry.getValue();
            if (angle >= range[0] && angle <= range[1]) {
                return entry.getKey();
            }
        }
        return "未知类型";
    }

    // 显示正确答案
    private void showCorrectAnswer() {
        String correctType = getAngleType(currentAngle);
        resultArea.setText("尝试次数已用完! 正确答案是: " + currentAngle + "° 是 " + correctType);
    }

    // 显示完成对话框
    private void showCompletionDialog() {
        JOptionPane.showMessageDialog(
                this,
                "恭喜你已识别所有角度类型!\n最终得分: " + score,
                "任务完成",
                JOptionPane.INFORMATION_MESSAGE
        );
        dispose(); // 关闭当前窗口
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Task2Screen();
            }
        });
    }
}