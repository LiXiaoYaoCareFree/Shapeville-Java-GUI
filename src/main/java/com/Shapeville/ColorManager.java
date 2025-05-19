package com.Shapeville;

import java.awt.Color;

/**
 * Centralises every colour constant used by <strong>Shapeville</strong>.
 * Provides two coherent palettes -- <em>normal</em> and <em>colour-blind friendly</em>. A
 * single static flag controls the active palette; UI components obtain colours
 * only through the public getters so they switch automatically when the mode
 * changes. The helper {@link #adaptColor(Color)} maps any ad-hoc colour to a
 * high-contrast alternative when colour-blind mode is on. The class is a pure
 * static utility and must not be instantiated.
 *
 * @author Lingyuan Li
 */
public class ColorManager {

    /* ---------------- Normal palette ---------------- */
    /** Orange color used in normal mode */
    private static final Color NORMAL_ORANGE        = new Color(245, 158, 11);
    /** Gray color used in normal mode */
    private static final Color NORMAL_GRAY          = new Color(229, 231, 235);
    /** Red color used in normal mode */
    private static final Color NORMAL_RED           = new Color(239, 68, 68);
    /** Green color used in normal mode */
    private static final Color NORMAL_GREEN         = new Color(34, 197, 94);
    /** Blue color used in normal mode */
    private static final Color NORMAL_BLUE          = new Color(33, 150, 243);
    /** Dark blue color used in normal mode */
    private static final Color NORMAL_BLUE_DARK     = new Color(25, 118, 210);
    /** Yellow color used in normal mode */
    private static final Color NORMAL_YELLOW        = new Color(254, 249, 195);
    /** Progress bar color used in normal mode */
    private static final Color NORMAL_PROGRESS_BAR  = new Color(23, 181, 67);
    /** Start color for gradient in normal mode */
    private static final Color NORMAL_GRADIENT_START= new Color(63, 81, 181);
    /** End color for gradient in normal mode */
    private static final Color NORMAL_GRADIENT_END  = new Color(156, 39, 176);

    /* -------- Colour-blind friendly palette -------- */
    /** Orange color used in color-blind mode */
    private static final Color CB_ORANGE        = new Color(0, 92, 230);
    /** Gray color used in color-blind mode */
    private static final Color CB_GRAY          = new Color(180, 180, 180);
    /** Red color used in color-blind mode */
    private static final Color CB_RED           = new Color(0, 0, 0);
    /** Green color used in color-blind mode */
    private static final Color CB_GREEN         = new Color(0, 0, 255);
    /** Blue color used in color-blind mode */
    private static final Color CB_BLUE          = new Color(139, 0, 139);
    /** Dark blue color used in color-blind mode */
    private static final Color CB_BLUE_DARK     = new Color(85, 0, 85);
    /** Yellow color used in color-blind mode */
    private static final Color CB_YELLOW        = new Color(190, 190, 190);
    /** Progress bar color used in color-blind mode */
    private static final Color CB_PROGRESS_BAR  = new Color(0, 0, 255);
    /** Start color for gradient in color-blind mode */
    private static final Color CB_GRADIENT_START= new Color(0, 0, 139);
    /** End color for gradient in color-blind mode */
    private static final Color CB_GRADIENT_END  = new Color(75, 0, 130);

    /** Flag indicating whether color-blind mode is active */
    private static boolean isColorBlindMode = false;

    /* ---------------- Mode management ---------------- */

    /**
     * Enables or disables the color-blind friendly palette.
     * @param mode true to enable color-blind mode, false to use normal mode
     */
    public static void setColorBlindMode(boolean mode) {
        isColorBlindMode = mode;
    }

    /**
     * Checks if color-blind mode is currently active.
     * @return true if color-blind mode is enabled, false otherwise
     */
    public static boolean isColorBlindMode() {
        return isColorBlindMode;
    }

    /* ---------------- Palette getters ---------------- */

    /**
     * Gets the orange color based on current mode.
     * @return the appropriate orange color for the current mode
     */
    public static Color getOrange()          { return isColorBlindMode ? CB_ORANGE        : NORMAL_ORANGE; }

    /**
     * Gets the gray color based on current mode.
     * @return the appropriate gray color for the current mode
     */
    public static Color getGray()            { return isColorBlindMode ? CB_GRAY          : NORMAL_GRAY; }

