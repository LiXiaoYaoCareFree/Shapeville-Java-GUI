package com.Shapeville;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.*;
import static com.Shapeville.ShapevilleGUI.getJPanel;
import static com.Shapeville.ShapevilleMainContent.flag2;
import static com.Shapeville.ShapevilleMainContent.flag4;

/**
 * Circle-practice window used by Shapeville's geometry course.
 * <p>
 * The frame queues four modes—area / circumference calculated from either
 * radius or diameter—so every combination is attempted once per session.
 * A timer (3 min/question), progress bar and colour-blind-aware palette
 * provide real-time feedback.  Each round randomly picks an integer radius
 * or diameter (1–20 cm), draws the circle with dimensions, and lets the
 * learner type the result; three wrong answers or a timeout reveal the
 * correct formula and value.  After all four modes the window disposes and
 * returns control to the main GUI.  Colours update through {@link
 * ColorManager} when colour-blind mode toggles via the parent frame.
 * <p>
 * Author : Lingyuan Li
 */
public class Task4Screen extends JFrame implements ColorRefreshable {
    /** Queue to store the sequence of practice modes */
    private Queue<String> modesQueue;
    
    /** Total number of practice modes in a session */
    private final int totalModes = 4;
    
    /** Current index of the practice mode */
    private int currentModeIndex = 0;
    
    /** Flag indicating if the first practice type is area calculation */
    private boolean firstIsArea;

    /** Number of remaining attempts for the current question */
    private int attempts;
    
    /** The correct result for the current question */
    private double correctResult;
    
    /** The radius or diameter value for the current question */
    private int value;

    /** Label displaying the progress information */
    private JLabel progressLabel;
    
    /** Progress bar showing completion status */
    private JProgressBar progressBar;
    
    /** Label displaying the countdown timer */
    private JLabel timerLabel;
    
    /** Timer for countdown functionality */
    private Timer countdownTimer;
    
    /** Remaining seconds for the current question */
    private int remainingSeconds = 180;
    
    /** Panel containing the question card */
    private CardPanel cardPanel;
    
    /** Wrapper panel for the gradient top section */
    private JPanel gradientTopWrapper;

    /** Blue color for UI elements */
    private Color blue = ColorManager.getBlue();
    
    /** Green color for UI elements */
    private Color green = ColorManager.getGreen();
    
    /** Red color for UI elements */
    private Color red = ColorManager.getRed();
    
    /** Color for the progress bar */
    private Color progressBarColor = ColorManager.getProgressBarColor();

