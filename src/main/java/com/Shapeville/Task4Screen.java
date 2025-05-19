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
 * Circle-practice window used by Shapeville’s geometry course.
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
    private Queue<String> modesQueue;
    private final int totalModes = 4;
    private int currentModeIndex = 0;
    private boolean firstIsArea;

    private int attempts;
    private double correctResult;
    private int value; // 半径或直径

    // UI 组件
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int remainingSeconds = 180;
    private CardPanel cardPanel;
    private JPanel gradientTopWrapper;

    // 颜色常量 - 使用ColorManager
    private Color blue = ColorManager.getBlue();
    private Color green = ColorManager.getGreen();
    private Color red = ColorManager.getRed();
    private Color progressBarColor = ColorManager.getProgressBarColor();

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
     * 刷新所有UI元素的颜色，以响应色盲模式变化
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

    private void finishRound() {
        countdownTimer.stop();
        cardPanel.showFormulaAndNext(this::loadNextMode);
    }

    private void revealAnswer() {
        cardPanel.showFeedback(String.format("Answer: %.2f", correctResult), red);
        finishRound();
    }

    /** 卡片式面板及画布，代码与之前保持一致 **/
    private class CardPanel extends JPanel {
        private CircleCanvas canvas;
        private JLabel title, formulaLabel, paramLabel, feedbackLabel;
        private JTextField inputField;
        private JButton submitBtn, nextBtn;
        private Runnable submitCallback;
        private Runnable nextCallback;

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

        void resetForNewQuestion() {
            inputField.setText("");
            feedbackLabel.setText(" ");
            feedbackLabel.setForeground(Color.BLACK);
        }

        void showFeedback(String text, Color color) {
            feedbackLabel.setText(text);
            feedbackLabel.setForeground(color);
        }

        String getTitle(String mode) {
            return mode.contains("Area") ? "Circle Area Calculation" : "Circle Circumference Calculation";
        }

        String getParamText(String mode, int val) {
            if (mode.contains("Radius")) {
                return "Radius = " + val + " cm";
            } else {
                return "Diameter = " + val + " cm";
            }
        }
    }

    private class CircleCanvas extends JPanel {
        private String mode;
        private int val;
        boolean showResult = false;
        private static final int SCALE = 10;

        void setQuestion(String m, int v) {
            this.mode = m;
            this.val = v;
            repaint();
        }

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

        private void drawDimension(Graphics2D g, int x1, int y1, int x2, int y2, String label) {
            g.setColor(ColorManager.adaptColor(new Color(50, 50, 50)));
            g.draw(new Line2D.Double(x1, y1, x2, y2));
            drawArrowHead(g, x1, y1, x2, y2);
            drawArrowHead(g, x2, y2, x1, y1);

            FontMetrics fm = g.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g.drawString(label, (x1 + x2) / 2 - labelWidth / 2, y1 + fm.getHeight() + 2);
        }

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

    private static class RoundedBorder implements Border {
        private final int r;

        RoundedBorder(int radius) {
            this.r = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(r, r, r, r);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ColorManager.adaptColor(new Color(200, 200, 200)));
            g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task4Screen().setVisible(true));
    }
}
