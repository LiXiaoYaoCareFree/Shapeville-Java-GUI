package com.Shapeville;

import java.awt.Color;

/**
 * 颜色管理器 - 集中管理应用中的所有颜色，提供色盲模式支持
 */
public class ColorManager {
    // 普通模式颜色
    private static final Color NORMAL_ORANGE = new Color(245, 158, 11);
    private static final Color NORMAL_GRAY = new Color(229, 231, 235);
    private static final Color NORMAL_RED = new Color(239, 68, 68);
    private static final Color NORMAL_GREEN = new Color(34, 197, 94);
    private static final Color NORMAL_BLUE = new Color(33, 150, 243);
    private static final Color NORMAL_BLUE_DARK = new Color(25, 118, 210);
    private static final Color NORMAL_YELLOW = new Color(254, 249, 195);
    private static final Color NORMAL_PROGRESS_BAR = new Color(23, 181, 67);
    private static final Color NORMAL_GRADIENT_START = new Color(63, 81, 181);
    private static final Color NORMAL_GRADIENT_END = new Color(156, 39, 176);

    // 色盲友好模式颜色 (使用色盲友好的颜色方案)
    private static final Color COLORBLIND_ORANGE = new Color(0, 92, 230); // 深蓝色替代橙色，提高对比度
    private static final Color COLORBLIND_GRAY = new Color(180, 180, 180); // 中灰色
    private static final Color COLORBLIND_RED = new Color(0, 0, 0); // 黑色替代红色
    private static final Color COLORBLIND_GREEN = new Color(0, 0, 255); // 纯蓝色替代绿色
    private static final Color COLORBLIND_BLUE = new Color(139, 0, 139); // 深紫色替代蓝色
    private static final Color COLORBLIND_BLUE_DARK = new Color(85, 0, 85); // 更深的紫色
    private static final Color COLORBLIND_YELLOW = new Color(190, 190, 190); // 亮灰色替代黄色
    private static final Color COLORBLIND_PROGRESS_BAR = new Color(0, 0, 255); // 纯蓝色
    private static final Color COLORBLIND_GRADIENT_START = new Color(0, 0, 139); // 深蓝色渐变开始
    private static final Color COLORBLIND_GRADIENT_END = new Color(75, 0, 130); // 深紫色渐变结束

    // 是否启用色盲模式
    private static boolean isColorBlindMode = false;

    /**
     * 设置色盲模式状态
     * 
     * @param mode true启用色盲模式，false使用普通颜色模式
     */
    public static void setColorBlindMode(boolean mode) {
        isColorBlindMode = mode;
    }

    /**
     * 获取当前色盲模式状态
     * 
     * @return 当前是否是色盲模式
     */
    public static boolean isColorBlindMode() {
        return isColorBlindMode;
    }

    /**
     * 获取橙色
     */
    public static Color getOrange() {
        return isColorBlindMode ? COLORBLIND_ORANGE : NORMAL_ORANGE;
    }

    /**
     * 获取灰色
     */
    public static Color getGray() {
        return isColorBlindMode ? COLORBLIND_GRAY : NORMAL_GRAY;
    }

    /**
     * 获取红色
     */
    public static Color getRed() {
        return isColorBlindMode ? COLORBLIND_RED : NORMAL_RED;
    }

    /**
     * 获取绿色
     */
    public static Color getGreen() {
        return isColorBlindMode ? COLORBLIND_GREEN : NORMAL_GREEN;
    }

    /**
     * 获取蓝色
     */
    public static Color getBlue() {
        return isColorBlindMode ? COLORBLIND_BLUE : NORMAL_BLUE;
    }

    /**
     * 获取深蓝色
     */
    public static Color getBlueDark() {
        return isColorBlindMode ? COLORBLIND_BLUE_DARK : NORMAL_BLUE_DARK;
    }

    /**
     * 获取黄色
     */
    public static Color getYellow() {
        return isColorBlindMode ? COLORBLIND_YELLOW : NORMAL_YELLOW;
    }

    /**
     * 获取进度条颜色
     */
    public static Color getProgressBarColor() {
        return isColorBlindMode ? COLORBLIND_PROGRESS_BAR : NORMAL_PROGRESS_BAR;
    }

    /**
     * 获取渐变起始颜色
     */
    public static Color getGradientStart() {
        return isColorBlindMode ? COLORBLIND_GRADIENT_START : NORMAL_GRADIENT_START;
    }

    /**
     * 获取渐变结束颜色
     */
    public static Color getGradientEnd() {
        return isColorBlindMode ? COLORBLIND_GRADIENT_END : NORMAL_GRADIENT_END;
    }

    /**
     * 根据当前模式转换指定颜色为色盲友好颜色
     * 适用于不在预定义列表中的颜色
     */
    public static Color adaptColor(Color originalColor) {
        if (!isColorBlindMode) {
            return originalColor;
        }

        // 如果是标准颜色，直接使用对应的色盲友好色
        if (originalColor.equals(NORMAL_RED))
            return COLORBLIND_RED;
        if (originalColor.equals(NORMAL_GREEN))
            return COLORBLIND_GREEN;
        if (originalColor.equals(NORMAL_BLUE))
            return COLORBLIND_BLUE;
        if (originalColor.equals(NORMAL_ORANGE))
            return COLORBLIND_ORANGE;
        if (originalColor.equals(NORMAL_YELLOW))
            return COLORBLIND_YELLOW;

        // 否则，使用算法转换为色盲友好颜色
        int r = originalColor.getRed();
        int g = originalColor.getGreen();
        int b = originalColor.getBlue();

        // 根据颜色主调进行分类处理
        if (r > g && r > b) { // 红色系
            // 红色系 -> 黑色（高对比度）
            return new Color(0, 0, 0);
        } else if (g > r && g > b) { // 绿色系
            // 绿色系 -> 深蓝色（高对比度）
            return new Color(0, 0, 230);
        } else if (b > r && b > g) { // 蓝色系
            // 蓝色保持，但增强亮度
            return new Color(Math.min(r + 30, 255), Math.min(g + 30, 255), b);
        } else if (r == g && r > b) { // 黄色系 (红+绿)
            // 黄色系 -> 深蓝色
            return new Color(0, 0, 200);
        } else if (r == b && r > g) { // 紫色系 (红+蓝)
            // 紫色系保持，但降低红色成分
            return new Color(r / 2, g, b);
        } else if (g == b && g > r) { // 青色系 (绿+蓝)
            // 青色系 -> 深紫色
            return new Color(130, 0, 130);
        }

        // 灰度颜色 - 保持原样但增强对比度
        if (Math.abs(r - g) < 20 && Math.abs(r - b) < 20 && Math.abs(g - b) < 20) {
            int avg = (r + g + b) / 3;
            if (avg < 128) { // 暗灰色变得更暗
                return new Color(Math.max(avg - 30, 0), Math.max(avg - 30, 0), Math.max(avg - 30, 0));
            } else { // 亮灰色变得更亮
                return new Color(Math.min(avg + 30, 255), Math.min(avg + 30, 255), Math.min(avg + 30, 255));
            }
        }

        // 其他颜色保持不变
        return originalColor;
    }

    /**
     * 获取颜色的十六进制表示
     */
    public static String getColorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}