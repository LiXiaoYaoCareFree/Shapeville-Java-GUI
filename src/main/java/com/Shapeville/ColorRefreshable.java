package com.Shapeville;

/**
 * 颜色可刷新接口 - 所有需要响应色盲模式变化的窗口或组件需实现此接口
 */
public interface ColorRefreshable {
    /**
     * 刷新所有UI元素的颜色，以响应色盲模式变化
     */
    void refreshColors();
}