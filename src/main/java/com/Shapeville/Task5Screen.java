package com.Shapeville;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static com.Shapeville.ShapevilleGUI.getJPanel;

public class Task5Screen extends JFrame {
    private List<String> availableShapes;
    private Map<String, String> formulasMap;
    private Map<String, Double> solutionsMap;
    private String currentShape;
    private String attemptsText;

    private int attempts;
    private String correctFormula;
    private double correctSolution;

    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JLabel hintLabel;
    private JLabel attemptDots;
    private JTextField answerField;
    private JButton submitButton;
    private JButton nextButton;
    private JPanel shapePanel;

    private JLabel timerLabel;
    private Timer countdownTimer;
    private int remainingSeconds;

    // 颜色常量
    private final Color orange = new Color(245, 158, 11); // 橙色 #f59e0b
    private final Color gray = new Color(229, 231, 235); // 灰色 #e5e7eb
    private final Color red = new Color(239, 68, 68); // 红色 #ef4444
    private final Color green = new Color(34, 197, 94); // 绿色
    private final Color yellow = new Color(254,249,195);

    public Task5Screen() {
        setTitle("Task 5: Compound Shapes Area Calculation");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top navigation bar
        JPanel topWrapper = getJPanel();
        TopNavBarPanel top = new TopNavBarPanel();
        topWrapper.add(top);
        add(topWrapper, BorderLayout.NORTH);
        top.homeButton.addActionListener(e -> dispose());
        top.endSessionButton.addActionListener(e -> dispose());

        // Initialize data
        initializeData();

        // Progress bar
        progressBar = new JProgressBar(0, formulasMap.size());
        progressBar.setStringPainted(true);
        progressLabel = new JLabel("Completed: 0/" + formulasMap.size());
        JPanel progressPanel = new JPanel(new FlowLayout());
        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);
        add(progressPanel, BorderLayout.SOUTH);

        // Main content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Shape display
        shapePanel = new JPanel();
        mainPanel.add(shapePanel);

