package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class TaskScreen extends JFrame {
    private String taskName;

    public TaskScreen() {
        // 设置窗口标题
        setTitle("Task Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // 关闭当前窗口

        // 初始化界面内容
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 任务名称
        JLabel taskTitle = new JLabel("Welcome to " + taskName, SwingConstants.CENTER);
        taskTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(taskTitle);

        // 任务说明内容
        JLabel taskDescription = new JLabel("<html>Here you will perform various tasks related to shapes and geometry.<br>Good luck!</html>", SwingConstants.CENTER);
        taskDescription.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(taskDescription);

        // 显示任务
        // 根据任务名称，加载不同的任务内容（例如，任务 1 是 "识别图形"，任务 2 是 "计算面积"）
        loadTaskContent(panel);

        // 将面板加入到窗口
        add(panel);
        setLocationRelativeTo(null);  // 居中显示
    }

    // 根据任务名称加载不同的任务内容
    private void loadTaskContent(JPanel panel) {
        if ("Shape Recognition".equals(taskName)) {
            // 任务1内容，图形识别
            panel.add(new JLabel("Identify shapes like Circle, Square, Triangle..."));
        } else if ("Angle Types".equals(taskName)) {
            // 任务2内容，角度类型
            panel.add(new JLabel("Identify different angles like Acute, Right, and Obtuse."));
        } else if ("Shape Area".equals(taskName)) {
            // 任务3内容，计算形状面积
            panel.add(new JLabel("Learn how to calculate the area of rectangles, triangles, and other 2D shapes!"));
        } else if ("Circle Area & Circumference".equals(taskName)) {
            // 任务4内容，圆的面积与周长
            panel.add(new JLabel("Discover how to calculate the area and circumference of circles using π!"));
        } else if ("Compound Shapes".equals(taskName)) {
            // 任务5内容，复合形状
            panel.add(new JLabel("Learn to calculate the area of compound shapes by breaking them into simpler shapes!"));
        } else if ("Sectors & Arcs".equals(taskName)) {
            // 任务6内容，扇形与弧长
            panel.add(new JLabel("Master calculating the area of sectors and the length of arcs in circles!"));
        } else {
            // 其他任务内容
            panel.add(new JLabel("Other tasks will be shown here."));
        }
    }


    // 设置任务名称
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
