package com.Shapeville;

/**
 * Color Refresh interface - All Windows or components that need to respond to
 * changes in color blindness modes must implement this interface
 */
public interface ColorRefreshable {
    /**
     * Refresh the colors of all UI elements to respond to the changes in the color blindness mode
     */
    void refreshColors();
}