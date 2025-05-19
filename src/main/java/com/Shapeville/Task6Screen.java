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

/**
 * Interactive window for practising the area of circular sectors.
 * <p>
 * Eight preset exercises describe a sector by central angle and radius.
 * The learner selects a case, inspects the drawn sector, and types the
 * area; three attempts and a 5-minute countdown are allowed.  A 5 %
 * tolerance recognises small rounding differences.  Traffic-light dots,
 * a live timer, and a progress bar show current status.  Correct answers
 * or exhausted attempts/time reveal the full worked formula.  Colours
 * refresh instantly through {@link ColorManager} when colour-blind mode
 * is toggled, in line with the {@link ColorRefreshable} protocol.
 * When all sectors are completed the frame disposes and returns control
 * to Shapeville’s main GUI.
 * <p>
 * Author : Lingyuan Li
 */
public class Task6Screen extends JFrame implements ColorRefreshable {
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
    private JPanel gradientTopWrapper;

    // Color constants - 使用ColorManager
    private Color orange = ColorManager.getOrange();
    private Color gray = ColorManager.getGray();
    private Color red = ColorManager.getRed();
    private Color green = ColorManager.getGreen();
    private Color blue = ColorManager.getBlue();
    private Color progressBarColor = ColorManager.getProgressBarColor();

    public Task6Screen() {
        if (flag6 == 0) {
            ShapevilleMainContent.updateProgress();
            flag6 = 1;
        }
        setTitle("Task 6: Sector Area Calculation");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top navigation bar
        gradientTopWrapper = getJPanel();
        TopNavBarPanel top = new TopNavBarPanel();
        gradientTopWrapper.add(top);
        add(gradientTopWrapper, BorderLayout.NORTH);
        top.homeButton.addActionListener(e -> dispose());
        top.endSessionButton.addActionListener(e -> dispose());

        // Initialize data for 8 sector questions
        initializeData();

        // Progress bar
        progressBar = new JProgressBar(0, formulasMap.size());
        progressBar.setForeground(progressBarColor);
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
        shapePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentShape == null)
                    return;

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(getWidth(), getHeight()) / 3;

                Map<String, Double> params = getSectorParameters(currentShape);
                double angle = params.get("angle");
                double r = params.get("radius");

