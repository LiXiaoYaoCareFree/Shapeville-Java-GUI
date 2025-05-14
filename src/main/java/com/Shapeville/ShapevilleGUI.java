package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ShapevilleGUI extends JFrame {
    private boolean isColorBlindMode = false;
    private int currentProgress = 18;
    private static boolean otherWindowsOpen = false;

    private TopNavBarPanel topPanel;
    private BottomBarPanel bottomPanel;

    private JPanel gradientTopWrapper;

    // 定时检查是否有Task窗口打开的计时器
    private Timer windowCheckTimer;

    public ShapevilleGUI() {
        setTitle("Shapeville");
        setSize(1000, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顶部渐变背景包裹 topPanel
        gradientTopWrapper = getJPanel();
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

        // 启动定时检查窗口状态的计时器
        startWindowChecker();
    }

    // 启动窗口检查计时器
    private void startWindowChecker() {
        windowCheckTimer = new Timer(500, e -> checkOtherWindows());
        windowCheckTimer.start();
    }

    // 检查是否有其他任务窗口打开
    private void checkOtherWindows() {
        boolean taskWindowsOpen = false;

        // 检查所有打开的窗口
        for (Window window : Window.getWindows()) {
            // 只检查可见的窗口
            if (window.isVisible() && window != this) {
                // 检查是否是任何Task窗口类型
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

        // 更新状态
        if (taskWindowsOpen != otherWindowsOpen) {
            otherWindowsOpen = taskWindowsOpen;
            updateColorBlindModeCheckbox();
        }
    }

    // 更新色盲模式复选框的启用状态
    private void updateColorBlindModeCheckbox() {
        if (bottomPanel != null && bottomPanel.colorBlindModeCheckBox != null) {
            boolean enabled = !otherWindowsOpen;
            bottomPanel.colorBlindModeCheckBox.setEnabled(enabled);

            // 如果禁用，提供视觉提示
            if (!enabled) {
                bottomPanel.colorBlindModeCheckBox.setToolTipText(
                        "色盲模式在任务窗口打开时无法更改。请先关闭所有任务窗口再更改此设置。");
            } else {
                bottomPanel.colorBlindModeCheckBox.setToolTipText("启用色盲友好的颜色方案");
            }
        }
    }

    // 创建自定义面板，绘制渐变色背景
    static JPanel getJPanel() {
        JPanel gradientTopWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, ColorManager.getGradientStart(), getWidth(), 0,
                        ColorManager.getGradientEnd());
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientTopWrapper.setLayout(new BorderLayout());
        return gradientTopWrapper;
    }

    // 绑定按钮监听事件
    private void bindListeners() {
        topPanel.homeButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Returning to Home Screen..."));
        topPanel.endSessionButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You earned " + currentProgress + " points in this session. Goodbye!");
            System.exit(0);
        });
    }

    // 色盲模式
    private void toggleColorBlindMode() {
        // 如果有其他任务窗口打开，不允许更改色盲模式
        if (otherWindowsOpen) {
            JOptionPane.showMessageDialog(
                    this,
                    "色盲模式在任务窗口打开时无法更改。\n请先关闭所有任务窗口再更改此设置。",
                    "无法更改色盲模式",
                    JOptionPane.WARNING_MESSAGE);

            // 恢复复选框状态以匹配当前色盲模式状态
            bottomPanel.colorBlindModeCheckBox.setSelected(isColorBlindMode);
            return;
        }

        isColorBlindMode = bottomPanel.colorBlindModeCheckBox.isSelected();
        // 更新ColorManager中的状态
        ColorManager.setColorBlindMode(isColorBlindMode);

        // 刷新UI以应用色盲模式变更
        refreshUI();

        // 开发调试信息
        System.out.println("色盲模式已" + (isColorBlindMode ? "启用" : "禁用"));
    }

    // 刷新UI以应用色盲模式变更
    private void refreshUI() {
        // 不使用updateComponentTreeUI，避免窗口重置
        // SwingUtilities.updateComponentTreeUI(this);

        // 仅刷新本窗口的顶部渐变背景
        if (gradientTopWrapper != null) {
            gradientTopWrapper.repaint();
        }

        // 对所有打开的窗口进行通知
        for (Window window : Window.getWindows()) {
            if (window instanceof ColorRefreshable) {
                ((ColorRefreshable) window).refreshColors();
            }
        }

        // 显示切换提示
        String message = isColorBlindMode ? "blind model" : "normal model";
        JOptionPane.showMessageDialog(
                this,
                message,
                "color model switching",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // 获取当前色盲模式状态
    public static boolean isTaskWindowOpen() {
        return otherWindowsOpen;
    }
}
