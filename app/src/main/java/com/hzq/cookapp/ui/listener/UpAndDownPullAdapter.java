package com.hzq.cookapp.ui.listener;

import com.hzq.cookapp.ui.PullUpAndDownRefreshView;

/**
 * @author: hezhiqiang
 * @date: 2017/6/27
 * @version:
 * @description:
 */

public abstract class UpAndDownPullAdapter implements PullListener {
    @Override
    public void onPullingDown(PullUpAndDownRefreshView refreshLayout, float fraction) {

    }

    @Override
    public void onPullingUp(PullUpAndDownRefreshView refreshLayout, float fraction) {

    }

    @Override
    public void onPullDownReleasing(PullUpAndDownRefreshView refreshLayout, float fraction) {

    }

    @Override
    public void onPullUpReleasing(PullUpAndDownRefreshView refreshLayout, float fraction) {

    }

    @Override
    public void onRefreshing(PullUpAndDownRefreshView refreshLayout) {

    }

    @Override
    public void onLoadingMore(PullUpAndDownRefreshView refreshLayout) {

    }

    @Override
    public void onFinishRefreshed() {

    }

    @Override
    public void onFinishLoadMore() {

    }

    @Override
    public void onRefreshCanceled() {

    }

    @Override
    public void onLoadmoreCanceled() {

    }
}
