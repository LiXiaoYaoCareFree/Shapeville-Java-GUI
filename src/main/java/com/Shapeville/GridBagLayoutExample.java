package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GridBagLayoutExample extends JFrame {
    private int attempts = 3;
    private int currentAngle;
    private List<String> identifiedAngles = new ArrayList<>();

    // UI组件
    private JLabel titleLabel;
    private JLabel instructionLabel;
    private JTextField angleInput;
    private JButton submitButton;
    private JButton homeButton;
    private JLabel resultLabel;
    private AngleCanvas angleCanvas;
    private JLabel attemptsLabel;
    private JPanel identifiedTypesPanel;

    // 角度类型标签
    private JLabel acuteLabel;
    private JLabel rightLabel;
    private JLabel obtuseLabel;
    private JLabel reflexLabel;

    public GridBagLayoutExample() {
        // 设置窗口
        setTitle("Task 2: Identification of Angle Types");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 组件间距

        // 初始化组件
        titleLabel = new JLabel("Angle Identification Task");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        instructionLabel = new JLabel("Enter an angle between 0 and 360 (in multiples of 10):");

        angleInput = new JTextField(10);
        submitButton = new JButton("Submit");
        homeButton = new JButton("Home");

        resultLabel = new JLabel("");
        attemptsLabel = new JLabel("Attempts left: 3");

        angleCanvas = new AngleCanvas();

        // 已识别角度类型面板
        identifiedTypesPanel = new JPanel();
        identifiedTypesPanel.setBorder(BorderFactory.createTitledBorder("Identified Angle Types"));

        acuteLabel = new JLabel("Acute: ❌");
        rightLabel = new JLabel("Right: ❌");
        obtuseLabel = new JLabel("Obtuse: ❌");
        reflexLabel = new JLabel("Reflex: ❌");

        identifiedTypesPanel.add(acuteLabel);
        identifiedTypesPanel.add(rightLabel);
        identifiedTypesPanel.add(obtuseLabel);
        identifiedTypesPanel.add(reflexLabel);

        // 使用GridBagLayout添加组件
        // 标题行
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 30;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // 已识别类型面板
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(identifiedTypesPanel, gbc);

        // 角度显示区域
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        add(angleCanvas, gbc);

        // 指令标签
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(instructionLabel, gbc);

        // 输入框和按钮
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(angleInput, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(submitButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(homeButton, gbc);

        // 结果标签
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(resultLabel, gbc);

        // 尝试次数标签
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(attemptsLabel, gbc);

        // 添加事件监听器
        submitButton.addActionListener(new SubmitButtonListener());
        homeButton.addActionListener(e -> dispose());

        // 生成第一个角度
        generateNewAngle();

        setLocationRelativeTo(null); // 居中显示
    }

    // 生成新角度
    private void generateNewAngle() {
        do {
            currentAngle = (int) (Math.random() * 37) * 10;
        } while (isAllAnglesIdentified() || isAngleTypeAlreadyIdentified(getAngleType(currentAngle)));

        angleCanvas.setAngle(currentAngle);
        angleCanvas.repaint();
        attempts = 3;
        attemptsLabel.setText("Attempts left: 3");
        resultLabel.setText("");
    }

    // 获取角度类型
    private String getAngleType(int angle) {
        if (angle > 0 && angle < 90) {
            return "Acute";
        } else if (angle == 90) {
            return "Right";
        } else if (angle > 90 && angle < 180) {
            return "Obtuse";
        } else if (angle > 180 && angle < 360) {
            return "Reflex";
        } else {
            return "None";
        }
    }

    // 检查是否已识别所有角度类型
    private boolean isAllAnglesIdentified() {
        return identifiedAngles.contains("Acute") && identifiedAngles.contains("Right")
                && identifiedAngles.contains("Obtuse") && identifiedAngles.contains("Reflex");
    }

    // 检查特定角度类型是否已被识别
    private boolean isAngleTypeAlreadyIdentified(String angleType) {
        return identifiedAngles.contains(angleType);
    }

    // 更新已识别角度类型显示
    private void updateIdentifiedTypesDisplay() {
        acuteLabel.setText("Acute: " + (identifiedAngles.contains("Acute") ? "✅" : "❌"));
        rightLabel.setText("Right: " + (identifiedAngles.contains("Right") ? "✅" : "❌"));
        obtuseLabel.setText("Obtuse: " + (identifiedAngles.contains("Obtuse") ? "✅" : "❌"));
        reflexLabel.setText("Reflex: " + (identifiedAngles.contains("Reflex") ? "✅" : "❌"));
    }

    // 提交按钮监听器
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String userAnswer = angleInput.getText().trim();
                String correctType = getAngleType(currentAngle);

                if (userAnswer.equalsIgnoreCase(correctType)) {
                    resultLabel.setText("Correct! This is a " + correctType + " angle.");
                    identifiedAngles.add(correctType);
                    updateIdentifiedTypesDisplay();

                    if (isAllAnglesIdentified()) {
                        resultLabel.setText("Congratulations! You have identified all 4 types of angles.");
                        submitButton.setEnabled(false);
                        angleInput.setEnabled(false);
                    } else {
                        generateNewAngle();
                    }
                } else {
                    attempts--;
                    attemptsLabel.setText("Attempts left: " + attempts);

                    if (attempts > 0) {
                        resultLabel.setText("Incorrect. You have " + attempts + " attempts left.");
                    } else {
                        resultLabel.setText("No more attempts. The correct answer is: " + correctType);
                        identifiedAngles.add(correctType);
                        updateIdentifiedTypesDisplay();

                        if (isAllAnglesIdentified()) {
                            resultLabel.setText("Congratulations! You have identified all 4 types of angles.");
                            submitButton.setEnabled(false);
                            angleInput.setEnabled(false);
                        } else {
                            generateNewAngle();
                        }
                    }
                }
            } catch (Exception ex) {
                resultLabel.setText("Please enter a valid angle.");
            }
        }
    }

    // 角度画布
    private static class AngleCanvas extends JPanel {
        private int angle = 0;

        public AngleCanvas() {
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setBackground(Color.WHITE);
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(centerX, centerY) - 40;

            // 绘制坐标轴
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(0, centerY, getWidth(), centerY); // x轴
            g2d.drawLine(centerX, 0, centerX, getHeight()); // y轴

            // 绘制角度
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(centerX, centerY, centerX + radius, centerY); // 起始边

            // 计算终止边的坐标
            double radians = Math.toRadians(angle);
            int endX = (int) (centerX + radius * Math.cos(radians));
            int endY = (int) (centerY - radius * Math.sin(radians));
            g2d.drawLine(centerX, centerY, endX, endY); // 终止边

            // 绘制圆弧
            g2d.setColor(Color.RED);
            if (angle <= 180) {
                g2d.drawArc(centerX - radius/2, centerY - radius/2, radius, radius, 0, -angle);
            } else {
                // 对于大于180度的角，绘制内外两个圆弧
                g2d.drawArc(centerX - radius/3, centerY - radius/3, radius*2/3, radius*2/3, 0, -180);
                g2d.drawArc(centerX - radius*2/3, centerY - radius*2/3, radius*4/3, radius*4/3, 180, -(angle-180));
            }

            // 显示角度值
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(angle + "°", centerX + radius/2 + 10, centerY - radius/2 - 10);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GridBagLayoutExample().setVisible(true));
    }
}