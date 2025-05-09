package com.xiaoyao;

import javax.swing.*;

public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShapevilleGUI frame = new ShapevilleGUI();
                frame.setVisible(true);
            }
        });
    }
}
