package com.Shapeville;

import javax.swing.*;
import java.util.Locale;

/**
 * Entry point for the <em>Shapeville</em> desktop game.
 * <p>
 * Launches Swing on the Event&nbsp;Dispatch&nbsp;Thread, switches the default
 * JVM locale to English so that all UI strings are rendered in a single
 * language, and finally instantiates and displays the {@link ShapevilleGUI}
 * frame.
 * </p>
 *
 * @author Lingyuan Li
 */
public class MainWindow {

    /**
     * Boots the application.
     * <ul>
     *   <li>Schedules the startup work on the EDT via {@link SwingUtilities#invokeLater(Runnable)}.</li>
     *   <li>Sets {@code Locale.ENGLISH} as the program‑wide default.</li>
     *   <li>Creates and shows the primary window.</li>
     * </ul>
     *
     * @param args command‑line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Locale.setDefault(Locale.ENGLISH);
                ShapevilleGUI frame = new ShapevilleGUI();
                frame.setVisible(true);
            }
        });
    }
}
