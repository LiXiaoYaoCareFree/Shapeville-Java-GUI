package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class CenterPanelContainer extends JPanel {
    public CenterPanelContainer() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255));

        //
        ShapevilleMainContent mainContent = new ShapevilleMainContent();
        StageSwitcherPanel stagePanel = new StageSwitcherPanel();

        mainContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        stagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(mainContent);
        add(Box.createVerticalStrut(10));
        add(stagePanel);
    }
}
