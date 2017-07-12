package com.hzq.cookapp.helper;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description:
 */

public interface OnDragVHListener {

    /**
     * item被选中时触发
     */
    void onItemSelected();

    /**
     * item拖拽结束时触发
     */
    void onItemFinish();
}
