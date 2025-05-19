package com.Shapeville;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Slim footer bar for the <em>Shapeville</em> window.
 * Contains a single checkbox that lets the user turn the colour‑blind palette
 * on or off. The parent frame injects an {@link ActionListener} so that the
 * toggle logic lives in one place; this panel is therefore only responsible
 * for presentation. A simple {@link FlowLayout} keeps the bar compact while
 * allowing future controls to be added easily.
 *
 * @author Lingyuan Li
 */
public class BottomBarPanel extends JPanel {

    /** Exposed so the main frame can query or update its state. */
    public final JCheckBox colorBlindModeCheckBox;

    /**
     * Builds the bar and wires the supplied listener to the checkbox.
     *
     * @param toggleColorListener handler that flips the palette in
     *                            {@link ColorManager}
     */
    public BottomBarPanel(ActionListener toggleColorListener) {
        setLayout(new FlowLayout());

        colorBlindModeCheckBox = new JCheckBox("Enable Color Blind Mode");
        colorBlindModeCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        colorBlindModeCheckBox.setToolTipText("Enable colour‑blind friendly palette");
        colorBlindModeCheckBox.addActionListener(toggleColorListener);

        add(colorBlindModeCheckBox);
    }
}