                g2d.setColor(ColorManager.adaptColor(new Color(173, 216, 230)));
                g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, -(int) angle);

                g2d.setColor(Color.BLACK);
                g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, -(int) angle);
                g2d.drawLine(centerX, centerY, centerX + radius, centerY);

                double endRadian = Math.toRadians(angle);
                int x2 = centerX + (int) (radius * Math.cos(endRadian));
                int y2 = centerY + (int) (radius * Math.sin(endRadian));
                g2d.drawLine(centerX, centerY, x2, y2);

                // 标注角度
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString(angle + "°", centerX + radius / 3, centerY - radius / 3);

                // 标注半径
                g2d.drawString("r = " + r, centerX + radius / 2, centerY + 15);
            }
        };
        shapePanel.setPreferredSize(new Dimension(400, 400));
        mainPanel.add(shapePanel);

        // Timer display
        timerLabel = new JLabel("Time left: 05:00");
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(timerLabel);
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            int mm = remainingSeconds / 60;
            int ss = remainingSeconds % 60;
            timerLabel.setText(String.format("Time left: %02d:%02d", mm, ss));

            if (remainingSeconds == 60) {
                timerLabel.setForeground(red);
            }

            if (remainingSeconds <= 0) {
                countdownTimer.stop();
                onTimeUp();
            }
        });

        // Answer input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Area:"));
        answerField = new JTextField(10);
        answerField.addActionListener(e -> onSubmit());
        inputPanel.add(answerField);
        submitButton = new JButton("Submit");
        submitButton.setBackground(blue);
        submitButton.setForeground(Color.WHITE);
        inputPanel.add(submitButton);
        mainPanel.add(inputPanel);

        hintLabel = new JLabel("You have 3 attempts.");
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(hintLabel);

        JPanel attemptsPanel = new JPanel();
        attemptsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 中间对齐
        attemptDots = new JLabel();
        attemptsPanel.add(attemptDots);
        mainPanel.add(attemptsPanel);

        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setBackground(green);
        nextButton.setForeground(Color.WHITE);
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

    /**
     * Refresh the colors of all UI elements to respond to the changes in the color blindness mode
     */
    @Override
    public void refreshColors() {
        System.out.println("Task6Screen正在刷新颜色...");

        orange = ColorManager.getOrange();
        gray = ColorManager.getGray();
        red = ColorManager.getRed();
        green = ColorManager.getGreen();
        blue = ColorManager.getBlue();
        progressBarColor = ColorManager.getProgressBarColor();

        if (progressBar != null) {
            progressBar.setForeground(progressBarColor);
        }

        if (submitButton != null) {
            submitButton.setBackground(blue);
            submitButton.setForeground(Color.WHITE);
        }

        if (nextButton != null) {
            nextButton.setBackground(green);
            nextButton.setForeground(Color.WHITE);
        }

        if (hintLabel != null) {
            String text = hintLabel.getText();
            if (text.contains("correct")) {
                hintLabel.setForeground(green);
            } else if (text.contains("incorrect") || text.contains("solution")) {
                hintLabel.setForeground(red);
            } else {
                hintLabel.setForeground(Color.BLACK);
            }
        }

        if (timerLabel != null && remainingSeconds <= 60) {
            timerLabel.setForeground(red);
        } else if (timerLabel != null) {
            timerLabel.setForeground(Color.BLACK);
        }

        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }
        if (shapePanel != null) {
            shapePanel.repaint();
        }

        repaint();
    }

    private Map<String, Double> getSectorParameters(String shapeName) {
        Map<String, Double> params = new HashMap<>();

        switch (shapeName) {
            case "Shape 1":
                params.put("angle", 90.0);
                params.put("radius", 8.0);
                break;
            case "Shape 2":
                params.put("angle", 130.0);
                params.put("radius", 18.0);
                break;
            case "Shape 3":
                params.put("angle", 240.0);
                params.put("radius", 19.0);
                break;
            case "Shape 4":
                params.put("angle", 110.0);
                params.put("radius", 22.0);
                break;
            case "Shape 5":
                params.put("angle", 100.0);
                params.put("radius", 3.5);
                break;
            case "Shape 6":
                params.put("angle", 270.0);
                params.put("radius", 8.0);
                break;
            case "Shape 7":
                params.put("angle", 280.0);
                params.put("radius", 12.0);
                break;
            case "Shape 8":
                params.put("angle", 250.0);
                params.put("radius", 15.0);
                break;
            default:
                params.put("angle", 90.0);
                params.put("radius", 10.0);
        }

        return params;
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
                "Please select a sector",
                "Select the sector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (sel == null)
            return;
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
        hintLabel.setForeground(Color.BLACK);
        answerField.setText("");
        submitButton.setEnabled(true);
        nextButton.setVisible(false);

        remainingSeconds = 300;
        timerLabel.setForeground(Color.BLACK);
        countdownTimer.restart();

        shapePanel.repaint();

        progressLabel.setText("Completed: " + (8 - availableShapes.size()) + "/" + formulasMap.size());
        progressBar.setValue(8 - availableShapes.size());
    }

    private void updateAttempts() {
        attemptsText = "<html>Attempts: ";
        for (int i = 0; i < 3; i++) {
            if (attempts == 3) {
                if (i == 0)
                    attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>";
                else
                    attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>";
            } else if (attempts == 2) {
                if (i == 0)
                    attemptsText += "<font color='" + getColorHex(red) + "'>● </font>";
                else if (i == 1)
                    attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>";
                else
                    attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>";
            } else if (attempts == 1) {
                if (i == 0)
                    attemptsText += "<font color='" + getColorHex(red) + "'>● </font>";
                else if (i == 1)
                    attemptsText += "<font color='" + getColorHex(red) + "'>● </font>";
                else
                    attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>";
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
        if (currentShape == null)
            return;

        try {
            double answer = Double.parseDouble(answerField.getText().trim());
            double errorMargin = correctSolution * 0.05; // 允许5%误差

            if (Math.abs(answer - correctSolution) <= errorMargin) {
                onCorrect();
            } else {
                attempts--;
                updateAttempts();

                if (attempts > 0) {
                    hintLabel.setText("Your answer is incorrect. Try again. You have " + attempts + " attempts left.");
                    hintLabel.setForeground(red);
                } else {
                    showSolution();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void onCorrect() {
        hintLabel.setText("Your answer is correct!");
        hintLabel.setForeground(green);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
        countdownTimer.stop();
    }

    private void showSolution() {
        hintLabel.setText("The correct solution is: " + correctFormula);
        hintLabel.setForeground(red);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
        countdownTimer.stop();
    }

    private void onTimeUp() {
        hintLabel.setText("Time's up! The correct solution is: " + correctFormula);
        hintLabel.setForeground(red);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task6Screen().setVisible(true));
    }
}
