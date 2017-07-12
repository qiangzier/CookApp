package com.hzq.cookapp.helper;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description:
 */

public interface ItemTouchHelperAdapter {
    //拖拽
    void onItemMove(int fromPosition,int toPosition);

    //侧滑
    void onSwipe(int position);
}
