package com.hzq.indicator.callback;

/**
 * @author: hezhiqiang
 * @date: 2017/7/3
 * @version:
 * @description:
 */

public interface OnNavigatorScrollListener {
    void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);

    void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

    void onSelected(int index, int totalCount);

    void onDeselected(int index, int totalCount);
}
