package com.Shapeville;

import javax.swing.*;
import java.util.Locale;

public class MainWindow {
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
