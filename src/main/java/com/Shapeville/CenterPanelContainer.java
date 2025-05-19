package com.Shapeville;

import javax.swing.*;
import java.awt.*;

/**
 * Vertical container placed at the centre of the <em>Shapeville</em> main window.
 * Hosts the primary game dashboard ({@link ShapevilleMainContent}) followed by
 * the {@link StageSwitcherPanel} that lets the player jump between stages. A
 * {@code BoxLayout} on the Y‑axis keeps both panels stacked and centred, while
 * an airy <code>aliceblue</code> background separates the area from the dark
 * gradient header and footer.
 *
 * @author Lingyuan Li
 */
public class CenterPanelContainer extends JPanel {

    /** Builds the container, initialises layout and adds its child panels. */
    public CenterPanelContainer() {
        // Vertical box layout for top‑to‑bottom stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 248, 255)); // AliceBlue

        // Child panels -------------------------------------------------------
        ShapevilleMainContent mainContent = new ShapevilleMainContent();
        StageSwitcherPanel    stagePanel  = new StageSwitcherPanel();

        // Let mainContent stretch horizontally but cap its height
        mainContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        mainContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        stagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with a small gap in between
        add(mainContent);
        add(Box.createVerticalStrut(10));
        add(stagePanel);
    }
}
