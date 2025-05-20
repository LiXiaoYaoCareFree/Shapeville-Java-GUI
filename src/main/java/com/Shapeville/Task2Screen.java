package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import static com.Shapeville.ShapevilleMainContent.flag1;
import static com.Shapeville.ShapevilleMainContent.flag2;

/**
 * Interactive window for <strong>Task&nbsp;2 – Angle Type Identification</strong>.
 * Presents a drawn angle (multiple of 10°) and asks the pupil to classify it
 * as acute, right, obtuse, straight or reflex. The user has three attempts; a
 * colourful feedback label and attempt counter guide the process. After four
 * different angle types have been recognised the task ends. Colours adapt
 * through {@link ColorManager}, and the main dashboard progress is updated the
 * first time the window opens.
 *
 * @author Lingyuan Li
 */
public class Task2Screen extends JFrame implements ColorRefreshable {
    /** Set to store the types of angles that have been correctly identified */
    private Set<String> recognizedTypes = new HashSet<>();
    
    /** Number of attempts remaining for the current angle */
    private int attempts;
    
    /** The current angle being displayed (in degrees) */
    private int currentAngle;
    
    /** The correct type of the current angle */
    private String correctType;

    /** Panel for displaying the angle visualization */
    private JPanel shapePanel;
    
    /** Label for displaying hints and feedback */
    private JLabel hintLabel;
    
    /** Label for displaying remaining attempts */
    private JLabel attemptsLabel;
    
    /** Dropdown menu for selecting angle type */
    private JComboBox<String> typeCombo;
    
    /** Button for submitting the selected angle type */
    private JButton submitButton;
    
    /** Button for proceeding to the next angle */
    private JButton nextButton;
    
    /** Wrapper panel for the top gradient navigation */
    private JPanel gradientTopWrapper;
    
    /** Main content panel containing all UI elements */
    private JPanel mainPanel;

    /** Color constants managed by ColorManager */
    private Color red = ColorManager.getRed();
    private Color green = ColorManager.getGreen();
    private Color blue = ColorManager.getBlue();

    /**
     * Panel class for drawing and displaying angles
     */
    class AnglePanel extends JPanel {
        /** The angle to be displayed (in degrees) */
        private int angle;
        
        /** Radius of the angle visualization */
        private static final int RADIUS = 100;
        
        /** Color of the angle lines */
        private Color lineColor = Color.BLACK;
        
        /** Color of the angle arc */
        private Color arcColor = ColorManager.getBlue();

        /**
         * Constructs a new AnglePanel with the specified angle
         * @param angle The angle to display (in degrees)
         */
        public AnglePanel(int angle) {
            this.angle = angle;
            setPreferredSize(new Dimension(300, 300));
        }

        /**
         * Updates the colors of the angle visualization based on color blindness mode
         */
        public void updateColors() {
            lineColor = ColorManager.isColorBlindMode() ? Color.BLACK : Color.BLACK;
            arcColor = ColorManager.getBlue();
            repaint();
        }

        /**
         * Paints the angle visualization including lines, arc, and angle label
         * @param g The Graphics context to paint with
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            // Draw base line
            g2.setColor(lineColor);
            g2.drawLine(cx, cy, cx + RADIUS, cy);

            // Draw rotated line
            double rad = Math.toRadians(angle);
            int x2 = cx + (int) (RADIUS * Math.cos(rad));
            int y2 = cy - (int) (RADIUS * Math.sin(rad));
            g2.drawLine(cx, cy, x2, y2);

            // Draw arc indicating angle
            g2.setColor(arcColor);
            int arcAngle = angle % 360; // 确保角度在0到360之间
            int startAngle = 0;
            if (arcAngle < 0) {
                arcAngle = 360 + arcAngle;
                startAngle = 180; // 如果角度为负，调整起始角度
            }
            g2.drawArc(cx - RADIUS, cy - RADIUS, RADIUS * 2, RADIUS * 2, startAngle, arcAngle);

            // Draw angle label
            g2.setColor(lineColor);
            g2.drawString(angle + "°", cx + 5, cy - 5);
        }
    }

    /**
     * Constructs a new Task2Screen and initializes the UI components
     */
    public Task2Screen() {
        setTitle("Task 2: Angle Type Identification");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top navigation
        gradientTopWrapper = ShapevilleGUI.getJPanel();
        TopNavBarPanel top = new TopNavBarPanel();
        gradientTopWrapper.add(top);
        add(gradientTopWrapper, BorderLayout.NORTH);
        top.homeButton.addActionListener(e -> dispose());
        top.endSessionButton.addActionListener(e -> dispose());

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel, BorderLayout.CENTER);

        // Shape drawing panel
        shapePanel = new AnglePanel(0);
        mainPanel.add(shapePanel);

        // Hint and attempts
        hintLabel = new JLabel("Select the type for the displayed angle.");
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(hintLabel);

        attemptsLabel = new JLabel();
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(attemptsLabel);

        // Type selection
        typeCombo = new JComboBox<>(new String[] { "Acute", "Obtuse", "Right", "Straight", "Reflex" });
        typeCombo.setMaximumSize(typeCombo.getPreferredSize());
        typeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(typeCombo);

