package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.Shapeville.ShapevilleGUI.getJPanel;
import static com.Shapeville.ShapevilleMainContent.flag1;

/**
 * Interactive window for <strong>Task&nbsp;1 – Shape Recognition</strong>.
 * <p>
 * Displays eight randomly chosen 2‑D and 3‑D shapes and lets the pupil type
 * their names. The user has three attempts per shape; fewer mistakes yield a
 * higher score. A contextual hint panel, a colourful attempt counter and a
 * progress bar keep the activity engaging. The window adapts its palette via
 * {@link ColorManager} and notifies the main dashboard through static flags
 * and {@link ShapevilleMainContent#updateProgress()}. When all shapes have
 * been answered the window closes automatically.
 * </p>
 *
 * @author Lingyuan Li
 */
public class Task1Screen extends JFrame implements ColorRefreshable {
    private int attempts = 3; // Number of attempts
    private String correctAnswer; // right answers
    public int score = 0; // scores
    private JProgressBar progressBar;
    private TopNavBarPanel topPanel;
    private JToggleButton basicButton;
    private JToggleButton advancedButton;
    private JPanel shapePanel; // A panel for displaying shapes
    private JPanel progressPanel;
    private JPanel taskPanel;
    private JPanel levelPanel;
    private JPanel inputPanel;
    private JPanel hintPanel;
    private JPanel attemptPanel;
    private JPanel gradientTopWrapper;
    private JLabel hintLabel;
    private JLabel attemptDots;
    private JLabel progressLabel;
    private JLabel questionLabel;
    private JTextField styledTextField;
    private JButton submitButton;
    private JButton nextButton;
    private String attemptsText;
    private Boolean isBasic;

    // Create a counter to record the number of clicks
    int[] clickCount = { 0 }; // 使用数组来使其在 Lambda 表达式中可变

    // 2D and 3D shape arrays
    private String[] shapes2D = { "Circle", "Rectangle", "Triangle", "Oval", "Octagon", "Square", "Heptagon", "Rhombus",
            "Pentagon", "Hexagon", "Kite" };
    private String[] shapes3D = { "Cube", "Cuboid", "Cylinder", "Sphere", "Triangular prism", "Square-based pyramid",
            "Cone", "Tetrahedron" };

    private int currentShapeIndex = 0; // Record the currently loaded graphic index
    private List<String> allShapes = new ArrayList<>(); // Store all the graphics to be loaded

    // Color constant - Use ColorManager to obtain colors
    private Color orange = ColorManager.getOrange();
    private Color gray = ColorManager.getGray();
    private Color red = ColorManager.getRed();
    private Color green = ColorManager.getGreen();
    private Color yellow = ColorManager.getYellow();
    private Color blue = ColorManager.getBlue();
    private Color blueDark = ColorManager.getBlueDark();

    // Randomly select 4 from the 2D shape array
    List<String> selected2DShapes = getRandomElements(shapes2D, 4);
    // Randomly select 4 from the 3D shape array
    List<String> selected3DShapes = getRandomElements(shapes3D, 4);

    private void CreateTask1Screen() {
        setTitle("Task 1: Shape Recognition");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void CreateTask1TopNavigationBar() {
        // Top navigation bar
        gradientTopWrapper = getJPanel();
        topPanel = new TopNavBarPanel();
        gradientTopWrapper.add(topPanel);
        add(gradientTopWrapper, BorderLayout.NORTH);
        // The binding button listens for events
        topPanel.homeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Returning to Home Screen...");
            // Close the window immediately and do not display more dialog boxes
            System.out.println("Task1Screen closing from home button");
            dispose();
        });

