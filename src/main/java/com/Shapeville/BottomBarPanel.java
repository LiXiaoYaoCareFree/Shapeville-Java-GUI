package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BottomBarPanel extends JPanel {
    public JCheckBox colorBlindModeCheckBox;

    public BottomBarPanel(ActionListener toggleColorListener) {
        setLayout(new FlowLayout());

        colorBlindModeCheckBox = new JCheckBox("Enable Color Blind Mode");
        colorBlindModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        colorBlindModeCheckBox.addActionListener(toggleColorListener);
        colorBlindModeCheckBox.setToolTipText("启用色盲友好的颜色方案");

        add(colorBlindModeCheckBox);
    }
}
