package com.Shapeville;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Slim, gradient-friendly navigation bar reused at the top of every
 * Shapeville window.
 * <p>
 * The left side shows the application logo ("Shapeville") with a
 * 30 × 30 px icon; the right side holds two coloured text-buttons:
 * "Home" returns to the main menu, "End Session" closes the current
 * task after showing a summary.  The panel itself is transparent so the
 * parent frame can paint a gradient behind it; internal sub-panels are
 * opaque to preserve button alignment.  All child components expose
 * their references (public fields) so callers can attach listeners
 * directly.
 * <p>
 * Author : Lingyuan Li
 */
public class TopNavBarPanel extends JPanel {
    /**
     * Button that navigates to the home screen
     */
    public JButton homeButton;

    /**
     * Button that ends the current session
     */
    public JButton endSessionButton;

    /**
     * The raw application icon loaded from resources
     */
    public ImageIcon rawIcon;

    /**
     * The scaled version of the application icon (30x30 pixels)
     */
    public Image scaledImage;

    /**
     * Constructs a new TopNavBarPanel with the Shapeville logo and navigation buttons.
     * The panel is transparent to allow gradient backgrounds, with opaque sub-panels
     * for proper component alignment.
     */
    public TopNavBarPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        rawIcon = new ImageIcon(getClass().getClassLoader().getResource("images/img.png"));
        scaledImage = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel("Shapeville", new ImageIcon(scaledImage), JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        leftPanel.setBorder(new EmptyBorder(5, 20, 0, 0));

        homeButton = createButton("Home", "images/home.png", Color.BLUE);
        endSessionButton = createButton("End Session", "images/logout.png", Color.RED);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        rightPanel.add(homeButton);
        rightPanel.add(endSessionButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Creates a styled button with the specified text, icon, and color.
     *
     * @param text The text to display on the button
     * @param iconPath The path to the button's icon in the resources
     * @param color The color of the button's text
     * @return A new JButton with the specified styling
     */
    private JButton createButton(String text, String iconPath, Color color) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JButton button = new JButton(text, new ImageIcon(scaledImage));
        button.setBackground(Color.WHITE);
        button.setForeground(color);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        return button;
    }
}
