package com.Shapeville;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TopNavBarPanel extends JPanel {
    public JButton homeButton;
    public JButton endSessionButton;

    public TopNavBarPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // 左侧 logo
        ImageIcon rawIcon = new ImageIcon(getClass().getClassLoader().getResource("images/img.png"));
        Image scaledImage = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel("Shapeville", new ImageIcon(scaledImage), JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        leftPanel.setBorder(new EmptyBorder(5, 20, 0, 0));

        // 右侧按钮
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