    /**
     * Constructs a new Task4Screen instance.
     * Initializes the UI components and starts the practice session.
     */
    public Task4Screen() {
        if (flag4 == 0) {
            ShapevilleMainContent.updateProgress();
            flag4 = 1;
        }
        setTitle("Task 4: Circle Area & Circumference");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 初始选择：先练习 Area 还是 Circumference
        String[] options = { "Area", "Circumference" };
        int choice = JOptionPane.showOptionDialog(
                this,
                "Please select the type of calculation to practice first:",
                "Select the calculation type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice < 0) {
            dispose();
            return;
        }
        firstIsArea = (choice == 0);
        modesQueue = new LinkedList<>();
        if (firstIsArea) {
            modesQueue.add("Area with Radius");
            modesQueue.add("Area with Diameter");
        } else {
            modesQueue.add("Circumference with Radius");
            modesQueue.add("Circumference with Diameter");
        }

        // 北：导航栏
        gradientTopWrapper = getJPanel();
        TopNavBarPanel topNav = new TopNavBarPanel();
        gradientTopWrapper.add(topNav);
        add(gradientTopWrapper, BorderLayout.NORTH);
        topNav.homeButton.addActionListener(e -> dispose());
        topNav.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "You completed " + currentModeIndex + " / " + totalModes);
            dispose();
        });

        // 东：计时与进度
        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        timerLabel = new JLabel("Time: 03:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(timerLabel);
        east.add(Box.createVerticalStrut(30));
        progressLabel = new JLabel("Progress: 0 / " + totalModes, SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 20));
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(progressLabel);
        east.add(Box.createVerticalStrut(10));
        progressBar = new JProgressBar(0, totalModes);
        progressBar.setValue(0);
        progressBar.setForeground(progressBarColor);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(progressBar);
        add(east, BorderLayout.EAST);

        // 中：题目卡片
        cardPanel = new CardPanel();
        add(cardPanel, BorderLayout.CENTER);

        bindTimer();
        setLocationRelativeTo(null);
        loadNextMode();
    }

    /**
     * Refreshes all UI elements' colors in response to color-blind mode changes.
     * Updates colors for progress bar, card panel, timer, and gradient background.
     */
    @Override
    public void refreshColors() {
        System.out.println("Task4Screen is refreshing the color...");

        // 更新颜色常量
        blue = ColorManager.getBlue();
        green = ColorManager.getGreen();
        red = ColorManager.getRed();
        progressBarColor = ColorManager.getProgressBarColor();

        // 更新进度条颜色
        if (progressBar != null) {
            progressBar.setForeground(progressBarColor);
        }

        // 更新卡片面板上的组件颜色
        if (cardPanel != null) {
            cardPanel.refreshColors();
        }

        // 更新计时器颜色
        if (timerLabel != null && remainingSeconds <= 60) {
            timerLabel.setForeground(red);
        } else if (timerLabel != null) {
            timerLabel.setForeground(Color.BLACK);
        }

        // 刷新渐变背景
        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        repaint();
    }

    /**
     * Initializes and binds the countdown timer functionality.
     * Updates the timer display and handles timeout conditions.
     */
    private void bindTimer() {
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText(String.format("Time: %02d:%02d",
                    remainingSeconds / 60, remainingSeconds % 60));

            // 当剩余时间少于1分钟时变红
            if (remainingSeconds == 60) {
                timerLabel.setForeground(red);
            }

            if (remainingSeconds <= 0) {
                countdownTimer.stop();
                revealAnswer();
            }
        });
    }

    /**
     * Loads the next practice mode and initializes a new question.
     * Updates the UI and resets the timer and attempts counter.
     */
    private void loadNextMode() {
        // 第三题之前，自动添加另一类型的两题
        if (currentModeIndex == 2) {
            if (firstIsArea) {
                modesQueue.add("Circumference with Radius");
                modesQueue.add("Circumference with Diameter");
            } else {
                modesQueue.add("Area with Radius");
                modesQueue.add("Area with Diameter");
            }
        }
        if (currentModeIndex >= totalModes) {
            JOptionPane.showMessageDialog(this, "The practice is over!");
            dispose();
            return;
        }
        attempts = 3;
        remainingSeconds = 180;
        countdownTimer.restart();
        timerLabel.setForeground(Color.BLACK); // 重置计时器颜色
        cardPanel.resetForNewQuestion();

        // 更新进度
        progressLabel.setText("Progress: " + currentModeIndex + " / " + totalModes);
        progressBar.setValue(currentModeIndex);

        String mode = modesQueue.poll();
        currentModeIndex++;
        Random rnd = new Random();
        value = rnd.nextInt(20) + 1;

        switch (mode) {
            case "Area with Radius":
                correctResult = Math.PI * value * value;
                break;
            case "Area with Diameter":
                correctResult = Math.PI * value * value / 4.0;
                break;
            case "Circumference with Radius":
                correctResult = 2 * Math.PI * value;
                break;
            default:
                correctResult = Math.PI * value;
        }
        cardPanel.updateQuestion(mode, value, correctResult, this::onSubmit);
    }

    /**
     * Handles the submission of an answer.
     * Validates the input and checks if it matches the correct result.
     */
    private void onSubmit() {
        try {
            double ans = Double.parseDouble(cardPanel.inputField.getText().trim());
            if (Math.abs(ans - correctResult) < 1e-2) {
                cardPanel.showFeedback("Correct! ", green);
                finishRound();
            } else {
                attempts--;
                if (attempts > 0) {
                    cardPanel.showFeedback("Incorrect, " + attempts + " attempts left", red);
                } else {
                    revealAnswer();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter the answer in numerical format");
        }
    }

    /**
     * Completes the current round and prepares for the next question.
     * Stops the timer and shows the formula.
     */
    private void finishRound() {
        countdownTimer.stop();
        cardPanel.showFormulaAndNext(this::loadNextMode);
    }

    /**
     * Reveals the correct answer when time runs out or attempts are exhausted.
     * Shows the correct result and prepares for the next question.
     */
    private void revealAnswer() {
        cardPanel.showFeedback(String.format("Answer: %.2f", correctResult), red);
        finishRound();
    }

    /**
     * Inner class representing the card panel that displays questions and handles user input.
     */
    private class CardPanel extends JPanel {
        /** Canvas for drawing the circle */
        private CircleCanvas canvas;
        
        /** Label for the question title */
        private JLabel title;
        
        /** Label for displaying formulas */
        private JLabel formulaLabel;
        
        /** Label for displaying parameters */
        private JLabel paramLabel;
        
        /** Label for feedback messages */
        private JLabel feedbackLabel;
        
        /** Text field for user input */
        private JTextField inputField;
        
        /** Button for submitting answers */
        private JButton submitBtn;
        
        /** Button for proceeding to next question */
        private JButton nextBtn;
        
        /** Callback for submit action */
        private Runnable submitCallback;
        
        /** Callback for next question action */
        private Runnable nextCallback;

        /**
         * Constructs a new CardPanel with all necessary UI components.
         */
        CardPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 249, 254));
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // 北：标题
            title = new JLabel("Circle Area Calculation with Radius", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 20));
            title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            add(title, BorderLayout.NORTH);

            // 中：显示圆形的面板
            canvas = new CircleCanvas();
            add(canvas, BorderLayout.CENTER);

            // 南：输入控件和反馈
            JPanel south = new JPanel();
            south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
            south.setOpaque(false);

            // 参数标签
            paramLabel = new JLabel("Radius = " + value + " cm");
            paramLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            paramLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            south.add(paramLabel);

            // 公式标签（默认隐藏）
            formulaLabel = new JLabel("");
            formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            formulaLabel.setFont(new Font("Arial", Font.BOLD, 16));
            formulaLabel.setVisible(false);
            south.add(formulaLabel);

            // 输入区域
            JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
            inputRow.setOpaque(false);
            inputRow.add(new JLabel("Result = "));
            inputField = new JTextField(10);
            inputField.addActionListener(e -> onSubmit());
            inputRow.add(inputField);
            inputRow.add(new JLabel("cm²"));

            // 提交按钮
            submitBtn = new JButton("Submit");
            submitBtn.setBackground(blue);
            submitBtn.setForeground(Color.WHITE);
            inputRow.add(submitBtn);
            south.add(inputRow);

            // 反馈标签
            feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
            feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
            south.add(feedbackLabel);

            // 下一题按钮（默认隐藏）
            nextBtn = new JButton("Next Question");
            nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            nextBtn.setBackground(green);
            nextBtn.setForeground(Color.WHITE);
            nextBtn.setVisible(false);
            south.add(nextBtn);

            // 加入底部面板
            add(south, BorderLayout.SOUTH);

            // 绑定按钮事件
            submitBtn.addActionListener(e -> {
                if (submitCallback != null)
                    submitCallback.run();
            });
            nextBtn.addActionListener(e -> {
                if (nextCallback != null)
                    nextCallback.run();
            });
        }

        /**
         * Refreshes the colors of all UI elements in the card panel.
         */
        void refreshColors() {
            if (submitBtn != null) {
                submitBtn.setBackground(blue);
                submitBtn.setForeground(Color.WHITE);
            }

            if (nextBtn != null) {
                nextBtn.setBackground(green);
                nextBtn.setForeground(Color.WHITE);
            }

            if (feedbackLabel != null) {
                String text = feedbackLabel.getText();
                if (text.contains("Correct")) {
                    feedbackLabel.setForeground(green);
                } else if (text.contains("Incorrect") || text.contains("Answer:")) {
                    feedbackLabel.setForeground(red);
                }
            }

            // 刷新画布
            if (canvas != null) {
                canvas.repaint();
            }

            repaint();
        }

        /**
         * Updates the question display with new content.
         * @param mode The practice mode
         * @param val The radius or diameter value
         * @param correct The correct result
         * @param cb Callback for submit action
         */
        void updateQuestion(String mode, int val, double correct, Runnable cb) {
            title.setText(getTitle(mode));
            paramLabel.setText(getParamText(mode, val));
            submitCallback = cb;
            inputField.setText("");
            inputField.setEnabled(true);
            submitBtn.setEnabled(true);
            feedbackLabel.setText(" ");
            formulaLabel.setVisible(false);
            canvas.setQuestion(mode, val);
            canvas.showResult = false;
            nextBtn.setVisible(false);
            inputField.requestFocus();
        }

        /**
         * Shows the formula and next question button.
         * @param nextCb Callback for next question action
         */
        void showFormulaAndNext(Runnable nextCb) {
            nextCallback = nextCb;
            canvas.showResult = true;
            canvas.repaint();
            formulaLabel.setText(canvas.getFormulaText(canvas.mode));
            formulaLabel.setVisible(true);
            submitBtn.setEnabled(false);
            inputField.setEnabled(false);
            nextBtn.setVisible(true);
        }

        /**
         * Resets the panel for a new question.
         */
        void resetForNewQuestion() {
            inputField.setText("");
            feedbackLabel.setText(" ");
            feedbackLabel.setForeground(Color.BLACK);
        }

        /**
         * Displays feedback message with specified color.
         * @param text The feedback message
         * @param color The color for the message
         */
        void showFeedback(String text, Color color) {
            feedbackLabel.setText(text);
            feedbackLabel.setForeground(color);
        }

        /**
         * Gets the title text based on the practice mode.
         * @param mode The practice mode
         * @return The formatted title text
         */
        String getTitle(String mode) {
            return mode.contains("Area") ? "Circle Area Calculation" : "Circle Circumference Calculation";
        }

        /**
         * Gets the parameter text based on the practice mode.
         * @param mode The practice mode
         * @param val The radius or diameter value
         * @return The formatted parameter text
         */
        String getParamText(String mode, int val) {
            if (mode.contains("Radius")) {
                return "Radius = " + val + " cm";
            } else {
                return "Diameter = " + val + " cm";
            }
        }
    }

    /**
     * Inner class for drawing the circle and its measurements.
     */
    private class CircleCanvas extends JPanel {
        /** Current practice mode */
        private String mode;
        
        /** Current radius or diameter value */
        private int val;
        
        /** Flag indicating if result should be shown */
        boolean showResult = false;
        
        /** Scale factor for drawing */
        private static final int SCALE = 10;

        /**
         * Sets the question parameters for the canvas.
         * @param m The practice mode
         * @param v The radius or diameter value
         */
        void setQuestion(String m, int v) {
            this.mode = m;
            this.val = v;
            repaint();
        }

        /**
         * Gets the formula text based on the practice mode.
         * @param m The practice mode
         * @return The formatted formula text
         */
        String getFormulaText(String m) {
            if (m.startsWith("Area with Radius")) {
                return String.format("Area = π × r² = π × %d² ≈ %.2f", val, Math.PI * val * val);
            } else if (m.startsWith("Area with Diameter")) {
                return String.format("Area = π × (d/2)² = π × (%d/2)² ≈ %.2f", val, Math.PI * val * val / 4);
            } else if (m.startsWith("Circumference with Radius")) {
                return String.format("Circumference = 2π × r = 2π × %d ≈ %.2f", val, 2 * Math.PI * val);
            } else {
                return String.format("Circumference = π × d = π × %d ≈ %.2f", val, Math.PI * val);
            }
        }

        /**
         * Paints the circle and its measurements.
         * @param g0 The graphics context
         */
        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            if (mode == null)
                return;

            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            int rawRadius;
            if (mode.contains("Diameter")) {
                rawRadius = val / 2; // 如果给定直径，取半
            } else {
                rawRadius = val; // 给定半径，直接用
            }

            int radius = rawRadius * SCALE;

            // 绘制圆的填充部分
            g.setColor(ColorManager.adaptColor(new Color(173, 216, 230))); // 浅蓝色
            g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

            // 绘制边框
            g.setColor(Color.BLACK);
            g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

            // 绘制中心点
            g.fillOval(centerX - 3, centerY - 3, 6, 6);

            // 如果是直径模式，绘制直径线
            if (mode.contains("Diameter")) {
                g.drawLine(centerX - radius, centerY, centerX + radius, centerY);
                // 直径标注
                drawDimension(g, centerX - radius, centerY + 15, centerX + radius, centerY + 15,
                        val + " cm");
            } else {
                // 半径标注
                g.drawLine(centerX, centerY, centerX + radius, centerY);
                drawDimension(g, centerX, centerY + 15, centerX + radius, centerY + 15,
                        val + " cm");
            }
        }

        /**
         * Draws dimension lines with arrows and labels.
         * @param g The graphics context
         * @param x1 Start x-coordinate
         * @param y1 Start y-coordinate
         * @param x2 End x-coordinate
         * @param y2 End y-coordinate
         * @param label The dimension label
         */
        private void drawDimension(Graphics2D g, int x1, int y1, int x2, int y2, String label) {
            g.setColor(ColorManager.adaptColor(new Color(50, 50, 50)));
            g.draw(new Line2D.Double(x1, y1, x2, y2));
            drawArrowHead(g, x1, y1, x2, y2);
            drawArrowHead(g, x2, y2, x1, y1);

            FontMetrics fm = g.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g.drawString(label, (x1 + x2) / 2 - labelWidth / 2, y1 + fm.getHeight() + 2);
        }

        /**
         * Draws an arrow head at the specified position.
         * @param g The graphics context
         * @param x Arrow head x-coordinate
         * @param y Arrow head y-coordinate
         * @param tx Target x-coordinate
         * @param ty Target y-coordinate
         */
        private void drawArrowHead(Graphics2D g, int x, int y, int tx, int ty) {
            int ARR_SIZE = 6;
            double dx = tx - x;
            double dy = ty - y;
            double angle = Math.atan2(dy, dx);

            g.fillPolygon(
                    new int[] { x, (int) (x - ARR_SIZE * Math.cos(angle - Math.PI / 6)),
                            (int) (x - ARR_SIZE * Math.cos(angle + Math.PI / 6)) },
                    new int[] { y, (int) (y - ARR_SIZE * Math.sin(angle - Math.PI / 6)),
                            (int) (y - ARR_SIZE * Math.sin(angle + Math.PI / 6)) },
                    3);
        }
    }

    /**
     * Inner class implementing a rounded border.
     */
    private static class RoundedBorder implements Border {
        /** Border radius */
        private final int r;

        /**
         * Constructs a new RoundedBorder with specified radius.
         * @param radius The border radius
         */
        RoundedBorder(int radius) {
            this.r = radius;
        }

        /**
         * Gets the border insets.
         * @param c The component
         * @return The border insets
         */
        public Insets getBorderInsets(Component c) {
            return new Insets(r, r, r, r);
        }

        /**
         * Checks if the border is opaque.
         * @return false as the border is not opaque
         */
        public boolean isBorderOpaque() {
            return false;
        }

        /**
         * Paints the rounded border.
         * @param c The component
         * @param g The graphics context
         * @param x The x-coordinate
         * @param y The y-coordinate
         * @param w The width
         * @param h The height
         */
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ColorManager.adaptColor(new Color(200, 200, 200)));
            g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
        }
    }

    /**
     * Main method for testing the Task4Screen.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task4Screen().setVisible(true));
    }
}