        // Timer display
        timerLabel = new JLabel("Time left: 05:00");
        mainPanel.add(timerLabel);
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            int mm = remainingSeconds / 60;
            int ss = remainingSeconds % 60;
            timerLabel.setText(String.format("Time left: %02d:%02d", mm, ss));
            if (remainingSeconds <= 0) {
                countdownTimer.stop();
                onTimeUp();
            }
        });

        // Answer input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Area:"));
        answerField = new JTextField(10);
        inputPanel.add(answerField);
        submitButton = new JButton("Submit");
        inputPanel.add(submitButton);
        mainPanel.add(inputPanel);

        hintLabel = new JLabel("You have 3 attempts.");
        mainPanel.add(hintLabel);
        attemptDots = new JLabel();
        mainPanel.add(attemptDots);

        nextButton = new JButton("Next");
        nextButton.setVisible(false);
        mainPanel.add(nextButton);

        add(mainPanel, BorderLayout.CENTER);

        // Event handlers
        submitButton.addActionListener(e -> onSubmit());
        nextButton.addActionListener(e -> selectNext());

        // Start first selection
        selectNext();
        setLocationRelativeTo(null);
    }

    private void initializeData() {
        availableShapes = new ArrayList<>();
        formulasMap = new LinkedHashMap<>();
        solutionsMap = new LinkedHashMap<>();

        // Shape 1
        availableShapes.add("Shape 1");
        formulasMap.put("Shape 1", "A = 14×14 + 0.5×14×5 = 231 cm²");
        solutionsMap.put("Shape 1", 183.5);

        // Shape 2
        availableShapes.add("Shape 2");
        formulasMap.put("Shape 2", "A = 20×21 - 10×11 = 420 - 110 = 310 cm²");
        solutionsMap.put("Shape 2", 310.0);

        // Shape 3
        availableShapes.add("Shape 3");
        formulasMap.put("Shape 3", "A = (18 + 16)×19 - 16×(19 - 16) = 34×19 - 16×3 = 646 - 48 = 598 cm²");
        solutionsMap.put("Shape 3", 598.0);

        // Shape 4
        availableShapes.add("Shape 4");
        formulasMap.put("Shape 4", "A = 24×6 + 12×12 = 144 + 144 = 288 m²");
        solutionsMap.put("Shape 4", 280.0);

        // Shape 5
        availableShapes.add("Shape 5");
        formulasMap.put("Shape 5", "A = 4×2 + 1/2×4×(4 - 2) = 8 + 4 = 12 m²");
        solutionsMap.put("Shape 5", 12.0);

        // Shape 6
        availableShapes.add("Shape 6");
        formulasMap.put("Shape 6", "A = 9×11 + 1/2×(20 - 9)×11 = 99 + 60.5 = 159.5 m²");
        solutionsMap.put("Shape 6", 159.5);

        // Shape 7 (需更多信息以计算)
        availableShapes.add("Shape 7");
        formulasMap.put("Shape 7", "A = √[s(s-a)(s-b)(s-c)] + 14×5, where s = (a+b+c)/2 = 21, A = √[21×9×7×5] + 70 = 21√15 + 70 ≈ 151.04 m²");
        solutionsMap.put("Shape 7", 151.04);

        // Shape 8
        availableShapes.add("Shape 8");
        formulasMap.put("Shape 8", "A = 60×36 + 36×36 = 2160 + 1296 = 3456 m²");
        solutionsMap.put("Shape 8", 3456.0);

        // Shape 9
        availableShapes.add("Shape 9");
        formulasMap.put("Shape 9", "A = 18×11 - 8×3 = 198 - 24 = 174 m²");
        solutionsMap.put("Shape 9", 174.0);
    }


    private void selectNext() {
        countdownTimer.stop();
        if (availableShapes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have practiced all shapes.");
            dispose();
            return;
        }
        String[] options = availableShapes.toArray(new String[0]);
        String selection = (String) JOptionPane.showInputDialog(
                this,
                "请选择一个图形",
                "选择图形",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selection == null) {
            return; // 用户取消选择
        }
        currentShape = selection;
        availableShapes.remove(selection);
        loadShape(currentShape);
    }

    private void loadShape(String shapeName) {
        correctFormula = formulasMap.get(shapeName);
        correctSolution = solutionsMap.get(shapeName);
        attempts = 3;
        updateAttemptsDisplay();
        hintLabel.setText("You have 3 attempts.");
        answerField.setText("");
        submitButton.setEnabled(true);
        nextButton.setVisible(false);

        // Load image
        shapePanel.removeAll();
        ImageIcon icon = new ImageIcon(getClass().getClassLoader()
                .getResource("images/task5/" + shapeName + ".png"));
        shapePanel.add(new JLabel(icon));
        shapePanel.revalidate();
        shapePanel.repaint();

        // Update progress
        int done = formulasMap.size() - availableShapes.size() - 1;
        progressBar.setValue(done);
        progressLabel.setText("Completed: " + done + "/" + formulasMap.size());

        // Start timer
        remainingSeconds = 300;
        timerLabel.setText("Time left: 05:00");
        countdownTimer.start();
    }
    // 更新尝试次数显示
    private void updateAttemptsDisplay() {
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
//    private void updateAttemptsDisplay() {
//        StringBuilder sb = new StringBuilder("Attempts: ");
//        for (int i = 0; i < 3; i++) {
//            sb.append(i < attempts ? "● " : "○ ");
//        }
//        attemptDots.setText(sb.toString());
//    }

    private void onSubmit() {
        try {
            double ans = Double.parseDouble(answerField.getText().trim());
            if (Math.abs(ans - correctSolution) < 0.01) {
                onCorrect();
            } else {
                attempts--;
                updateAttemptsDisplay();
                if (attempts > 0) {
                    hintLabel.setText("Incorrect. Attempts left: " + attempts);
                } else {
                    countdownTimer.stop();
                    showSolution();
                }
            }
        } catch (NumberFormatException ex) {
            hintLabel.setText("请输入有效数字");
        }
    }

    // 获取颜色的Hex值
    private String getColorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private void onCorrect() {
        countdownTimer.stop();
        hintLabel.setText("Correct!");
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
    }

    private void showSolution() {
        hintLabel.setText(correctFormula + " = " + correctSolution);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
    }

    private void onTimeUp() {
        hintLabel.setText("Time up! " + correctFormula + " = " + correctSolution);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task5Screen().setVisible(true));
    }
}
