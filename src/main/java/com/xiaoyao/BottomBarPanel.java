package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BottomBarPanel extends JPanel {
    public JCheckBox colorBlindModeCheckBox;
    public JButton homeButton;
    public JButton endSessionButton;

    public BottomBarPanel(ActionListener toggleColorListener) {
        setLayout(new FlowLayout());
        homeButton = new JButton("Home");
        endSessionButton = new JButton("End Session");

        colorBlindModeCheckBox = new JCheckBox("Enable Color Blind Mode");
        colorBlindModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        colorBlindModeCheckBox.addActionListener(toggleColorListener);

        add(homeButton);
        add(endSessionButton);
        add(colorBlindModeCheckBox);
    }
}
