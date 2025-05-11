package com.Shapeville;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.Random;

import static com.Shapeville.ShapevilleGUI.getJPanel;

public class Task3Screen extends JFrame {
    private final String[] shapes = {"Rectangle", "Parallelogram", "Triangle", "Trapezoid"};
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

    private RoundedCardPanel cardPanel;  // 题目卡片
    private JTextField answerField;
    private JLabel hintLabel;

    public Task3Screen() {
        remainingShapes = new java.util.ArrayList<>(java.util.Arrays.asList(shapes));
        setTitle("Task 3: Shape Area Calculation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ─── 北：导航栏 ─────────────────────────
        JPanel topWrapper = getJPanel();
        TopNavBarPanel topNav = new TopNavBarPanel();
        topWrapper.add(topNav);
        add(topWrapper, BorderLayout.NORTH);
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
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        east.add(progressBar);
        add(east, BorderLayout.EAST);

        // ─── 中央：卡片式题目 ────────────────────
        cardPanel = new RoundedCardPanel();
        add(cardPanel, BorderLayout.CENTER);

        bindActions();
        loadShape();

        setLocationRelativeTo(null);
        setVisible(true);

//        remainingShapes = new java.util.ArrayList<>(
//                java.util.Arrays.asList(shapes)
//        );

        bindActions();
        loadShape();  // 现在会先弹出选择对话框

        setLocationRelativeTo(null);
        setVisible(true);

    }

    /** 绑定倒计时和按钮逻辑 */
    private void bindActions() {
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText(String.format("Time: %02d:%02d", remainingSeconds / 60, remainingSeconds % 60));
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
                options[0]
        );
        if (shape == null) { // 用户点“取消”
            dispose();
            return;
        }
        // 从列表中移除，防止重复练习
        remainingShapes.remove(shape);


        // 生成随机参数并计算面积
        Random rand = new Random();
        switch (shape) {
            case "Rectangle":
                dims = new int[]{rand.nextInt(20)+1, rand.nextInt(20)+1};
                correctArea = dims[0] * dims[1];
                break;
            case "Parallelogram":
                dims = new int[]{rand.nextInt(20)+1, rand.nextInt(20)+1};
                correctArea = dims[0] * dims[1];
                break;
            case "Triangle":
                dims = new int[]{rand.nextInt(20)+1, rand.nextInt(20)+1};
                correctArea = dims[0] * dims[1] / 2.0;
                break;
            default:
                int a = rand.nextInt(19)+1;
                int b = rand.nextInt(20-a)+a+1;
                int h = rand.nextInt(20)+1;
                dims = new int[]{a,b,h};
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
                finishRound();
            } else {
                attempts--;
                if (attempts > 0) {
                    hintLabel.setText("不对，还剩 " + attempts + " 次机会");
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
            // “下一图形” 按钮点击
            currentShapeIndex++;
            if (currentShapeIndex < shapes.length) {
                loadShape();
            } else {
                JOptionPane.showMessageDialog(this, "练习结束，您完成了所有题目！");
                dispose();
            }
        });
    }

    /** 时间到或三次错误后公布答案 */
    private void revealAnswer() {
        hintLabel.setText(String.format("答案：%.2f", correctArea));
        finishRound();
    }

    /** 卡片式 UI Panel */
    private class RoundedCardPanel extends JPanel {
        private JLabel titleLabel;
        private ShapeCanvas shapeCanvas;
        private JLabel formulaLabel;
        private JLabel paramsLabel;
        private JButton submitButton, nextButton;
        private JPanel inputRow;

        RoundedCardPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 249, 254));
            setBorder(new RoundedBorder(16));

            // 顶部：标题
            titleLabel = new JLabel("", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(12,0,12,0));
            add(titleLabel, BorderLayout.NORTH);

            // 中部：绘图 Canvas
            shapeCanvas = new ShapeCanvas();
            add(shapeCanvas, BorderLayout.CENTER);

            // 底部：公式 + 参数 + 输入行
            JPanel south = new JPanel();
            south.setOpaque(false);
            south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
            south.setBorder(BorderFactory.createEmptyBorder(8,16,16,16));

            formulaLabel = new JLabel("Formula: ");
            formulaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            south.add(formulaLabel);

            paramsLabel = new JLabel();
            paramsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            south.add(paramsLabel);
            south.add(Box.createVerticalStrut(8));

            // 输入 + 按钮行
            inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8,0));
            inputRow.setOpaque(false);
            inputRow.add(new JLabel("Area ="));
            answerField = new JTextField(6);
            inputRow.add(answerField);
            inputRow.add(new JLabel("cm²"));
            submitButton = new JButton("提交");
            inputRow.add(submitButton);
            hintLabel = new JLabel("你有 3 次尝试机会");
            inputRow.add(hintLabel);
            nextButton = new JButton("下一图形");
            nextButton.setVisible(false);
            inputRow.add(nextButton);

            south.add(inputRow);
            add(south, BorderLayout.SOUTH);
        }

        /** 点击提交按钮，触发外部回调 */
        void updateShape(String shapeName, int[] p, double area,
                         Runnable submitCallback) {
            titleLabel.setText(shapeName);
            shapeCanvas.setShape(shapeName, p, area);

            formulaLabel.setText("Formula: " + shapeCanvas.getFormulaText());
            paramsLabel.setText("Parameters: " + shapeCanvas.getParamsText());

            submitButton.setVisible(true);
            nextButton.setVisible(false);
            hintLabel.setText("你有 3 次尝试机会");
            answerField.setText("");

            // 解绑旧动作，绑定新动作
            for (ActionListener al : submitButton.getActionListeners()) {
                submitButton.removeActionListener(al);
            }
            submitButton.addActionListener(e -> submitCallback.run());

            nextButton.removeActionListener(nextButton.getActionListeners().length>0
                    ? nextButton.getActionListeners()[0] : null);
            nextButton.addActionListener(e -> submitCallback.run());
        }

        /** 本题完成后展示公式及“下一图形”按钮 */
        void showFormulaAndNext(Runnable nextCallback) {
            shapeCanvas.showFormula = true;
            shapeCanvas.repaint();
            submitButton.setVisible(false);
            nextButton.setVisible(true);
            // 重新绑定下一题动作
            for (ActionListener al : nextButton.getActionListeners()) {
                nextButton.removeActionListener(al);
            }
            nextButton.addActionListener(e -> nextCallback.run());
        }

        /** 重置状态，为新题做准备 */
        void resetForNewShape() {
            shapeCanvas.showFormula = false;
            shapeCanvas.repaint();
        }
    }

    /** 真正的绘图和尺寸、公式计算 */
    private class ShapeCanvas extends JPanel {
        String shape; int[] p; double area;
        boolean showFormula = false;
        static final int SCALE = 10;

        void setShape(String shape, int[] p, double area) {
            this.shape = shape; this.p = p; this.area = area;
        }

        String getFormulaText() {
            switch (shape) {
                case "Rectangle":
                case "Parallelogram":
                    return String.format("Area = %d × %d = %.0f", p[0], p[1], area);
                case "Triangle":
                    return String.format("Area = ½ × %d × %d = %.1f", p[0], p[1], area);
                default:
                    return String.format("Area = ( %d + %d ) ÷ 2 × %d = %.1f",
                            p[0], p[1], p[2], area);
            }
        }

        String getParamsText() {
            switch (shape) {
                case "Rectangle":
                    return String.format("Length = %d cm, Width = %d cm", p[0], p[1]);
                case "Parallelogram":
                    return String.format("Base = %d cm, Height = %d cm", p[0], p[1]);
                case "Triangle":
                    return String.format("Base = %d cm, Height = %d cm", p[0], p[1]);
                default:
                    return String.format("Top a = %d cm, Bottom b = %d cm, Height h = %d cm",
                            p[0], p[1], p[2]);
            }
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            if (shape == null) return;
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // 计算居中绘图起始点
            int w = (shape.equals("Trapezoid") ? p[1] : p[0]) * SCALE;
            int h = p[ shape.equals("Triangle") ? 1
                    : shape.equals("Trapezoid") ? 2 : 1 ] * SCALE;
            int x0 = (getWidth() - w) / 2, y0 = (getHeight() - h) / 2;

            // 填充
            g.setColor(new Color(100, 149, 237));
            switch (shape) {
                case "Rectangle":    g.fillRect(x0,y0,w,h); break;
                case "Parallelogram":
                    g.fillPolygon(
                            new int[]{x0,x0+w,x0+w+20,x0+20},
                            new int[]{y0,y0,y0+h,y0+h},4);
                    break;
                case "Triangle":
                    g.fillPolygon(
                            new int[]{x0,x0+w/2,x0+w},
                            new int[]{y0+h,y0,y0+h},3);
                    break;
                default: // Trapezoid
                    int top = p[0]*SCALE, bot = p[1]*SCALE, ht = p[2]*SCALE;
                    int off = (bot - top)/2;
                    g.fillPolygon(
                            new int[]{x0+off,x0+off+top,x0+bot,x0},
                            new int[]{y0,y0,y0+ht,y0+ht},4);
            }

            // 边框 & 标注尺寸
            g.setColor(Color.BLACK);
            switch (shape) {
                case "Rectangle":    g.drawRect(x0,y0,w,h); break;
                case "Parallelogram":
                    g.drawPolygon(
                            new int[]{x0,x0+w,x0+w+20,x0+20},
                            new int[]{y0,y0,y0+h,y0+h},4);
                    break;
                case "Triangle":
                    g.drawPolygon(
                            new int[]{x0,x0+w/2,x0+w},
                            new int[]{y0+h,y0,y0+h},3);
                    break;
                default:
                    int top2 = p[0]*SCALE, bot2 = p[1]*SCALE, ht2 = p[2]*SCALE;
                    int off2 = (bot2 - top2)/2;
                    g.drawPolygon(
                            new int[]{x0+off2,x0+off2+top2,x0+bot2,x0},
                            new int[]{y0,y0,y0+ht2,y0+ht2},4);
            }

            // 上方标注
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            String topLabel = shape.equals("Trapezoid")
                    ? (p[0]+" cm") : (p[0]+" cm");
            int tw = g.getFontMetrics().stringWidth(topLabel);
            g.drawString(topLabel, x0 + (w - tw)/2, y0 - 6);

            // 左侧标注
            String leftLabel = shape.equals("Trapezoid")
                    ? (p[2]+" cm") : (shape.equals("Triangle")? p[1]+" cm": p[1]+" cm");
            int lh = g.getFontMetrics().getHeight();
            g.drawString(leftLabel,
                    x0 - g.getFontMetrics().stringWidth(leftLabel) - 6,
                    y0 + (h + lh)/2 );

            // 显示公式
            if (showFormula) {
                g.setColor(Color.RED);
                String formula = getFormulaText();
                FontMetrics fm = g.getFontMetrics();
                int fx = (getWidth() - fm.stringWidth(formula)) / 2;
                int fy = y0 + h + 30;
                g.drawString(formula, fx, fy);
            }
        }
    }

    /** 通用：绘制带双向箭头的标注 */
    private void drawDimension(Graphics2D g, int x1, int y1, int x2, int y2, String label, Color color) {
        Stroke old = g.getStroke();
        g.setColor(color);
        g.setStroke(new BasicStroke(2));
        // 画中间线
        g.drawLine(x1, y1, x2, y2);
        // 画两端箭头
        drawArrowHead(g, x1, y1, x2, y2);
        drawArrowHead(g, x2, y2, x1, y1);
        // 文字标签（略微偏移，避免贴在线上）
        int mx = (x1 + x2) / 2, my = (y1 + y2) / 2;
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(label, mx + 4, my - 4);
        g.setStroke(old);
    }

    /** 辅助：在 (x,y) 处画一端箭头，指向 (tx,ty) */
    private void drawArrowHead(Graphics2D g, int x, int y, int tx, int ty) {
        double phi = Math.toRadians(20);
        int barb = 10;
        double theta = Math.atan2(y - ty, x - tx);
        double x1 = x - barb * Math.cos(theta + phi);
        double y1 = y - barb * Math.sin(theta + phi);
        double x2 = x - barb * Math.cos(theta - phi);
        double y2 = y - barb * Math.sin(theta - phi);
        g.draw(new Line2D.Double(x, y, x1, y1));
        g.draw(new Line2D.Double(x, y, x2, y2));
    }


    /** 简单的圆角边框实现 */
    private static class RoundedBorder implements Border {
        private final int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(radius,radius,radius,radius);
        }
        @Override public boolean isBorderOpaque() { return false; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y,
                                          int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200,200,200));
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Task3Screen::new);
    }
}
