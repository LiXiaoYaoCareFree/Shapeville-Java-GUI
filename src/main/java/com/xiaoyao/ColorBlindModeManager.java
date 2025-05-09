package com.xiaoyao;

import javax.swing.*;
import java.awt.*;

public class ColorBlindModeManager {
    private boolean isColorBlindMode = false;

    public void toggleColorBlindMode() {
        isColorBlindMode = !isColorBlindMode;
    }

    public void setButtonColor(JButton button, String color) {
        if (isColorBlindMode) {
            // 色盲模式下采用更高对比度的颜色
            switch (color) {
                case "green":
                    button.setBackground(new Color(0, 128, 0));  // 深绿色
                    break;
                case "blue":
                    button.setBackground(new Color(0, 0, 255));  // 深蓝色
                    break;
                case "purple":
                    button.setBackground(new Color(128, 0, 128));  // 深紫色
                    break;
            }
        } else {
            // 默认颜色
            switch (color) {
                case "green":
                    button.setBackground(new Color(76, 175, 80));  // 绿色
                    break;
                case "blue":
                    button.setBackground(new Color(33, 150, 243));  // 蓝色
                    break;
                case "purple":
                    button.setBackground(new Color(156, 39, 176));  // 紫色
                    break;
            }
        }
        button.setForeground(Color.WHITE);  // 设置按钮文字颜色为白色
    }
}
