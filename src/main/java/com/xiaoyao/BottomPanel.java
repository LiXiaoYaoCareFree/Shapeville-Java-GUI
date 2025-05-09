package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {

    public BottomPanel() {
        // 创建底部按钮面板
        setLayout(new FlowLayout());

        JCheckBox colorBlindModeCheckBox = new JCheckBox("Enable Color Blind Mode");
        colorBlindModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton homeButton = new JButton("Home");
        JButton endSessionButton = new JButton("End Session");

        add(homeButton);
        add(endSessionButton);
        add(colorBlindModeCheckBox);
    }
}
