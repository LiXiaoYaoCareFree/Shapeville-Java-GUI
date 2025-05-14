package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StageSwitcherPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel cardContainer;

    public StageSwitcherPanel() {
        setLayout(new BorderLayout());

        // Stage buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JToggleButton ks1 = new JToggleButton("<html><b>Key Stage 1</b><br>(Years 1–2)</html>");
        JToggleButton ks2 = new JToggleButton("<html><b>Key Stage 2</b><br>(Years 3–4)</html>");

        ButtonGroup group = new ButtonGroup();
        group.add(ks1);
        group.add(ks2);
        ks1.setSelected(true);
        decorateButton(ks1, true);
        decorateButton(ks2, false);
        buttonPanel.add(ks1);
        buttonPanel.add(ks2);

        add(buttonPanel, BorderLayout.NORTH);

        // Content cards
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.add(createStage1(), "KS1");
        cardContainer.add(createStage2(), "KS2");
        add(cardContainer, BorderLayout.CENTER);

        ks1.addActionListener(e -> {
            cardLayout.show(cardContainer, "KS1");
            decorateButton(ks1, true);
            decorateButton(ks2, false);
        });
        ks2.addActionListener(e -> {
            cardLayout.show(cardContainer, "KS2");
            decorateButton(ks1, false);
            decorateButton(ks2, true);
        });
    }

    private void decorateButton(JToggleButton btn, boolean active) {
        if (active) {
            btn.setBackground(new Color(66, 133, 244));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createMatteBorder(0, 0, 8, 0, new Color(66, 133, 244)));
        } else {
            btn.setBackground(new Color(230, 230, 230));
            btn.setForeground(Color.DARK_GRAY);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
    }

    private JPanel createStage1() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(240, 250, 255));

        TaskCard task1 = new TaskCard("Task 1", "Shape Recognition",
                "Learn to identify basic 2D shapes like circles, squares, triangles, rectangles, and more!", "Ages 5–7",
                new Color(76, 175, 80), loadIcon("shapes.png"));
        task1.addStartButtonListener(e -> startTask1());

        TaskCard task2 = new TaskCard("Task 2", "Angle Types",
                "Learn about different types of angles: right angles, acute angles, and obtuse angles!", "Ages 5–7",
                new Color(76, 175, 80), loadIcon("angles.png"));
        task2.addStartButtonListener(e -> startTask2());

        panel.add(task1);
        panel.add(task2);
        return panel;
    }

    private JPanel createStage2() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 250, 255));

        // 第一行的任务
        JPanel row1 = new JPanel(new FlowLayout());
        row1.setOpaque(false);
        TaskCard task3 = new TaskCard("Task 3", "Shape Area",
                "Learn how to calculate the area of rectangles, triangles, and other 2D shapes!", "Ages 7–10",
                new Color(33, 150, 243), loadIcon("area.png"));
        task3.addStartButtonListener(e -> startTask3());

        TaskCard task4 = new TaskCard("Task 4", "Circle Area & Circumference",
                "Discover how to calculate the area and circumference of circles using π!", "Ages 7–10",
                new Color(33, 150, 243), loadIcon("Circle.png"));
        task4.addStartButtonListener(e -> startTask4());

        row1.add(task3);
        row1.add(task4);

        // 第二行的任务
        JPanel row2 = new JPanel(new FlowLayout());
        row2.setOpaque(false);
        TaskCard task5 = new TaskCard("Challenge 1", "Compound Shapes",
                "Learn to calculate the area of compound shapes by breaking them into simpler shapes!", "Advanced",
                new Color(156, 39, 176), loadIcon("compound.png"));
        task5.addStartButtonListener(e -> startTask5());

        TaskCard task6 = new TaskCard("Challenge 2", "Sectors & Arcs",
                "Master calculating the area of sectors and the length of arcs in circles!", "Advanced",
                new Color(156, 39, 176), loadIcon("sectors.png"));
        task6.addStartButtonListener(e -> startTask6());

        row2.add(task5);
        row2.add(task6);

        panel.add(row1);
        panel.add(row2);
        return panel;
    }

    private ImageIcon loadIcon(String name) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getClassLoader().getResource("images/" + name));
            Image img = raw.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    // 配置任务窗口，使其总是在前，并禁用主窗口直到任务窗口关闭
    private void configureTaskWindow(JFrame taskWindow) {
        // 设置总是在最上层，阻止与其他窗口的交互
        taskWindow.setAlwaysOnTop(true);
        // 获取顶层窗口作为所有者（通常是主应用窗口）
        Window owner = SwingUtilities.getWindowAncestor(this);
        if (owner instanceof Frame) {
            // 暂存引用，以便在自定义的事件处理中使用
            final Frame ownerFrame = (Frame) owner;
            // 使主窗口无法获得焦点，直到任务窗口关闭
            ownerFrame.setEnabled(false);

            // 添加窗口关闭监听器，在任务窗口关闭时重新启用主窗口
            taskWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    enableOwnerWindow(ownerFrame);
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    enableOwnerWindow(ownerFrame);
                }
            });

            // 添加一个窗口状态监听器来检测任务窗口是否仍然可见
            // 这可以捕获任何方式的窗口关闭，包括Home和Exit按钮
            new Timer(100, e -> {
                if (!taskWindow.isVisible() || !taskWindow.isDisplayable()) {
                    ((Timer) e.getSource()).stop();
                    enableOwnerWindow(ownerFrame);
                }
            }).start();
        }
    }

    // 辅助方法：在适当的时候启用主窗口
    private void enableOwnerWindow(Frame ownerFrame) {
        SwingUtilities.invokeLater(() -> {
            ownerFrame.setEnabled(true);
            ownerFrame.requestFocus();
            ownerFrame.toFront();
        });
    }

    // 启动任务1
    public void startTask1() {
        System.out.println("Starting Task 1: Shape Recognition");
        Task1Screen task1Screen = new Task1Screen();
        configureTaskWindow(task1Screen);
        task1Screen.setVisible(true); // 显示任务界面
    }

    // 启动任务2
    public void startTask2() {
        System.out.println("Starting Task 2: Angle Types");
        Task2Screen task2Screen = new Task2Screen();
        configureTaskWindow(task2Screen);
        task2Screen.setVisible(true);
    }

    // 启动任务3
    public void startTask3() {
        System.out.println("Starting Task 3: Shape Area");
        Task3Screen task3Screen = new Task3Screen();
        configureTaskWindow(task3Screen);
        task3Screen.setVisible(true);
    }

    // 启动任务4
    public void startTask4() {
        System.out.println("Starting Task 4: Circle Area & Circumference");
        Task4Screen task4Screen = new Task4Screen();
        configureTaskWindow(task4Screen);
        task4Screen.setVisible(true);
    }

    // 启动任务5
    public void startTask5() {
        System.out.println("Starting Task 5: Compound Shapes");
        Task5Screen task5Screen = new Task5Screen();
        configureTaskWindow(task5Screen);
        task5Screen.setVisible(true);
    }

    // 启动任务6
    public void startTask6() {
        System.out.println("Starting Task 6: Sectors & Arcs");
        Task6Screen task6Screen = new Task6Screen();
        configureTaskWindow(task6Screen);
        task6Screen.setVisible(true);
    }
}
