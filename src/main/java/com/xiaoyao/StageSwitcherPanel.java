package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class StageSwitcherPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel cardContainer;

    public StageSwitcherPanel() {
        setLayout(new BorderLayout());

        // Stage buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JToggleButton ks1 = new JToggleButton("<html><b>Key Stage 1</b><br>(Years 1–2)</html>");
        JToggleButton ks2 = new JToggleButton("<html><b>Key Stage 2</b><br>(Years 3–6)</html>");

        ButtonGroup group = new ButtonGroup();
        group.add(ks1); group.add(ks2);
        ks1.setSelected(true);
        decorateButton(ks1, true);
        decorateButton(ks2, false);
        buttonPanel.add(ks1); buttonPanel.add(ks2);

        add(buttonPanel, BorderLayout.NORTH);

        // Content cards
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.add(createStage1(), "KS1");
        cardContainer.add(createStage2(), "KS2");
        add(cardContainer, BorderLayout.CENTER);

        ks1.addActionListener(e -> { cardLayout.show(cardContainer, "KS1"); decorateButton(ks1, true); decorateButton(ks2, false); });
        ks2.addActionListener(e -> { cardLayout.show(cardContainer, "KS2"); decorateButton(ks1, false); decorateButton(ks2, true); });
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
        panel.add(new TaskCard("Task 1", "Shape Recognition", "Identify basic 2D shapes...", "Ages 5–7", new Color(76, 175, 80), loadIcon("shapes.png")));
        panel.add(new TaskCard("Task 2", "Angle Types", "Learn right, acute, obtuse...", "Ages 5–7", new Color(76, 175, 80), loadIcon("angles.png")));
        return panel;
    }

    private JPanel createStage2() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 250, 255));

        JPanel row1 = new JPanel(new FlowLayout());
        row1.setOpaque(false);
        row1.add(new TaskCard("Task 3", "Shape Area", "Area of rectangles, triangles...", "Ages 7–10", new Color(33, 150, 243), loadIcon("area.png")));
        row1.add(new TaskCard("Task 4", "Circle Area & Circumference", "Using π to calculate...", "Ages 7–10", new Color(33, 150, 243), loadIcon("circle.png")));

        JPanel row2 = new JPanel(new FlowLayout());
        row2.setOpaque(false);
        row2.add(new TaskCard("Challenge 1", "Compound Shapes", "Break complex shapes...", "Advanced", new Color(156, 39, 176), loadIcon("compound.png")));
        row2.add(new TaskCard("Challenge 2", "Sectors & Arcs", "Sector area and arc length...", "Advanced", new Color(156, 39, 176), loadIcon("sectors.png")));

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
}