    /**
     * Gets the red color based on current mode.
     * @return the appropriate red color for the current mode
     */
    public static Color getRed()             { return isColorBlindMode ? CB_RED           : NORMAL_RED; }

    /**
     * Gets the green color based on current mode.
     * @return the appropriate green color for the current mode
     */
    public static Color getGreen()           { return isColorBlindMode ? CB_GREEN         : NORMAL_GREEN; }

    /**
     * Gets the blue color based on current mode.
     * @return the appropriate blue color for the current mode
     */
    public static Color getBlue()            { return isColorBlindMode ? CB_BLUE          : NORMAL_BLUE; }

    /**
     * Gets the dark blue color based on current mode.
     * @return the appropriate dark blue color for the current mode
     */
    public static Color getBlueDark()        { return isColorBlindMode ? CB_BLUE_DARK     : NORMAL_BLUE_DARK; }

    /**
     * Gets the yellow color based on current mode.
     * @return the appropriate yellow color for the current mode
     */
    public static Color getYellow()          { return isColorBlindMode ? CB_YELLOW        : NORMAL_YELLOW; }

    /**
     * Gets the progress bar color based on current mode.
     * @return the appropriate progress bar color for the current mode
     */
    public static Color getProgressBarColor(){ return isColorBlindMode ? CB_PROGRESS_BAR  : NORMAL_PROGRESS_BAR; }

    /**
     * Gets the gradient start color based on current mode.
     * @return the appropriate gradient start color for the current mode
     */
    public static Color getGradientStart()   { return isColorBlindMode ? CB_GRADIENT_START: NORMAL_GRADIENT_START; }

    /**
     * Gets the gradient end color based on current mode.
     * @return the appropriate gradient end color for the current mode
     */
    public static Color getGradientEnd()     { return isColorBlindMode ? CB_GRADIENT_END  : NORMAL_GRADIENT_END; }

    /* ---------------- Adaptation helpers ---------------- */

    /**
     * Re-maps colours that are not part of the predefined set into safer, high-contrast
     * variants when colour-blind mode is on; otherwise returns the original colour.
     * @param originalColor the color to be adapted
     * @return the adapted color for color-blind mode, or the original color if color-blind mode is off
     */
    public static Color adaptColor(Color originalColor) {
        if (!isColorBlindMode) return originalColor;

        // Direct substitutions for known colours
        if (originalColor.equals(NORMAL_RED))    return CB_RED;
        if (originalColor.equals(NORMAL_GREEN))  return CB_GREEN;
        if (originalColor.equals(NORMAL_BLUE))   return CB_BLUE;
        if (originalColor.equals(NORMAL_ORANGE)) return CB_ORANGE;
        if (originalColor.equals(NORMAL_YELLOW)) return CB_YELLOW;

        int r = originalColor.getRed();
        int g = originalColor.getGreen();
        int b = originalColor.getBlue();

        // Heuristic mapping by dominant channel
        if (r > g && r > b)          return Color.BLACK;          // red-ish ⇒ black
        else if (g > r && g > b)     return new Color(0, 0, 230); // green-ish ⇒ blue
        else if (b > r && b > g)     return new Color(Math.min(r + 30, 255), Math.min(g + 30, 255), b);
        else if (r == g && r > b)    return new Color(0, 0, 200); // yellow-ish ⇒ dark blue
        else if (r == b && r > g)    return new Color(r / 2, g, b); // purple-ish ⇒ reduce red
        else if (g == b && g > r)    return new Color(130, 0, 130); // cyan-ish ⇒ dark purple

        // Greyscale: exaggerate contrast
        if (Math.abs(r - g) < 20 && Math.abs(r - b) < 20 && Math.abs(g - b) < 20) {
            int avg = (r + g + b) / 3;
            int delta = avg < 128 ? -30 : 30;
            int val = Math.max(0, Math.min(avg + delta, 255));
            return new Color(val, val, val);
        }

        return originalColor; // fallback
    }

    /**
     * Converts a Color object to its hexadecimal string representation.
     * @param color the color to convert
     * @return the color encoded as a HEX string (e.g. "#ffa500")
     */
    public static String getColorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}