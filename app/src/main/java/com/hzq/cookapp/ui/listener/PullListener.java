package com.hzq.cookapp.ui.listener;

import com.hzq.cookapp.ui.PullUpAndDownRefreshView;

/**
 * @author: hezhiqiang
 * @date: 2017/6/27
 * @version:
 * @description:
 */

public interface PullListener {
    /**
     * 下拉中
     * @param refreshLayout
     * @param fraction
     */
    void onPullingDown(PullUpAndDownRefreshView refreshLayout, float fraction);

    /**
     * 上拉中
     * @param refreshLayout
     * @param fraction
     */
    void onPullingUp(PullUpAndDownRefreshView refreshLayout, float fraction);

    /**
     * 下拉松手
     * @param refreshLayout
     * @param fraction
     */
    void onPullDownReleasing(PullUpAndDownRefreshView refreshLayout, float fraction);

    /**
     * 上拉松手
     * @param refreshLayout
     * @param fraction
     */
    void onPullUpReleasing(PullUpAndDownRefreshView refreshLayout, float fraction);

    /**
     * 刷新中
     * @param refreshLayout
     */
    void onRefreshing(PullUpAndDownRefreshView refreshLayout);

    /**
     * 加载更多中
     * @param refreshLayout
     */
    void onLoadingMore(PullUpAndDownRefreshView refreshLayout);

    /**
     * 手动调用finishRefresh或者finishLoadmore之后调用
     */
    void onFinishRefreshed();

    void onFinishLoadMore();

    /**
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    void onRefreshCanceled();

    /**
     * 正在加载更多时向下滑动屏幕，加载更多被取消
     */
    void onLoadmoreCanceled();
}
