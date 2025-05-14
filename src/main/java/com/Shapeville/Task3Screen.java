package com.Shapeville;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.Random;

import static com.Shapeville.ShapevilleGUI.getJPanel;
import static com.Shapeville.ShapevilleMainContent.flag2;
import static com.Shapeville.ShapevilleMainContent.flag3;

public class Task3Screen extends JFrame implements ColorRefreshable {
    private final String[] shapes = { "Rectangle", "Parallelogram", "Triangle", "Trapezoid" };
    private int currentShapeIndex = 0;
    private java.util.List<String> remainingShapes;

    private int attempts = 3;
    private double correctArea;
    private int[] dims; // 当前题的随机参数

    // UI 组件
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int remainingSeconds = 180;

    private RoundedCardPanel cardPanel; // 题目卡片
    private JTextField answerField;
    private JLabel hintLabel;
    private JPanel gradientTopWrapper;

    // 颜色常量 - 使用ColorManager
    private Color blue = ColorManager.getBlue();
    private Color green = ColorManager.getGreen();
    private Color red = ColorManager.getRed();
    private Color progressBarColor = ColorManager.getProgressBarColor();

    public Task3Screen() {
        if (flag3 == 0) {
            ShapevilleMainContent.updateProgress();
            flag3 = 1;
        }
        remainingShapes = new java.util.ArrayList<>(java.util.Arrays.asList(shapes));
        setTitle("Task 3: Shape Area Calculation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ─── 北：导航栏 ─────────────────────────
        gradientTopWrapper = getJPanel();
        TopNavBarPanel topNav = new TopNavBarPanel();
        gradientTopWrapper.add(topNav);
        add(gradientTopWrapper, BorderLayout.NORTH);
        topNav.homeButton.addActionListener(e -> dispose());
        topNav.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You scored " + currentShapeIndex + " / " + shapes.length);
            dispose();
        });

        // ─── 东：倒计时 + 进度 ────────────────────
        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        timerLabel = new JLabel("Time: 03:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(timerLabel);
        east.add(Box.createVerticalStrut(30));
        progressLabel = new JLabel("Progress: 0 / " + shapes.length, SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 20));
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(progressLabel);
        east.add(Box.createVerticalStrut(10));
        progressBar = new JProgressBar(0, shapes.length);
        progressBar.setValue(0);
        progressBar.setForeground(progressBarColor);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(progressBar);
        add(east, BorderLayout.EAST);

        // ─── 中央：卡片式题目 ────────────────────
        cardPanel = new RoundedCardPanel();
        add(cardPanel, BorderLayout.CENTER);

        bindActions();
        loadShape(); // 现在会先弹出选择对话框

