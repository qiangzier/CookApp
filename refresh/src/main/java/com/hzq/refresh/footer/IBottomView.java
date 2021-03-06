package com.hzq.refresh.footer;

import android.view.View;

/**
 * @author: hezhiqiang
 * @date: 2017/6/27
 * @version:
 * @description:
 */

public interface IBottomView {
    View getView();

    /**
     * 上拉准备加载更多的动作
     * @param fraction          上拉高度与Bottom总高度之比
     * @param maxHeadHeight     头部可拉伸最大高度
     * @param headHeight        头部高度
     */
    void onPullingUp(float fraction, float maxHeadHeight, float headHeight);

    void startAnim(float maxHeadHeight, float headHeight);

    /**
     * 上拉释放过程
     * @param fraction
     * @param maxHeadHeight
     * @param headHeight
     */
    void onPullReleasing(float fraction, float maxHeadHeight, float headHeight);

    void onFinish();

    void reset();
}
