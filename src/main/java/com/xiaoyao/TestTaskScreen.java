package com.xiaoyao;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TestTaskScreen extends JFrame {
    private String[] shapes2D = {"Circle", "Rectangle", "Triangle", "Oval", "Octagon", "Square", "Heptagon", "Rhombus", "Pentagon", "Hexagon", "Kite"};
    private String[] shapes3D = {"Cube", "Cuboid", "Cylinder", "Sphere", "Triangular prism", "Square-based pyramid", "Cone", "Tetrahedron"};

    // 随机选择并加载图片的方法
    public void loadRandomShapes() {
        // 创建主面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        // 从2D形状数组中随机选择4个
        List<String> selected2DShapes = getRandomElements(shapes2D, 4);
        // 从3D形状数组中随机选择4个
        List<String> selected3DShapes = getRandomElements(shapes3D, 4);

        // 加载并显示2D形状图片
        for (String shape : selected2DShapes) {
            mainPanel.add(createShapePanel(shape));
        }

        // 加载并显示3D形状图片
        for (String shape : selected3DShapes) {
            mainPanel.add(createShapePanel(shape));
        }

        // 创建并显示窗口
        JFrame frame = new JFrame("随机形状图片");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // 从数组中随机选择指定数量的元素
    private List<String> getRandomElements(String[] array, int count) {
        List<String> list = new ArrayList<>(Arrays.asList(array));
        Collections.shuffle(list);
        return list.subList(0, Math.min(count, list.size()));
    }

    // 创建包含形状图片和名称的面板
    private JPanel createShapePanel(String shape) {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            // 加载图片
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/task1/" + shape + ".png"));
            JLabel shapeIcon = new JLabel(icon);
            panel.add(shapeIcon, BorderLayout.CENTER);
        } catch (Exception e) {
            // 图片加载失败时显示默认文本
            JLabel errorLabel = new JLabel("图片未找到: " + shape, JLabel.CENTER);
            panel.add(errorLabel, BorderLayout.CENTER);
            e.printStackTrace();
        }

        // 添加形状名称标签
        JLabel nameLabel = new JLabel(shape, JLabel.CENTER);
        panel.add(nameLabel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TestTaskScreen().loadRandomShapes());
    }
}