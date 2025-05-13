package com.Shapeville;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static com.Shapeville.ShapevilleGUI.getJPanel;
import static com.Shapeville.ShapevilleMainContent.flag2;
import static com.Shapeville.ShapevilleMainContent.flag6;

public class Task6Screen extends JFrame {
    private List<String> availableShapes;
    private Map<String, String> formulasMap;
    private Map<String, Double> solutionsMap;
    private String currentShape;

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

    // Color constants
    private final Color orange = new Color(245, 158, 11);
    private final Color gray   = new Color(229, 231, 235);
    private final Color red    = new Color(239, 68, 68);

    public Task6Screen() {
        if (flag6 == 0) {
            ShapevilleMainContent.updateProgress();
            flag6 = 1;
        }
        setTitle("Task 5: Sector Area Calculation");
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

        // Initialize data for 8 sector questions
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

        // 1) 90°, r=8 cm
        availableShapes.add("Shape 1");
        formulasMap.put("Shape 1", "A = 90/360 × π × 8² = 1/4 × 3.14 × 64 = 50.24 cm²");
        solutionsMap.put("Shape 1", 50.24);

        // 2) 130°, r=18 ft
        availableShapes.add("Shape 2");
        formulasMap.put("Shape 2", "A = 130/360 × π × 18² = (130/360) × 3.14 × 324 ≈ 367.38 ft²");
        solutionsMap.put("Shape 2", 367.38);

        // 3) 240°, r=19 cm
        availableShapes.add("Shape 3");
        formulasMap.put("Shape 3", "A = 240/360 × π × 19² = 2/3 × 3.14 × 361 = 755.69 cm²");
        solutionsMap.put("Shape 3", 755.69);

        // 4) 110°, r=22 ft
        availableShapes.add("Shape 4");
        formulasMap.put("Shape 4", "A = 110/360 × π × 22² = (110/360) × 3.14 × 484 = 464.37 ft²");
        solutionsMap.put("Shape 4", 464.37);

        // 5) 100°, r=3.5 m
        availableShapes.add("Shape 5");
        formulasMap.put("Shape 5", "A = 100/360 × π × 3.5² = 10.68 m²");
        solutionsMap.put("Shape 5", 10.68);

        // 6) 270°, r=8 in
        availableShapes.add("Shape 6");
        formulasMap.put("Shape 6", "A = 270/360 × π × 8² = 150.72 in²");
        solutionsMap.put("Shape 6", 150.72);

        // 7) 280°, r=12 yd
        availableShapes.add("Shape 7");
        formulasMap.put("Shape 7", "A = 280/360 × π × 12² = 351.68 yd²");
        solutionsMap.put("Shape 7", 351.68);

        // 8) 250°, r=15 mm
        availableShapes.add("Shape 8");
        formulasMap.put("Shape 8", "A = 250/360 × π × 15² = 490.63 mm²");
        solutionsMap.put("Shape 8", 490.63);
    }

    private void selectNext() {
        countdownTimer.stop();
        if (availableShapes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have practiced all sectors.");
            dispose();
            return;
        }
        String[] options = availableShapes.toArray(new String[0]);
        String sel = (String) JOptionPane.showInputDialog(
                this,
                "请选择一个扇形",
                "选择扇形",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (sel == null) return;
        currentShape = sel;
        availableShapes.remove(sel);
        loadShape(sel);
    }

    private void loadShape(String shapeName) {
        correctFormula = formulasMap.get(shapeName);
        correctSolution = solutionsMap.get(shapeName);
        attempts = 3;
        updateAttempts();
        hintLabel.setText("You have 3 attempts.");
        answerField.setText("");
        submitButton.setEnabled(true);
        nextButton.setVisible(false);

        shapePanel.removeAll();
        ImageIcon icon = new ImageIcon(getClass().getClassLoader()
                .getResource("images/task6/" + shapeName + ".png"));
        shapePanel.add(new JLabel(icon));
        shapePanel.revalidate(); shapePanel.repaint();

        int done = formulasMap.size() - availableShapes.size() - 1;
        progressBar.setValue(done);
        progressLabel.setText("Completed: " + done + "/" + formulasMap.size());

        remainingSeconds = 300;
        timerLabel.setText("Time left: 05:00");
        countdownTimer.start();
    }

    // 更新尝试次数显示
    private void updateAttempts() {
        String attemptsText = "<html>Attempts: ";
        for (int i = 0; i < 3; i++) {
            if (attempts == 3) {
                if (i == 0) attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>";
                else attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>";
            } else if (attempts == 2) {
                if (i == 0) attemptsText += "<font color='" + getColorHex(red) + "'>● </font>";
                else if (i == 1) attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>";
                else attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>";
            } else if (attempts == 1) {
                if (i < 2) attemptsText += "<font color='" + getColorHex(red) + "'>● </font>";
                else attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>";
            } else {
                attemptsText += "<font color='" + getColorHex(red) + "'>● </font>";
            }
        }
        attemptsText += "</html>";
        attemptDots.setText(attemptsText);
    }

    private String getColorHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private void onSubmit() {
        try {
            double ans = Double.parseDouble(answerField.getText().trim());
            if (Math.abs(ans - correctSolution) < 0.01) onCorrect();
            else {
                attempts--;
                updateAttempts();
                if (attempts > 0) hintLabel.setText("Incorrect. Attempts left: " + attempts);
                else { countdownTimer.stop(); showSolution(); }
            }
        } catch (NumberFormatException ex) {
            hintLabel.setText("请输入有效数字");
        }
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
        SwingUtilities.invokeLater(() -> new Task6Screen().setVisible(true));
    }
}
