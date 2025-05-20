package com.Shapeville;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.Random;

import static com.Shapeville.ShapevilleGUI.getJPanel;
import static com.Shapeville.ShapevilleMainContent.flag3;
import static com.Shapeville.ShapevilleGUI.currentProgressScore;
import static com.Shapeville.StageSwitcherPanel.task1;
import static com.Shapeville.StageSwitcherPanel.task3;
import static com.Shapeville.Task1Screen.calculateScore;
import static com.Shapeville.Task1Screen.showCustomDialog;

/**
 * Interactive window for <strong>Task&nbsp;3 – Shape Area Calculation</strong>.
 * Lets the learner pick each of four shapes (rectangle, parallelogram,
 * triangle, trapezoid). Random dimensions are generated and the learner has
 * three attempts or 3&nbsp;minutes to supply the exact area. Colour‑coded hints,
 * a countdown and a progress bar support the activity. After each round the
 * correct formula is revealed; after all four rounds the window closes and the
 * main dashboard updates. Colours live‑swap via {@link ColorManager} through
 * {@link #refreshColors()}.
 *
 * @author Lingyuan Li
 */
public class Task3Screen extends JFrame implements ColorRefreshable {
    /** Array of available shapes for the exercise */
    private final String[] shapes = { "Rectangle", "Parallelogram", "Triangle", "Trapezoid" };
    
    /** Current index of the shape being tested */
    private int currentShapeIndex = 0;
    
    /** List of shapes that haven't been tested yet */
    private java.util.List<String> remainingShapes;

    /** Number of attempts remaining for the current shape */
    private int attempts = 3;
    
    /** The correct area for the current shape */
    private double correctArea;
    
    /** Random dimensions for the current shape */
    private int[] dims;

    // UI components --------------------------------------------------------
    /** Label showing progress through the exercise */
    private JLabel progressLabel;
    
    /** Progress bar showing completion status */
    private JProgressBar progressBar;
    
    /** Label showing remaining time */
    private JLabel timerLabel;
    
    /** Timer for the countdown */
    private Timer countdownTimer;
    
    /** Remaining seconds in the current round */
    private int remainingSeconds = 180;

    /** Panel containing the current shape exercise */
    private RoundedCardPanel cardPanel;
    
    /** Text field for entering the answer */
    private JTextField answerField;
    
    /** Label showing hints and feedback */
    private JLabel hintLabel;
    
    /** Wrapper panel for the gradient navigation bar */
    private JPanel gradientTopWrapper;

    private int score  = 0;

    // Palette colours ------------------------------------------------------
    /** Blue color for UI elements */
    private Color blue = ColorManager.getBlue();
    
    /** Green color for success states */
    private Color green = ColorManager.getGreen();
    
    /** Red color for error states */
    private Color red = ColorManager.getRed();
    
    /** Color for the progress bar */
    private Color progressBarColor = ColorManager.getProgressBarColor();

