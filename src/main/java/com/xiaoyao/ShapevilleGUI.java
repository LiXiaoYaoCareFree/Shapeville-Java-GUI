package com.xiaoyao;

import com.xiaoyao.BottomBarPanel;
import com.xiaoyao.TopNavBarPanel;

import javax.swing.*;
import java.awt.*;

public class ShapevilleGUI extends JFrame {
    private boolean isColorBlindMode = false;
    private int currentProgress = 18;

    private TopNavBarPanel topPanel;
    private BottomBarPanel bottomPanel;

    public ShapevilleGUI() {
        setTitle("Shapeville");
        setSize(1000, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顶部渐变背景包裹 topPanel
        JPanel gradientTopWrapper = getJPanel();
        topPanel = new TopNavBarPanel();
        gradientTopWrapper.add(topPanel);

        // 中间部分
        CenterPanelContainer centerPanel = new CenterPanelContainer();

        // 底部面板
        bottomPanel = new BottomBarPanel(e -> toggleColorBlindMode());

        add(gradientTopWrapper, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        bindListeners();
        setLocationRelativeTo(null);
    }

    private static JPanel getJPanel() {
        JPanel gradientTopWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(63, 81, 181), getWidth(), 0, new Color(156, 39, 176));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientTopWrapper.setLayout(new BorderLayout());
        return gradientTopWrapper;
    }

    private void bindListeners() {
        topPanel.homeButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Returning to Home Screen..."));
        topPanel.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You earned " + currentProgress + " points in this session. Goodbye!");
            System.exit(0);
        });
    }

    private void toggleColorBlindMode() {
        isColorBlindMode = bottomPanel.colorBlindModeCheckBox.isSelected();
        // 可加入按钮颜色调整逻辑（如 level1Button, level2Button 等）根据 isColorBlindMode 调整颜色
    }
}