        // Buttons
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit");
        submitButton.setBackground(blue);
        submitButton.setForeground(Color.WHITE);

        nextButton = new JButton("Next");
        nextButton.setBackground(green);
        nextButton.setForeground(Color.WHITE);
        nextButton.setVisible(false);

        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);
        mainPanel.add(buttonPanel);

        // Handlers
        submitButton.addActionListener(e -> onSubmit());
        nextButton.addActionListener(e -> selectNext());

        // Start
        selectNext();
        setLocationRelativeTo(null);
    }

    /**
     * Refreshes the colors of all UI elements to respond to changes in color blindness mode
     */
    @Override
    public void refreshColors() {
        System.out.println("Task2Screen正在刷新颜色...");

        red = ColorManager.getRed();
        green = ColorManager.getGreen();
        blue = ColorManager.getBlue();

        if (submitButton != null) {
            submitButton.setBackground(blue);
            submitButton.setForeground(Color.WHITE);
        }

        if (nextButton != null) {
            nextButton.setBackground(green);
            nextButton.setForeground(Color.WHITE);
        }

        if (hintLabel != null) {
            String hintText = hintLabel.getText();
            if (hintText.startsWith("Correct")) {
                hintLabel.setForeground(green);
            } else if (hintText.startsWith("Incorrect") || hintText.startsWith("No more")) {
                hintLabel.setForeground(red);
            } else {
                hintLabel.setForeground(Color.BLACK);
            }
        }

        if (shapePanel instanceof AnglePanel) {
            ((AnglePanel) shapePanel).updateColors();
        }

        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        if (mainPanel != null)
            mainPanel.repaint();
    }

    /**
     * Selects and displays the next angle for identification
     * Prompts user for angle input and updates the UI accordingly
     */
    private void selectNext() {
        if (recognizedTypes.size() == 4) {
            JOptionPane.showMessageDialog(this, "You have identified 4 angle types!");
            dispose();
            if (flag2 == 0) {
                ShapevilleMainContent.updateProgress();
                flag2 = 1;
            }
            return;
        }
        // Reset attempts
        attempts = 3;

        // Prompt for angle input
        Integer angle = null;
        while (angle == null) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter an angle (0-360, multiple of 10):",
                    "Input Angle",
                    JOptionPane.PLAIN_MESSAGE);
            if (input == null) {
                // User cancelled
                dispose();
                return;
            }
            try {
                int val = Integer.parseInt(input.trim());
                if (val > 0 && val < 360 && val % 10 == 0) {
                    angle = val;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a multiple of 10 between 0 and 360(Not\n" +
                            "including 0 and 360), using only multiples of 10 degrees for simplicity.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number. Try again.");
            }
        }
        currentAngle = angle;
        correctType = determineType(currentAngle);

        // Update UI
        shapePanel.getParent().remove(shapePanel);
        shapePanel = new AnglePanel(currentAngle);
        ((JPanel) getContentPane().getComponent(1)).add(shapePanel, 0);
        revalidate();
        repaint();

        hintLabel.setText("Identify the type of this angle.");
        hintLabel.setForeground(Color.BLACK);
        updateAttemptsLabel();
        submitButton.setEnabled(true);
        submitButton.setBackground(blue);
        nextButton.setVisible(false);
    }

    /**
     * Determines the type of angle based on its degree measurement
     * @param angle The angle in degrees
     * @return The type of angle ("Acute", "Right", "Obtuse", "Straight", or "Reflex")
     */
    private String determineType(int angle) {
        if (angle == 90)
            return "Right";
        if (angle == 180)
            return "Straight";
        if (angle > 0 && angle < 90)
            return "Acute";
        if (angle > 90 && angle < 180)
            return "Obtuse";
        if (angle > 180 && angle < 360)
            return "Reflex";
        // angle 0 or 360 treat as straight
        return "Straight";
    }

    /**
     * Updates the attempts label to show remaining attempts
     */
    private void updateAttemptsLabel() {
        attemptsLabel.setText("Attempts left: " + attempts);
    }

    /**
     * Handles the submission of the selected angle type
     * Checks if the answer is correct and updates the UI accordingly
     */
    private void onSubmit() {
        String selected = (String) typeCombo.getSelectedItem();
        if (selected.equals(correctType)) {
            recognizedTypes.add(selected);
            hintLabel.setText("Correct! You have recognized: " + recognizedTypes);
            hintLabel.setForeground(green);
            submitButton.setEnabled(false);
            nextButton.setVisible(true);
        } else {
            attempts--;
            updateAttemptsLabel();
            if (attempts > 0) {
                hintLabel.setText("Incorrect. Try again.");
                hintLabel.setForeground(red);
            } else {
                hintLabel.setText("No more attempts. The correct type is " + correctType);
                hintLabel.setForeground(red);
                submitButton.setEnabled(false);
                nextButton.setVisible(true);
            }
        }
    }

    /**
     * Main method to launch the Task2Screen
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task2Screen().setVisible(true));
    }
}