    /**
     * Constructs a new Task3Screen window and initializes the UI components.
     * Sets up the navigation bar, timer, progress tracking, and the main exercise card.
     */
    public Task3Screen() {
        remainingShapes = new java.util.ArrayList<>(java.util.Arrays.asList(shapes));
        setTitle("Task 3: Shape Area Calculation");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        /* ---------- North: gradient navigation bar ---------------------- */
        gradientTopWrapper = getJPanel();
        TopNavBarPanel topNav = new TopNavBarPanel();
        gradientTopWrapper.add(topNav);
        add(gradientTopWrapper, BorderLayout.NORTH);
        topNav.homeButton.addActionListener(e -> dispose());
        topNav.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You scored " + currentShapeIndex + " / " + shapes.length);
            dispose();
        });

        /* ---------- East: timer + progress ------------------------------ */
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

        /* ---------- Centre: exercise card ------------------------------- */
        cardPanel = new RoundedCardPanel();
        add(cardPanel, BorderLayout.CENTER);

        bindActions();
        loadShape();

        setLocationRelativeTo(null);
    }
    // ---------------------------------------------------------------------
    //                     Colour‑blind palette refresh
    // ---------------------------------------------------------------------
    /**
     * Refreshes all color-related UI elements when the color scheme changes.
     * Updates colors for progress bar, card panel, timer, hints, and navigation bar.
     */
    @Override
    public void refreshColors() {
        System.out.println("Task3Screen正在刷新颜色...");

        blue = ColorManager.getBlue();
        green = ColorManager.getGreen();
        red = ColorManager.getRed();
        progressBarColor = ColorManager.getProgressBarColor();

        if (progressBar != null) {
            progressBar.setForeground(progressBarColor);
        }

        if (cardPanel != null) {
            cardPanel.refreshColors();
        }

        if (timerLabel != null && remainingSeconds <= 60) {
            timerLabel.setForeground(red);
        }

        if (hintLabel != null) {
            String hintText = hintLabel.getText();
            if (hintText.contains("正确")) {
                hintLabel.setForeground(green);
            } else if (hintText.contains("不对")) {
                hintLabel.setForeground(red);
            }
        }

        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        repaint();
    }
    // ---------------------------------------------------------------------
    //                           Timer & flow
    // ---------------------------------------------------------------------

    /**
     * Initializes and binds the countdown timer action.
     * Updates the timer display and handles time expiration.
     */
    private void bindActions() {
        countdownTimer = new Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText(String.format("Time: %02d:%02d", remainingSeconds / 60, remainingSeconds % 60));

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
     * Loads a new shape for the exercise.
     * Resets the state, generates random dimensions, and updates the UI.
     */
    private void loadShape() {
        // 重置状态
        attempts = 3;
        remainingSeconds = 180;
        countdownTimer.restart();
        timerLabel.setForeground(Color.BLACK); // 重置计时器颜色
        cardPanel.resetForNewShape();

        progressLabel.setText("Progress: " + currentShapeIndex + " / " + shapes.length);
        progressBar.setValue(currentShapeIndex);

        if (currentShapeIndex >= shapes.length) {
            JOptionPane.showMessageDialog(this, "The practice is over. You have completed all the questions!");
            dispose();
            if (flag3 == 0) {
                ShapevilleMainContent.updateProgress();
                task3.setStartButtonEnabled(false); // 禁用
                flag3 = 1;
            }
            return;
        }


        String[] options = remainingShapes.toArray(new String[0]);
        String shape = (String) JOptionPane.showInputDialog(
                this,
                "Please select a graphic:",
                "Select the graphic",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (shape == null) {
            dispose();
            //return;
        }
        remainingShapes.remove(shape);

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

    /**
     * Handles the submission of an answer.
     * Validates the input and provides feedback based on correctness.
     */
    private void onSubmit() {
        try {
            double ans = Double.parseDouble(answerField.getText().trim());
            if (Math.abs(ans - correctArea) < 1e-6) {
                score += calculateScore(true, attempts);
                currentProgressScore += score;
                showCustomDialog(score);
                System.out.println(score);
                hintLabel.setText("Correct! ");
                hintLabel.setForeground(green);
                finishRound();
            } else {
                attempts--;
                if (attempts > 0) {
                    hintLabel.setText("There is still " + attempts + " chance left");
                    hintLabel.setForeground(red);
                } else {
                    revealAnswer();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter the answer in numerical format!");
        }
    }

    /**
     * Completes the current round after a correct answer.
     * Shows the formula and prepares for the next shape.
     */
    private void finishRound() {
        countdownTimer.stop();
        cardPanel.showFormulaAndNext(() -> {
            // "下一图形" 按钮点击
            currentShapeIndex++;
            if (currentShapeIndex < shapes.length) {
                loadShape();
            } else {
                JOptionPane.showMessageDialog(this, "The practice is over. You have completed all the questions!");
                dispose();
            }
        });
    }

    /**
     * Reveals the correct answer when time runs out or attempts are exhausted.
     * Shows the formula and prepares for the next shape.
     */
    private void revealAnswer() {
        countdownTimer.stop();
        hintLabel.setText("Right answers:" + correctArea);
        hintLabel.setForeground(red);
        cardPanel.showFormulaAndNext(() -> {
            currentShapeIndex++;
            if (currentShapeIndex < shapes.length) {
                loadShape();
            } else {
                JOptionPane.showMessageDialog(this, "The practice is over.");
                dispose();
            }
        });
    }

    /**
     * Panel class for displaying the shape exercise card with rounded corners.
     * Contains the shape visualization, input field, and feedback elements.
     */
    private class RoundedCardPanel extends JPanel {
        /** Label showing the current shape type */
        private JLabel titleLabel;
        
        /** Canvas for drawing the current shape */
        private ShapeCanvas shapeCanvas;
        
        /** Label showing the area formula */
        private JLabel formulaLabel;
        
        /** Label showing the shape dimensions */
        private JLabel paramsLabel;
        
        /** Button for submitting answers */
        private JButton submitButton;
        
        /** Button for proceeding to next shape */
        private JButton nextButton;
        
        /** Panel containing the input field and submit button */
        private JPanel inputRow;
        
        /** Callback for submit button action */
        private Runnable submitCallback;
        
        /** Callback for next button action */
        private Runnable nextCallback;

        /**
         * Constructs a new RoundedCardPanel with all necessary UI components.
         */
        RoundedCardPanel() {
            //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setLayout(new BorderLayout());
            setBackground(new Color(245, 249, 254));
            setBorder(new RoundedBorder(25));

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

            titleLabel = new JLabel("Rectangle Area Calculation", SwingConstants.CENTER);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            content.add(titleLabel);

            shapeCanvas = new ShapeCanvas();
            shapeCanvas.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            shapeCanvas.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中
            content.add(shapeCanvas);

            paramsLabel = new JLabel("", SwingConstants.CENTER);
            paramsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            paramsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            content.add(paramsLabel);

            formulaLabel = new JLabel("", SwingConstants.CENTER);
            formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            formulaLabel.setFont(new Font("Arial", Font.BOLD, 18));
            formulaLabel.setVisible(false); // 初始隐藏
            content.add(formulaLabel);

            inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel areaLabel = new JLabel("Area = ");
            answerField = new JTextField(10);
            submitButton = new JButton("Submit");
            submitButton.setBackground(blue);
            submitButton.setForeground(Color.WHITE);

            inputRow.add(areaLabel);
            inputRow.add(answerField);
            inputRow.add(submitButton);
            content.add(inputRow);

            hintLabel = new JLabel(" ", SwingConstants.CENTER);
            hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            hintLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            content.add(hintLabel);

            nextButton = new JButton("Next Shape");
            nextButton.setBackground(green);
            nextButton.setForeground(Color.WHITE);
            nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            nextButton.setVisible(false); // 初始隐藏
            content.add(nextButton);

            add(content, BorderLayout.CENTER);
            add(inputRow, BorderLayout.SOUTH);

            submitButton.addActionListener(e -> {
                if (submitCallback != null)
                    submitCallback.run();
            });
            nextButton.addActionListener(e -> {
                if (nextCallback != null)
                    nextCallback.run();
            });
        }

        /**
         * Updates the colors of UI elements when the color scheme changes.
         */
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

        /**
         * Updates the panel with a new shape exercise.
         * @param shapeName The name of the shape
         * @param p The dimensions of the shape
         * @param area The correct area
         * @param submitCallback Callback for submit button
         */
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

        /**
         * Shows the formula and next button after an answer is submitted.
         * @param nextCallback Callback for next button
         */
        void showFormulaAndNext(Runnable nextCallback) {
            this.nextCallback = nextCallback;
            shapeCanvas.showFormula = true; // 打开公式渲染
            formulaLabel.setVisible(true); // 显示公式文本
            submitButton.setEnabled(false);
            answerField.setEnabled(false);
            nextButton.setVisible(true);
            repaint();
        }

        /**
         * Resets the panel state for a new shape.
         */
        void resetForNewShape() {
            answerField.setText("");
            hintLabel.setText(" ");
            hintLabel.setForeground(Color.BLACK);
            shapeCanvas.showFormula = false;
            formulaLabel.setVisible(false);
        }
    }

    /**
     * Canvas class for drawing shapes and their dimensions.
     */
    private class ShapeCanvas extends JPanel {
        /** Current shape type */
        String shape;
        
        /** Shape dimensions */
        int[] p;
        
        /** Correct area of the shape */
        double area;
        
        /** Whether to show the formula */
        boolean showFormula = false;
        
        /** Scale factor for drawing shapes */
        static final int SCALE = 10;

        /**
         * Sets the current shape and its properties.
         * @param shape The shape type
         * @param p The dimensions
         * @param area The correct area
         */
        void setShape(String shape, int[] p, double area) {
            this.shape = shape;
            this.p = p;
            this.area = area;
        }

        /**
         * Gets the formula text for the current shape.
         * @return The formula as a string
         */
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

        /**
         * Gets the parameter text for the current shape.
         * @return The parameters as a string
         */
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

        /**
         * Paints the shape and its dimensions on the canvas.
         * @param g0 The graphics context
         */
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
                    int offset = height / 2;
                    int[] xPoints = { cx - base / 2 + offset, cx + base / 2 + offset, cx + base / 2 - offset,
                            cx - base / 2 - offset };
                    int[] yPoints = { cy - height / 2, cy - height / 2, cy + height / 2, cy + height / 2 };
                    g.fillPolygon(xPoints, yPoints, 4);
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
                    int a = p[0] * SCALE;
                    int b = p[1] * SCALE;
                    int trapHeight = p[2] * SCALE;
                    int[] zxPoints = { cx - b / 2, cx + b / 2, cx + a / 2, cx - a / 2 };
                    int[] zyPoints = { cy + trapHeight / 2, cy + trapHeight / 2, cy - trapHeight / 2,
                            cy - trapHeight / 2 };
                    g.fillPolygon(zxPoints, zyPoints, 4);
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

    /**
     * Draws dimension lines with arrows and labels.
     * @param g The graphics context
     * @param x1 Start x coordinate
     * @param y1 Start y coordinate
     * @param x2 End x coordinate
     * @param y2 End y coordinate
     * @param label The dimension label
     * @param color The line color
     */
    private void drawDimension(Graphics2D g, int x1, int y1, int x2, int y2, String label, Color color) {
        g.setColor(color);
        g.draw(new Line2D.Double(x1, y1, x2, y2));

        drawArrowHead(g, x1, y1, x2, y2);
        drawArrowHead(g, x2, y2, x1, y1);

        FontMetrics fm = g.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelX = (x1 + x2) / 2 - labelWidth / 2;
        int labelY;

        if (Math.abs(y1 - y2) < 5) {
            labelY = y1 - 5;
        } else {
            labelY = (y1 + y2) / 2 + fm.getAscent() / 2;
        }

        g.drawString(label, labelX, labelY);
    }

    /**
     * Draws an arrow head at the specified position.
     * @param g The graphics context
     * @param x Arrow base x coordinate
     * @param y Arrow base y coordinate
     * @param tx Target x coordinate
     * @param ty Target y coordinate
     */
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

    /**
     * Custom border class for rounded corners.
     */
    private static class RoundedBorder implements Border {
        /** Radius of the rounded corners */
        private final int radius;

        /**
         * Constructs a new RoundedBorder.
         * @param radius The corner radius
         */
        RoundedBorder(int radius) {
            this.radius = radius;
        }

        /**
         * Gets the border insets.
         * @param c The component
         * @return The border insets
         */
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        /**
         * Checks if the border is opaque.
         * @return false
         */
        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        /**
         * Paints the rounded border.
         * @param c The component
         * @param g The graphics context
         * @param x The x coordinate
         * @param y The y coordinate
         * @param width The width
         * @param height The height
         */
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(ColorManager.adaptColor(new Color(200, 200, 200)));
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
        }
    }

    /**
     * Main method for testing the Task3Screen.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task3Screen().setVisible(true));
    }
}
