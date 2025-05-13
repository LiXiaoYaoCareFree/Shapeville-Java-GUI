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

public class Task4Screen extends JFrame {
    private Queue<String> modesQueue;
    private final int totalModes = 4;
    private int currentModeIndex = 0;
    private boolean firstIsArea;

    private int attempts;
    private double correctResult;
    private int value;  // 半径或直径

    // UI 组件
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int remainingSeconds = 180;
    private CardPanel cardPanel;

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
        String[] options = {"Area", "Circumference"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "请选择先练习的计算类型:",
                "选择计算类型",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
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
        JPanel topWrapper = getJPanel();
        TopNavBarPanel topNav = new TopNavBarPanel();
        topWrapper.add(topNav);
        add(topWrapper, BorderLayout.NORTH);
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
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(progressBar);
        add(east, BorderLayout.EAST);

        // 中：题目卡片
        cardPanel = new CardPanel();
        add(cardPanel, BorderLayout.CENTER);

        bindTimer();
        setLocationRelativeTo(null);
        setVisible(true);
        loadNextMode();
    }

    private void bindTimer() {
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText(String.format("Time: %02d:%02d",
                    remainingSeconds / 60, remainingSeconds % 60));
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
            JOptionPane.showMessageDialog(this, "练习结束！");
            dispose();
            return;
        }
        attempts = 3;
        remainingSeconds = 180;
        countdownTimer.restart();
        cardPanel.resetForNewQuestion();

        // 更新进度
        progressLabel.setText("Progress: " + currentModeIndex + " / " + totalModes);
        progressBar.setValue(currentModeIndex);

        String mode = modesQueue.poll();
        currentModeIndex++;
        Random rnd = new Random();
        value = rnd.nextInt(20) + 1;