        topPanel.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You earned " + score + " points in this session. Goodbye!");
            System.out.println("Task1Screen closing from end session button");
            dispose();
        });
    }

    private void CreateTask1ProgressBarPanel() {
        // Custom progress bar
        progressBar = new JProgressBar(0, 8);
        progressBar.setValue(0); // Set the initial progress to 0
        progressBar.setPreferredSize(new Dimension(300, 20)); // Set the size of the progress bar
        progressBar.setStringPainted(true); // Display progress text

        // Set the foreground color and background color of the progress bar
        progressBar.setForeground(new Color(23, 181, 67)); // 设置前景色（进度条颜色）
        progressBar.setBackground(new Color(229, 231, 235)); // 设置背景色（进度条背景色）
    }

    private void CreateTask1levelPanel() {
        // Create button groups for managing mutual exclusion selections
        basicButton = new JToggleButton("2D Shapes (Basic Level)");
        basicButton.setPreferredSize(new Dimension(300, 40)); // 设置按钮尺寸
        basicButton.setFont(new Font("Roboto", Font.BOLD, 16));
        basicButton.setBackground(new Color(33, 150, 243)); // 按钮颜色
        basicButton.setForeground(Color.WHITE);
        advancedButton = new JToggleButton("3D Shapes (Advanced Level)");
        advancedButton.setPreferredSize(new Dimension(300, 40)); // 设置按钮尺寸
        advancedButton.setFont(new Font("Roboto", Font.BOLD, 16));
        advancedButton.setBackground(new Color(33, 150, 243)); // 按钮颜色
        advancedButton.setForeground(Color.WHITE);
    }

    private void CreateTask1InputPanel() {
        // Question and answer input box
        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        questionLabel = new JLabel("What shape is this?");
        questionLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        styledTextField = new JTextField(20);
        styledTextField.setPreferredSize(new Dimension(300, 40));
        // set font
        styledTextField.setFont(new Font("Roboto", Font.PLAIN, 16));
        styledTextField.setForeground(Color.BLACK);

        // Set the background color and border
        styledTextField.setBackground(new Color(245, 245, 245));
        styledTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        styledTextField.setCaretColor(Color.BLACK);
        styledTextField.setSelectionColor(yellow);
        styledTextField.setSelectionColor(Color.WHITE);

        styledTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                styledTextField.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2)); // 聚焦时改变边框颜色
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                styledTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2)); // 失去焦点时恢复边框颜色
            }
        });
    }

    private void CreateTask1submitButton() {
        submitButton = new JButton("Submit");

        submitButton.setBackground(blue);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Roboto", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createLineBorder(blue, 2));

        submitButton.setBorder(BorderFactory.createCompoundBorder(
                submitButton.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        submitButton.setBackground(blue);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(blueDark); // 鼠标悬停时变暗
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(blue); // 恢复默认颜色
            }
        });
        SubmitButtonEvent();
    }

    private void SubmitButtonEvent() {
        // 提交按钮事件
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = styledTextField.getText().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    hintLabel.setText("Correct! ✅ This is indeed a " + correctAnswer + ".");
                    if (currentShapeIndex <= 3) {
                        isBasic = true;
                    } else {
                        isBasic = false;
                    }
                    score += calculateScore(isBasic, attempts);
                    System.out.println(score);
                    styledTextField.setText("");
                    showCustomDialog(score);
                    hintLabel.setFont(new Font("Roboto", Font.BOLD, 16)); // 设置字体为 Arial，字体加粗，大小为 18
                    hintLabel.setForeground(green);
                    attempts = 3;
                    updateAttempts();
                    submitButton.setEnabled(false);
                    taskPanel.add(createNextShapeButton());
                } else {
                    attempts--;
                    if (attempts > 0) {
                        hintLabel.setText("Not quite right.Try again! (" + attempts + " attempts left)");
                        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
                        hintLabel.setForeground(red);
                    } else {
                        hintLabel.setText("No more attempts. The correct answer is: " + correctAnswer);
                        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
                        hintLabel.setForeground(red);
                        styledTextField.setText("");
                        submitButton.setEnabled(false);
                        taskPanel.add(createNextShapeButton());
                    }
                    updateAttempts();
                }
            }
        });
    }

    private void CreateTask1HintPanel() {
        // Error prompt box
        hintPanel = new JPanel();
        hintPanel.setLayout(new FlowLayout());
        hintPanel.setBackground(yellow);
        hintLabel = new JLabel("You are allowed three attempts.");
        hintLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        hintLabel.setForeground(red); // 设置提示为绿色
        hintPanel.add(hintLabel);
    }

    private void CreateTask1AttemptPanel() {
        attemptPanel = new JPanel();
        attemptPanel.setLayout(new FlowLayout());
        attemptDots = new JLabel("Attempts: ");
        attemptDots.setFont(new Font("Roboto", Font.BOLD, 14));
        attemptPanel.add(attemptDots);
    }

    // The score is calculated based on the level and the number of attempts
    private int calculateScore(boolean isBasic, int attempts) {
        switch (attempts) {
            case 1:
                return isBasic ? 1 : 2;
            case 2:
                return isBasic ? 2 : 4;
            case 3:
                return isBasic ? 3 : 6;
            default:
                return 0;
        }
    }

    // Customize the correct prompt dialog box
    private void showCustomDialog(int score) {
        //Create a non-modal dialog box to avoid blocking the focus restoration of the main window
        JDialog dialog = new JDialog();
        dialog.setTitle("Score");
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 248, 255));

        // The top green icon area
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(255, 255, 255));
        iconPanel.setPreferredSize(new Dimension(300, 40));
        JLabel icon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/img_1.png")));
        iconPanel.add(icon);
        contentPanel.add(iconPanel);

        // Score display area
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        scorePanel.setBackground(new Color(240, 248, 255));

        JLabel scoreIcon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/img_2.png")));
        JLabel scoreValueLabel = new JLabel("Your current score: ");
        JLabel scoreValue = new JLabel(String.valueOf(score));
        scoreValue.setForeground(new Color(59, 130, 246));
        scoreValue.setFont(new Font("Segoe UI", Font.BOLD, 16));

        scorePanel.add(scoreIcon);
        scorePanel.add(scoreValueLabel);
        scorePanel.add(scoreValue);
        contentPanel.add(scorePanel);

        // Make sure the dialog box is closed before the task window is closed
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();

        // "Continue button"
        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(240, 248, 255));
        continueButton.setForeground(new Color(59, 130, 246));
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> dialog.dispose());
        contentPanel.add(continueButton);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void updateLevelButtonState(String shapeName) {
        boolean isBasic = selected2DShapes.contains(shapeName);
        basicButton.setSelected(!isBasic);
        advancedButton.setSelected(isBasic);
    }


    private String getColorHex(Color color) {
        return ColorManager.getColorHex(color);
    }

    private void updateAttempts() {
        attemptsText = "<html>Attempts: ";
        for (int i = 0; i < 3; i++) {
            if (attempts == 3) {
                if (i == 0)
                    attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>"; // 橙色
                else
                    attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>"; // 灰色
            } else if (attempts == 2) {
                if (i == 0)
                    attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 红色
                else if (i == 1)
                    attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>"; // 橙色
                else
                    attemptsText += "<font color='" + getColorHex(gray) + "'>● </font>"; // 灰色
            } else if (attempts == 1) {
                if (i == 0)
                    attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 红色
                else if (i == 1)
                    attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 红色
                else
                    attemptsText += "<font color='" + getColorHex(orange) + "'>● </font>"; // 橙色
            } else {
                attemptsText += "<font color='" + getColorHex(red) + "'>● </font>"; // 全部红色
            }
        }
        attemptsText += "</html>";
        attemptDots.setText(attemptsText);
    }

    private JButton createNextShapeButton() {
        nextButton = new JButton("Next Shape");
        nextButton.setBackground(new Color(33, 150, 243));
        nextButton.setForeground(Color.WHITE);
        nextButton.setPreferredSize(new Dimension(10, 40));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNextShape();


                submitButton.setEnabled(true);
                submitButton.setBackground(new Color(33, 150, 243));
                hintLabel.setText("You are allowed three attempts.");
                hintLabel.setForeground(red);
                attempts = 3;
                updateAttempts();

                taskPanel.remove(nextButton);
                taskPanel.revalidate();
                taskPanel.repaint();

                clickCount[0]++;
                System.out.println("Next Shape clicked: " + clickCount[0] + " times");

                if (clickCount[0] == 8) {
                    showCustomDialog(score);

                    Timer closeTimer = new Timer(2500, event -> {
                        System.out.println("Task1Screen closed after 8 clicks.");
                        dispose();
                    });
                    closeTimer.setRepeats(false);
                    closeTimer.start();
                }
            }
        });
        return nextButton;
    }

    private void loadNextShape() {
        if (currentShapeIndex < allShapes.size()) {
            String nextShapeName = allShapes.get(currentShapeIndex);
            try {
                ImageIcon icon = new ImageIcon(
                        getClass().getClassLoader().getResource("images/task1/" + nextShapeName + ".png"));
                JLabel nextShape = new JLabel(icon);

                shapePanel.removeAll();
                shapePanel.add(nextShape);
                shapePanel.revalidate();
                shapePanel.repaint();

                correctAnswer = nextShapeName;
                attempts = 3;
                updateAttempts();

                submitButton.setEnabled(true);
                submitButton.setBackground(new Color(33, 150, 243)); // 恢复默认背景颜色
                hintLabel.setText("Not quite right. Hint: Try to identify the shape."); // 重置提示信息
                hintLabel.setForeground(red);

                currentShapeIndex++;

                progressBar.setValue(currentShapeIndex - 1);
                progressLabel.setText("Your Progress: " + (currentShapeIndex - 1) + "/8 shapes identified");

                updateLevelButtonState(nextShapeName);

                if (currentShapeIndex >= allShapes.size()) {
                    taskPanel.remove(nextButton);
                    taskPanel.revalidate();
                    taskPanel.repaint();
                }
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("The picture was not found: " + nextShapeName, JLabel.CENTER);
                shapePanel.removeAll();
                shapePanel.add(errorLabel);
                shapePanel.revalidate();
                shapePanel.repaint();
                e.printStackTrace();
            }
        }
    }

    // 从数组中随机选择指定数量的元素
    private java.util.List<String> getRandomElements(String[] array, int count) {
        List<String> list = new ArrayList<>(Arrays.asList(array));
        Collections.shuffle(list);
        return list.subList(0, Math.min(count, list.size()));
    }

    private void configureButton(JToggleButton button) {
        button.setPreferredSize(new Dimension(300, 40));
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setForeground(Color.WHITE);


        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);


        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBackground(new Color(25, 118, 210));
            } else {
                button.setBackground(new Color(33, 150, 243));
            }
        });

        button.setBackground(new Color(33, 150, 243));
    }

    /**
     *Refresh the colors of all UI elements to respond to the changes in the color blindness mode
     */
    @Override
    public void refreshColors() {
        System.out.println("Task1Screen is refreshing the color...");

        orange = ColorManager.getOrange();
        gray = ColorManager.getGray();
        red = ColorManager.getRed();
        green = ColorManager.getGreen();
        yellow = ColorManager.getYellow();
        blue = ColorManager.getBlue();
        blueDark = ColorManager.getBlueDark();

        if (submitButton != null) {
            submitButton.setBackground(blue);
            submitButton.setForeground(Color.WHITE);
        }

        if (nextButton != null) {
            nextButton.setBackground(blue);
            nextButton.setForeground(Color.WHITE);
        }

        if (basicButton != null) {
            basicButton.setBackground(blue);
            if (basicButton.isSelected()) {
                basicButton.setBackground(blueDark);
            }
        }

        if (advancedButton != null) {
            advancedButton.setBackground(blue);
            if (advancedButton.isSelected()) {
                advancedButton.setBackground(blueDark);
            }
        }

        // Update the color of the prompt text
        if (hintLabel != null) {
            String hintText = hintLabel.getText();
            if (hintText.startsWith("Correct")) {
                hintLabel.setForeground(green);
            } else if (hintText.startsWith("Not quite") || hintText.startsWith("No more")) {
                hintLabel.setForeground(red);
            }
        }

        // Update the color of the progress bar
        if (progressBar != null) {
            progressBar.setForeground(ColorManager.getProgressBarColor());
        }

        // Update the attempt count indicator
        updateAttempts();

        // Update the text color of the input box
        if (styledTextField != null) {
            styledTextField.setSelectionColor(yellow);
        }

        // Refresh the gradient background
        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        // Redraw all the panels
        if (taskPanel != null)
            taskPanel.repaint();
        if (shapePanel != null)
            shapePanel.repaint();
        if (inputPanel != null)
            inputPanel.repaint();
        if (hintPanel != null)
            hintPanel.repaint();
        if (attemptPanel != null)
            attemptPanel.repaint();
        if (progressPanel != null)
            progressPanel.repaint();
        if (levelPanel != null)
            levelPanel.repaint();
    }

    public Task1Screen() {
        if (flag1 == 0) {
            ShapevilleMainContent.updateProgress();
            flag1 = 1;
        }

        CreateTask1Screen();

        // Top navigation bar
        CreateTask1TopNavigationBar();

        // Task Display Panel
        taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        taskPanel.setBackground(new Color(240, 248, 255));

        // Progress bar panel
        progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout());
        progressLabel = new JLabel("Your Progress: 0/8 shapes identified");
        progressPanel.setFont(new Font("Arial", Font.BOLD, 24));

        // Custom progress bar
        CreateTask1ProgressBarPanel();

        progressPanel.add(progressLabel);
        progressPanel.add(progressBar);

        taskPanel.add(progressPanel);

        // Choose 2D or 3D shapes
        levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout());

        // Create button groups for managing mutual exclusion selections
        CreateTask1levelPanel();

        levelPanel.add(basicButton);
        levelPanel.add(advancedButton);
        taskPanel.add(levelPanel);

        // Shape display area
        shapePanel = new JPanel();
        shapePanel.setLayout(new FlowLayout());
        taskPanel.add(shapePanel);

        // Question and answer input box
        CreateTask1InputPanel();

        // Create a submitButton
        CreateTask1submitButton();

        inputPanel.add(questionLabel);
        inputPanel.add(styledTextField);
        inputPanel.add(submitButton);
        taskPanel.add(inputPanel);

        // Error prompt box
        CreateTask1HintPanel();
        taskPanel.add(hintPanel);

        // Display of attempt times
        CreateTask1AttemptPanel();
        taskPanel.add(attemptPanel);

        // Merge 2D and 3D shapes into one list
        allShapes.addAll(selected2DShapes);
        allShapes.addAll(selected3DShapes);

        // Load the first shape
        loadNextShape();

        add(taskPanel, BorderLayout.CENTER); // Add the task panel to the main window

        setLocationRelativeTo(null); // Centered display
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Task1Screen().setVisible(true);
            }
        });
    }
}