        setLocationRelativeTo(null);
    }

    /**
     * 刷新所有UI元素的颜色，以响应色盲模式变化
     */
    @Override
    public void refreshColors() {
        System.out.println("Task3Screen正在刷新颜色...");

        // 更新颜色常量
        blue = ColorManager.getBlue();
        green = ColorManager.getGreen();
        red = ColorManager.getRed();
        progressBarColor = ColorManager.getProgressBarColor();

        // 更新进度条颜色
        if (progressBar != null) {
            progressBar.setForeground(progressBarColor);
        }

        // 更新卡片组件颜色
        if (cardPanel != null) {
            cardPanel.refreshColors();
        }

        // 更新计时器颜色
        if (timerLabel != null && remainingSeconds <= 60) {
            timerLabel.setForeground(red);
        }

        // 更新提示颜色
        if (hintLabel != null) {
            String hintText = hintLabel.getText();
            if (hintText.contains("正确")) {
                hintLabel.setForeground(green);
            } else if (hintText.contains("不对")) {
                hintLabel.setForeground(red);
            }
        }

        // 刷新渐变背景
        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        repaint();
    }

    /** 绑定倒计时和按钮逻辑 */
    private void bindActions() {
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText(String.format("Time: %02d:%02d", remainingSeconds / 60, remainingSeconds % 60));

            // 剩下一分钟时将计时器变为红色
            if (remainingSeconds == 60) {
                timerLabel.setForeground(red);
            }

            if (remainingSeconds <= 0) {
                countdownTimer.stop();
                revealAnswer();
            }
        });
    }

    /** 加载/刷新一道新题 */
    private void loadShape() {
        // 重置状态
        attempts = 3;
        remainingSeconds = 180;
        countdownTimer.restart();
        timerLabel.setForeground(Color.BLACK); // 重置计时器颜色
        cardPanel.resetForNewShape();

        // 更新进度
        progressLabel.setText("Progress: " + currentShapeIndex + " / " + shapes.length);
        progressBar.setValue(currentShapeIndex);

        // 如果已经完成全部4种，结束
        if (currentShapeIndex >= shapes.length) {
            JOptionPane.showMessageDialog(this, "练习结束，您完成了所有题目！");
            dispose();
            return;
        }

        // 弹出对话框，让用户从剩余图形中选一个
        String[] options = remainingShapes.toArray(new String[0]);
        String shape = (String) JOptionPane.showInputDialog(
                this,
                "请选择一个图形：",
                "选择图形",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (shape == null) { // 用户点"取消"
            dispose();
            return;
        }
        // 从列表中移除，防止重复练习
        remainingShapes.remove(shape);

        // 生成随机参数并计算面积
        Random rand = new Random();
        switch (shape) {
            case "Rectangle":
                dims = new int[] { rand.nextInt(20) + 1, rand.nextInt(20) + 1 };
                correctArea = dims[0] * dims[1];
                break;
            case "Parallelogram":
                dims = new int[] { rand.nextInt(20) + 1, rand.nextInt(20) + 1 };
                correctArea = dims[0] * dims[1];
                break;
            case "Triangle":
                dims = new int[] { rand.nextInt(20) + 1, rand.nextInt(20) + 1 };
                correctArea = dims[0] * dims[1] / 2.0;
                break;
            default:
                int a = rand.nextInt(19) + 1;
                int b = rand.nextInt(20 - a) + a + 1;
                int h = rand.nextInt(20) + 1;
                dims = new int[] { a, b, h };
                correctArea = (a + b) * h / 2.0;
        }
        // 把所选图形名称、参数和计算逻辑传给 cardPanel
        cardPanel.updateShape(shape, dims, correctArea, this::onSubmit);
    }

    /** 用户提交答案的回调 */
    private void onSubmit() {
        try {
            double ans = Double.parseDouble(answerField.getText().trim());
            if (Math.abs(ans - correctArea) < 1e-6) {
                hintLabel.setText("正确！🎉");
                hintLabel.setForeground(green);
                finishRound();
            } else {
                attempts--;
                if (attempts > 0) {
                    hintLabel.setText("不对，还剩 " + attempts + " 次机会");
                    hintLabel.setForeground(red);
                } else {
                    revealAnswer();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请输入数字格式的答案");
        }
    }

    /** 本题答对或时间/机会用尽后调用 */
    private void finishRound() {
        countdownTimer.stop();
        cardPanel.showFormulaAndNext(() -> {
            // "下一图形" 按钮点击
            currentShapeIndex++;
            if (currentShapeIndex < shapes.length) {
                loadShape();
            } else {
                JOptionPane.showMessageDialog(this, "练习结束，您完成了所有题目！");
                dispose();
            }
        });
    }

    /** 公开正确答案 */
    private void revealAnswer() {
        countdownTimer.stop();
        hintLabel.setText("正确答案: " + correctArea);
        hintLabel.setForeground(red);
        cardPanel.showFormulaAndNext(() -> {
            currentShapeIndex++;
            if (currentShapeIndex < shapes.length) {
                loadShape();
            } else {
                JOptionPane.showMessageDialog(this, "练习结束!");
                dispose();
            }
        });
    }

    private class RoundedCardPanel extends JPanel {
        private JLabel titleLabel;
        private ShapeCanvas shapeCanvas;
        private JLabel formulaLabel;
        private JLabel paramsLabel;
        private JButton submitButton, nextButton;
        private JPanel inputRow;
        private Runnable submitCallback;
        private Runnable nextCallback;

        RoundedCardPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(245, 249, 254));
            setBorder(new RoundedBorder(25));

            // 标题
            titleLabel = new JLabel("Rectangle Area Calculation", SwingConstants.CENTER);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            add(titleLabel);

            // 图形渲染区
            shapeCanvas = new ShapeCanvas();
            add(shapeCanvas);

            // 参数文本
            paramsLabel = new JLabel("", SwingConstants.CENTER);
            paramsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            paramsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            add(paramsLabel);

            // 公式文本
            formulaLabel = new JLabel("", SwingConstants.CENTER);
            formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            formulaLabel.setFont(new Font("Arial", Font.BOLD, 18));
            formulaLabel.setVisible(false); // 初始隐藏
            add(formulaLabel);

            // 输入框和提交按钮
            inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel areaLabel = new JLabel("Area = ");
            answerField = new JTextField(10);
            submitButton = new JButton("Submit");
            submitButton.setBackground(blue);
            submitButton.setForeground(Color.WHITE);

            inputRow.add(areaLabel);
            inputRow.add(answerField);
            inputRow.add(submitButton);
            add(inputRow);

            // 提示文本
            hintLabel = new JLabel(" ", SwingConstants.CENTER);
            hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            hintLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            add(hintLabel);

            // 下一题按钮
            nextButton = new JButton("Next Shape");
            nextButton.setBackground(green);
            nextButton.setForeground(Color.WHITE);
            nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            nextButton.setVisible(false); // 初始隐藏
            add(nextButton);

            // 绑定按钮事件
            submitButton.addActionListener(e -> {
                if (submitCallback != null)
                    submitCallback.run();
            });
            nextButton.addActionListener(e -> {
                if (nextCallback != null)
                    nextCallback.run();
            });
        }

        void refreshColors() {
            if (submitButton != null) {
                submitButton.setBackground(blue);
                submitButton.setForeground(Color.WHITE);
            }

            if (nextButton != null) {
                nextButton.setBackground(green);
                nextButton.setForeground(Color.WHITE);
            }

            repaint(); // 重绘ShapeCanvas
        }

        /** 更新要展示的图形 */
        void updateShape(String shapeName, int[] p, double area,
                Runnable submitCallback) {
            titleLabel.setText(shapeName + " Area Calculation");
            shapeCanvas.setShape(shapeName, p, area);
            formulaLabel.setText(shapeCanvas.getFormulaText());
            paramsLabel.setText(shapeCanvas.getParamsText());
            this.submitCallback = submitCallback;
            shapeCanvas.showFormula = false;
            formulaLabel.setVisible(false);
            submitButton.setEnabled(true);
            answerField.setEnabled(true);
            answerField.requestFocus();
            nextButton.setVisible(false);
            repaint();
        }

        /** 显示公式和下一题按钮 */
        void showFormulaAndNext(Runnable nextCallback) {
            this.nextCallback = nextCallback;
            shapeCanvas.showFormula = true; // 打开公式渲染
            formulaLabel.setVisible(true); // 显示公式文本
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            nextButton.setVisible(true);
            repaint();
        }

        /** 为新题目重置组件状态 */
        void resetForNewShape() {
            answerField.setText("");
            hintLabel.setText(" ");
            hintLabel.setForeground(Color.BLACK);
            shapeCanvas.showFormula = false;
            formulaLabel.setVisible(false);
        }
    }

    private class ShapeCanvas extends JPanel {
        String shape;
        int[] p;
        double area;
        boolean showFormula = false;
        static final int SCALE = 10;

        void setShape(String shape, int[] p, double area) {
            this.shape = shape;
            this.p = p;
            this.area = area;
        }

        String getFormulaText() {
            switch (shape) {
                case "Rectangle":
                    return "Area = Length × Width = " + p[0] + " × " + p[1] + " = " + area;
                case "Parallelogram":
                    return "Area = Base × Height = " + p[0] + " × " + p[1] + " = " + area;
                case "Triangle":
                    return "Area = ½ × Base × Height = ½ × " + p[0] + " × " + p[1] + " = " + area;
                case "Trapezoid":
                    return "Area = ½ × (a + b) × h = ½ × (" + p[0] + " + " + p[1] + ") × " + p[2] + " = " + area;
                default:
                    return "";
            }
        }

        String getParamsText() {
            switch (shape) {
                case "Rectangle":
                    return "Length = " + p[0] + ", Width = " + p[1];
                case "Parallelogram":
                    return "Base = " + p[0] + ", Height = " + p[1];
                case "Triangle":
                    return "Base = " + p[0] + ", Height = " + p[1];
                case "Trapezoid":
                    return "a = " + p[0] + ", b = " + p[1] + ", Height = " + p[2];
                default:
                    return "";
            }
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            g.setColor(ColorManager.adaptColor(new Color(100, 149, 237))); // 浅蓝色

            switch (shape) {
                case "Rectangle":
                    int w = p[0] * SCALE;
                    int h = p[1] * SCALE;
                    g.fillRect(cx - w / 2, cy - h / 2, w, h);
                    // 如果要显示公式
                    if (showFormula) {
                        g.setColor(Color.BLACK);
                        drawDimension(g, cx - w / 2, cy + h / 2 + 10, cx + w / 2, cy + h / 2 + 10, p[0] + "",
                                Color.BLACK);
                        drawDimension(g, cx + w / 2 + 10, cy - h / 2, cx + w / 2 + 10, cy + h / 2, p[1] + "",
                                Color.BLACK);
                    }
                    break;
                case "Parallelogram":
                    int base = p[0] * SCALE;
                    int height = p[1] * SCALE;
                    int offset = height / 2; // 平行四边形的错位
                    int[] xPoints = { cx - base / 2 + offset, cx + base / 2 + offset, cx + base / 2 - offset,
                            cx - base / 2 - offset };
                    int[] yPoints = { cy - height / 2, cy - height / 2, cy + height / 2, cy + height / 2 };
                    g.fillPolygon(xPoints, yPoints, 4);
                    // 如果要显示公式
                    if (showFormula) {
                        g.setColor(Color.BLACK);
                        drawDimension(g, cx - base / 2 + offset, cy + height / 2 + 10, cx + base / 2 - offset,
                                cy + height / 2 + 10, p[0] + "", Color.BLACK);
                        drawDimension(g, cx + base / 2 + offset + 10, cy - height / 2, cx + base / 2 - offset + 10,
                                cy + height / 2, p[1] + "", Color.BLACK);
                    }
                    break;
                case "Triangle":
                    int tbase = p[0] * SCALE;
                    int theight = p[1] * SCALE;
                    int[] txPoints = { cx - tbase / 2, cx + tbase / 2, cx };
                    int[] tyPoints = { cy + theight / 2, cy + theight / 2, cy - theight / 2 };
                    g.fillPolygon(txPoints, tyPoints, 3);
                    // 如果要显示公式
                    if (showFormula) {
                        g.setColor(Color.BLACK);
                        drawDimension(g, cx - tbase / 2, cy + theight / 2 + 10, cx + tbase / 2, cy + theight / 2 + 10,
                                p[0] + "", Color.BLACK);
                        drawDimension(g, cx + tbase / 2 + 10, cy + theight / 2, cx, cy - theight / 2, p[1] + "",
                                Color.BLACK);
                    }
                    break;
                case "Trapezoid":
                    int a = p[0] * SCALE; // 上底
                    int b = p[1] * SCALE; // 下底
                    int trapHeight = p[2] * SCALE; // 高
                    int[] zxPoints = { cx - b / 2, cx + b / 2, cx + a / 2, cx - a / 2 };
                    int[] zyPoints = { cy + trapHeight / 2, cy + trapHeight / 2, cy - trapHeight / 2,
                            cy - trapHeight / 2 };
                    g.fillPolygon(zxPoints, zyPoints, 4);
                    // 如果要显示公式
                    if (showFormula) {
                        g.setColor(Color.RED);
                        drawDimension(g, cx - a / 2, cy - trapHeight / 2 - 10, cx + a / 2, cy - trapHeight / 2 - 10,
                                p[0] + " (a)", ColorManager.adaptColor(Color.RED));
                        g.setColor(Color.BLACK);
                        drawDimension(g, cx - b / 2, cy + trapHeight / 2 + 10, cx + b / 2, cy + trapHeight / 2 + 10,
                                p[1] + " (b)", Color.BLACK);
                        drawDimension(g, cx + b / 2 + 10, cy + trapHeight / 2, cx + a / 2 + 10, cy - trapHeight / 2,
                                p[2] + " (h)", ColorManager.adaptColor(new Color(0, 128, 0)));
                    }
                    break;
            }
        }
    }

    private void drawDimension(Graphics2D g, int x1, int y1, int x2, int y2, String label, Color color) {
        g.setColor(color);
        g.draw(new Line2D.Double(x1, y1, x2, y2));

        // 绘制两端的箭头
        drawArrowHead(g, x1, y1, x2, y2);
        drawArrowHead(g, x2, y2, x1, y1);

        // 绘制标签
        FontMetrics fm = g.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelX = (x1 + x2) / 2 - labelWidth / 2;
        int labelY;

        if (Math.abs(y1 - y2) < 5) { // 水平线
            labelY = y1 - 5;
        } else { // 垂直线
            labelY = (y1 + y2) / 2 + fm.getAscent() / 2;
        }

        g.drawString(label, labelX, labelY);
    }

    private void drawArrowHead(Graphics2D g, int x, int y, int tx, int ty) {
        int ARR_SIZE = 5;
        double dx = tx - x;
        double dy = ty - y;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        // Make arrow heads appear at the ends rather than the center
        double adjX = x + (ARR_SIZE * Math.cos(angle + Math.PI / 2) / len);
        double adjY = y + (ARR_SIZE * Math.sin(angle + Math.PI / 2) / len);

        g.fillPolygon(
                new int[] { x, (int) (x - ARR_SIZE * Math.cos(angle - Math.PI / 6)),
                        (int) (x - ARR_SIZE * Math.cos(angle + Math.PI / 6)) },
                new int[] { y, (int) (y - ARR_SIZE * Math.sin(angle - Math.PI / 6)),
                        (int) (y - ARR_SIZE * Math.sin(angle + Math.PI / 6)) },
                3);
    }

    private static class RoundedBorder implements Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
                int width, int height) {
            g.setColor(ColorManager.adaptColor(new Color(200, 200, 200)));
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task3Screen().setVisible(true));
    }
}