        switch (mode) {
            case "Area with Radius":      correctResult = Math.PI * value * value; break;
            case "Area with Diameter":    correctResult = Math.PI * value * value / 4.0; break;
            case "Circumference with Radius":   correctResult = 2 * Math.PI * value; break;
            default:                        correctResult = Math.PI * value;
        }
        cardPanel.updateQuestion(mode, value, correctResult, this::onSubmit);
    }

    private void onSubmit() {
        try {
            double ans = Double.parseDouble(cardPanel.inputField.getText().trim());
            if (Math.abs(ans - correctResult) < 1e-2) {
                cardPanel.showFeedback("Correct! 🎉");
                finishRound();
            } else {
                attempts--;
                if (attempts > 0) {
                    cardPanel.showFeedback("Incorrect, " + attempts + " attempts left");
                } else {
                    revealAnswer();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请输入数字格式答案");
        }
    }

    private void finishRound() {
        countdownTimer.stop();
        cardPanel.showFormulaAndNext(this::loadNextMode);
    }

    private void revealAnswer() {
        cardPanel.showFeedback(String.format("Answer: %.2f", correctResult));
        finishRound();
    }

    /** 卡片式面板及画布，代码与之前保持一致 **/
    private class CardPanel extends JPanel {
        private CircleCanvas canvas;
        private JLabel title, formulaLabel, paramLabel, feedbackLabel;
        private JTextField inputField;
        private JButton submitBtn, nextBtn;

        CardPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 249, 254));
            setBorder(new RoundedBorder(16));

            title = new JLabel("", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 24));
            title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
            add(title, BorderLayout.NORTH);

            canvas = new CircleCanvas();
            add(canvas, BorderLayout.CENTER);

            JPanel south = new JPanel();
            south.setOpaque(false);
            south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
            south.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));

            formulaLabel = new JLabel("Formula: ");
            south.add(formulaLabel);
            paramLabel = new JLabel();
            south.add(paramLabel);

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            row.setOpaque(false);
            row.add(new JLabel("Your answer ="));
            inputField = new JTextField(6);
            row.add(inputField);
            row.add(new JLabel("cm"));
            submitBtn = new JButton("Submit");
            row.add(submitBtn);
            feedbackLabel = new JLabel("3 attempts");
            row.add(feedbackLabel);
            nextBtn = new JButton("Next");
            nextBtn.setVisible(false);
            row.add(nextBtn);

            south.add(Box.createVerticalStrut(8));
            south.add(row);
            add(south, BorderLayout.SOUTH);
        }

        void updateQuestion(String mode, int val, double correct, Runnable cb) {
            title.setText(mode);
            formulaLabel.setText("Formula: " + canvas.getFormulaText(mode));
            paramLabel.setText("Given: " + (mode.contains("Radius") ? "r = " : "d = ") + val + " cm");
            canvas.setQuestion(mode, val);

            submitBtn.setVisible(true);
            nextBtn.setVisible(false);
            feedbackLabel.setText("You have 3 attempts");
            inputField.setText("");

            for (ActionListener al : submitBtn.getActionListeners()) submitBtn.removeActionListener(al);
            submitBtn.addActionListener(e -> cb.run());
            for (ActionListener al : nextBtn.getActionListeners()) nextBtn.removeActionListener(al);
            nextBtn.addActionListener(e -> cb.run());

            repaint();
        }

        void showFormulaAndNext(Runnable nextCb) {
            canvas.showResult = true;
            submitBtn.setVisible(false);
            nextBtn.setVisible(true);
            for (ActionListener al : nextBtn.getActionListeners()) nextBtn.removeActionListener(al);
            nextBtn.addActionListener(e -> nextCb.run());
            repaint();
        }

        void resetForNewQuestion() {
            canvas.showResult = false;
            repaint();
        }

        void showFeedback(String text) {
            feedbackLabel.setText(text);
        }
    }

    private class CircleCanvas extends JPanel {
        private String mode;
        private int val;
        boolean showResult = false;
        private static final int SCALE = 10;

        void setQuestion(String m, int v) {
            mode = m;
            val = v;
        }

        String getFormulaText(String m) {
            switch (m) {
                case "Area with Radius":      return "A = π × r²";
                case "Area with Diameter":    return "A = π / 4 × d²";
                case "Circumference with Radius":   return "C = 2π × r";
                default:                      return "C = π × d";
            }
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            if (mode == null) return;
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int radiusPx = (mode.contains("Radius") ? val : val / 2) * SCALE;
            int diameterPx = val * SCALE;
            int cx = getWidth() / 2, cy = getHeight() / 2;

            g.setColor(new Color(100, 149, 237));
            g.fillOval(cx - radiusPx, cy - radiusPx, 2 * radiusPx, 2 * radiusPx);
            g.setColor(Color.BLACK);
            g.drawOval(cx - radiusPx, cy - radiusPx, 2 * radiusPx, 2 * radiusPx);

            g.setColor(Color.RED);
            if (mode.contains("Radius")) {
                drawDimension(g, cx, cy, cx + radiusPx, cy, "r = " + val + " cm");
            } else {
                drawDimension(g, cx - diameterPx/2, cy, cx + diameterPx/2, cy, "d = " + val + " cm");
            }

            if (showResult) {
                String resTxt;
                if (mode.startsWith("Area")) {
                    resTxt = String.format("A = %.2f cm²", correctResult);
                } else {
                    resTxt = String.format("C = %.2f cm", correctResult);
                }
                FontMetrics fm = g.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(resTxt)) / 2;
                g.setColor(Color.RED);
                g.drawString(resTxt, tx, cy + radiusPx + 30);
            }
        }

        private void drawDimension(Graphics2D g, int x1, int y1, int x2, int y2, String label) {
            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(2));
            g.drawLine(x1, y1, x2, y2);
            drawArrowHead(g, x1, y1, x2, y2);
            drawArrowHead(g, x2, y2, x1, y1);
            FontMetrics fm = g.getFontMetrics();
            int mx = (x1 + x2) / 2, my = (y1 + y2) / 2;
            g.drawString(label, mx + 4, my - 4);
            g.setStroke(old);
        }

        private void drawArrowHead(Graphics2D g, int x, int y, int tx, int ty) {
            double phi = Math.toRadians(20), barb = 10;
            double theta = Math.atan2(y - ty, x - tx);
            double x1 = x - barb * Math.cos(theta + phi);
            double y1 = y - barb * Math.sin(theta + phi);
            double x2 = x - barb * Math.cos(theta - phi);
            double y2 = y - barb * Math.sin(theta - phi);
            g.draw(new Line2D.Double(x, y, x1, y1));
            g.draw(new Line2D.Double(x, y, x2, y2));
        }
    }

    private static class RoundedBorder implements Border {
        private final int r;
        RoundedBorder(int radius) { this.r = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(r,r,r,r); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200,200,200));
            g2.drawRoundRect(x, y, w-1, h-1, r, r);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task4Screen::new);
    }
}
