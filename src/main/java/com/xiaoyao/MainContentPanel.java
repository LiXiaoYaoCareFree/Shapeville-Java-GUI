package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class MainContentPanel extends JPanel {

    public MainContentPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255)); // 淡蓝背景

        // 添加形状卡片
        ShapevilleMainContent mainContent = new ShapevilleMainContent();
        StageSwitcherPanel stagePanel = new StageSwitcherPanel();

        // 可设置最大高度（例如 mainContent 占 300px，高度可控）
        mainContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        stagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(mainContent);
        add(Box.createVerticalStrut(10)); // 间隔
        add(stagePanel);
    }
}
