package com.Shapeville;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI frame for the <strong>Shapeville</strong> educational game application.
 * <p>
 * The frame assembles the top navigation bar, the central content container and
 * the bottom bar, and coordinates global behaviour such as:
 * <ul>
 *   <li>Switching between a normal and a colour‑blind friendly palette.</li>
 *   <li>Keeping track of whether any task window is currently open and disabling
 *       certain controls while they are.</li>
 *   <li>Displaying session statistics when the user ends the game.</li>
 * </ul>
 * </p>
 *
 * @author Lingyuan Li
 */
public class ShapevilleGUI extends JFrame {

    /** Whether the colour‑blind palette is currently active. */
    private boolean isColorBlindMode = false;

    /** The user's current session score (e.g. number of correct answers). */
    public static int currentProgressScore = 0;

    /**
     * {@code true} if any of the {@code TaskXScreen} windows are open.
     * This flag is shared so that other components can query the state.
     */
    private static boolean otherWindowsOpen = false;

    // --- UI components -----------------------------------------------------

    /** Top navigation bar showing the logo, Home and End‑Session buttons. */
    private TopNavBarPanel topPanel;

    /** Bottom bar that hosts the colour‑blind mode checkbox and other controls. */
    private BottomBarPanel bottomPanel;

    /** Wrapper panel that paints the horizontal colour gradient behind {@link #topPanel}. */
    private JPanel gradientTopWrapper;

    /** Timer that periodically checks whether any task window is currently visible. */
    private Timer windowCheckTimer;

    // -----------------------------------------------------------------------

    /**
     * Creates and initialises the main application window, builds its child components
     * and starts the background timer that monitors auxiliary task windows.
     */
    public ShapevilleGUI() {
        setTitle("Shapeville");
        setSize(1000, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* --- North: gradient background with top navigation bar ------------------- */
        gradientTopWrapper = getJPanel();
        topPanel = new TopNavBarPanel();
        gradientTopWrapper.add(topPanel);

        /* --- Centre --------------------------------------------------------------- */
        CenterPanelContainer centerPanel = new CenterPanelContainer();

        /* --- South ---------------------------------------------------------------- */
        bottomPanel = new BottomBarPanel(e -> toggleColorBlindMode());

        add(gradientTopWrapper, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        bindListeners();
        setLocationRelativeTo(null);

        // Start monitoring the state of auxiliary task windows
        startWindowChecker();
    }

    // -----------------------------------------------------------------------
    //                          Window‑tracking logic
    // -----------------------------------------------------------------------

    /** Starts {@link #windowCheckTimer} which polls every 500 ms for open task windows. */
    private void startWindowChecker() {
        windowCheckTimer = new Timer(500, e -> checkOtherWindows());
        windowCheckTimer.start();
    }

    /**
     * Iterates over all visible {@link Window}s and updates {@link #otherWindowsOpen}
     * to reflect whether any {@code TaskXScreen} is currently shown.
     * This information is used to disable the colour‑blind mode toggle while tasks
     * are in progress.
     */
    private void checkOtherWindows() {
        boolean taskWindowsOpen = false;

        // Inspect every visible window except this one
        for (Window window : Window.getWindows()) {
            if (window.isVisible() && window != this) {
                if (window instanceof Task1Screen ||
                        window instanceof Task2Screen ||
                        window instanceof Task3Screen ||
                        window instanceof Task4Screen ||
                        window instanceof Task5Screen ||
                        window instanceof Task6Screen) {
                    taskWindowsOpen = true;
                    break;
                }
            }
        }

        // Reflect the new state and refresh the checkbox accordingly
        if (taskWindowsOpen != otherWindowsOpen) {
            otherWindowsOpen = taskWindowsOpen;
            updateColorBlindModeCheckbox();
        }
    }

    /**
     * Enables or disables the checkbox inside {@link #bottomPanel} so that the user
     * cannot toggle the palette while any task window is open.
     */
    private void updateColorBlindModeCheckbox() {
        if (bottomPanel != null && bottomPanel.colorBlindModeCheckBox != null) {
            boolean enabled = !otherWindowsOpen;
            bottomPanel.colorBlindModeCheckBox.setEnabled(enabled);

            bottomPanel.colorBlindModeCheckBox.setToolTipText(
                    enabled
                            ? "Enable a colour‑blind friendly palette"
                            : "The palette cannot be changed while a task window is open. Close all task windows first.");
        }
    }

    // -----------------------------------------------------------------------
    //                             UI helpers
    // -----------------------------------------------------------------------

    /**
     * Creates a {@link JPanel} that paints a horizontal gradient, to be used as a
     * background container for the top navigation bar.
     *
     * @return a panel with a custom {@code paintComponent} implementation
     */
    static JPanel getJPanel() {
        JPanel gradientTopWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, ColorManager.getGradientStart(),
                        getWidth(), 0, ColorManager.getGradientEnd());
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientTopWrapper.setLayout(new BorderLayout());
        return gradientTopWrapper;
    }

    /** Wires up event listeners for the top navigation buttons. */
    private void bindListeners() {
        topPanel.homeButton.addActionListener(
                e -> JOptionPane.showMessageDialog(null, "Returning to Home Screen..."));

        topPanel.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    null,
                    "You earned " + currentProgressScore + " points in this session. Goodbye!");
            System.exit(0);
        });
    }

    // -----------------------------------------------------------------------
    //                         Colour‑blind mode logic
    // -----------------------------------------------------------------------

    /**
     * Toggles between the normal and the colour‑blind friendly palette.
     * If any task window is open, the toggle is aborted and a warning is shown.
     */
    private void toggleColorBlindMode() {
        // Disallow toggling while tasks are running
        if (otherWindowsOpen) {
            JOptionPane.showMessageDialog(
                    this,
                    "The colour‑blind palette cannot be changed while a task window is open.\n" +
                            "Please close all task windows first.",
                    "Palette change blocked",
                    JOptionPane.WARNING_MESSAGE);

            // Re‑synchronise the checkbox state
            bottomPanel.colorBlindModeCheckBox.setSelected(isColorBlindMode);
            return;
        }

        isColorBlindMode = bottomPanel.colorBlindModeCheckBox.isSelected();

        // Persist the choice inside the central colour manager
        ColorManager.setColorBlindMode(isColorBlindMode);

        // Apply the new palette to all refreshable windows
        refreshUI();

        System.out.println("Colour‑blind mode " + (isColorBlindMode ? "enabled" : "disabled"));
    }

    /**
     * Repacks / repaints components so that the new palette takes effect
     * without recreating the entire Swing component tree.
     */
    private void refreshUI() {
        // Repaint the gradient background of this window
        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        // Notify all windows that implement the {@link ColorRefreshable} contract
        for (Window window : Window.getWindows()) {
            if (window instanceof ColorRefreshable) {
                ((ColorRefreshable) window).refreshColors();
            }
        }

        // Inform the user
        String message = isColorBlindMode ? "Colour‑blind palette" : "Normal palette";
        JOptionPane.showMessageDialog(
                this,
                message,
                "Palette switched",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Convenience accessor used by other classes to determine if a task window
     * is currently open.
     *
     * @return {@code true} if any {@code TaskXScreen} window is visible
     */
    public static boolean isTaskWindowOpen() {
        return otherWindowsOpen;
    }
}
