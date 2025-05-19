package com.Shapeville;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static com.Shapeville.ShapevilleGUI.getJPanel;
import static com.Shapeville.ShapevilleMainContent.flag2;
import static com.Shapeville.ShapevilleMainContent.flag5;

/**
 * Practice window for estimating the area of irregular / compound shapes.
 * <p>
 * The frame shuffles six pre-defined figures, each linked to a text
 * formula and exact numeric result.  Learners pick a shape, study the
 * picture, and type the area.  They have 3 attempts and a 5-minute
 * countdown; running out of tries or time reveals the worked solution.
 * Progress and remaining shapes are shown via a coloured progress bar,
 * three traffic-light “attempt dots,” and a live timer.  All colours
 * adapt on the fly through {@link ColorManager} when colour-blind mode
 * is toggled, thanks to the {@link ColorRefreshable} contract.
 * After every shape is answered, the window disposes and control returns
 * to the Shapeville main GUI.
 * <p>
 * Author : Lingyuan Li
 */
public class Task5Screen extends JFrame implements ColorRefreshable {
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

    // 使用ColorManager管理颜色
    private Color orange = ColorManager.getOrange();
    private Color gray = ColorManager.getGray();
    private Color red = ColorManager.getRed();
    private Color green = ColorManager.getGreen();
    private Color yellow = ColorManager.getYellow();

    private TopNavBarPanel topPanel;
    private JPanel taskPanel;
    private JPanel gradientTopWrapper;

    public Task5Screen() {
        if (flag5 == 0) {
            ShapevilleMainContent.updateProgress();
            flag5 = 1;
        }
        setTitle("Task 5: Compound Shapes");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top navigation bar
        gradientTopWrapper = getJPanel();
        topPanel = new TopNavBarPanel();
        gradientTopWrapper.add(topPanel);
        add(gradientTopWrapper, BorderLayout.NORTH);
        topPanel.homeButton.addActionListener(e -> dispose());
        topPanel.endSessionButton.addActionListener(e -> dispose());

        // Initialize data
        formulasMap = new HashMap<>();
        solutionsMap = new HashMap<>();
        initFormulasAndSolutions();
        availableShapes = new ArrayList<>(formulasMap.keySet());
        Collections.shuffle(availableShapes);

        // Task area
        taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout(10, 10));
        taskPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(taskPanel, BorderLayout.CENTER);

        // Progress bar
        progressBar = new JProgressBar(0, formulasMap.size());
        progressBar.setStringPainted(true);
        progressLabel = new JLabel("Completed: 0/" + formulasMap.size());
        JPanel progressPanel = new JPanel(new FlowLayout());
        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);
        taskPanel.add(progressPanel, BorderLayout.SOUTH);

        // Main content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Shape display
        shapePanel = new JPanel();
        mainPanel.add(shapePanel);

        // Timer display
        timerLabel = new JLabel("Time left: 05:00");
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
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
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(hintLabel);


        JPanel attemptsPanel = new JPanel();
        attemptsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 中间对齐
        attemptDots = new JLabel();
        attemptsPanel.add(attemptDots);
        mainPanel.add(attemptsPanel);

        nextButton = new JButton("Next");
        nextButton.setVisible(false);
        mainPanel.add(nextButton);

        taskPanel.add(mainPanel, BorderLayout.CENTER);

        // Event handlers
        submitButton.addActionListener(e -> onSubmit());
        nextButton.addActionListener(e -> selectNext());

        // Start first selection
        selectNext();
        setLocationRelativeTo(null);
    }

    private void initFormulasAndSolutions() {
        // Shape 2
        formulasMap.put("Shape 2", "A = 20×21 - 10×11 = 420 - 110 = 310 cm²");
        solutionsMap.put("Shape 2", 310.0);

        // Shape 3
        formulasMap.put("Shape 3", "A = (18 + 16)×19 - 16×(19 - 16) = 34×19 - 16×3 = 646 - 48 = 598 cm²");
        solutionsMap.put("Shape 3", 598.0);

        // Shape 4
        formulasMap.put("Shape 4", "A = 24×6 + 12×12 = 144 + 144 = 288 m²");
        solutionsMap.put("Shape 4", 280.0);

        // Shape 5
        formulasMap.put("Shape 5", "A = 4×3 + 1/2×4×(6 - 3) = 12 + 6 = 18 m²");
        solutionsMap.put("Shape 5", 18.0);

        // Shape 8
        formulasMap.put("Shape 8", "A = 60×36 + 36×36 = 2160 + 1296 = 3456 m²");
        solutionsMap.put("Shape 8", 3456.0);

        // Shape 9
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
                "Please select a graphic",
                "Select the graphic",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (selection == null) {
            return;
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

    private void updateAttemptsDisplay() {
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
            hintLabel.setText("Please enter significant figures");
        }
    }

    private String getColorHex(Color color) {
        return ColorManager.getColorHex(color);
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

    /**
     * Refresh the colors of all UI elements to respond to the changes in the color blindness mode
     */
    @Override
    public void refreshColors() {
        System.out.println("Task5Screen is refreshing colors...");

        orange = ColorManager.getOrange();
        gray = ColorManager.getGray();
        red = ColorManager.getRed();
        green = ColorManager.getGreen();
        yellow = ColorManager.getYellow();

        updateAttemptsDisplay();


        if (submitButton != null) {
            submitButton.setBackground(ColorManager.getBlue());
            submitButton.setForeground(Color.WHITE);
        }

        if (nextButton != null) {
            nextButton.setBackground(ColorManager.getGreen());
            nextButton.setForeground(Color.WHITE);
        }
        if (hintLabel != null) {
            String hintText = hintLabel.getText();
            if (hintText.startsWith("Correct")) {
                hintLabel.setForeground(green);
            } else if (hintText.startsWith("Incorrect") || hintText.contains("Time up")) {
                hintLabel.setForeground(red);
            }
        }

        if (progressBar != null) {
            progressBar.setForeground(ColorManager.getProgressBarColor());
        }

        if (timerLabel != null) {
            if (remainingSeconds < 60) {
                timerLabel.setForeground(red);
            } else {
                timerLabel.setForeground(Color.BLACK);
            }
        }

        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        if (taskPanel != null)
            taskPanel.repaint();
        if (shapePanel != null)
            shapePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task5Screen().setVisible(true));
    }
}
