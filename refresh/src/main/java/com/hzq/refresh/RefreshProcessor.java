package com.hzq.refresh;

import android.view.MotionEvent;

/**
 * @author: hezhiqiang
 * @date: 2017/6/27
 * @version:
 * @description:
 */

public class RefreshProcessor {

    private float mTouchX,mTouchY;
    private PullUpAndDownRefreshView refreshView;

    public RefreshProcessor(PullUpAndDownRefreshView coProcessor){
        this.refreshView = coProcessor;
    }

    public boolean interceptTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - mTouchX;
                float dy = event.getY() - mTouchY;
                if(Math.abs(dx) <= Math.abs(dy)){ //滑动允许最大角度为45度
                    if(dy > 0 && !ScrollingUtil.canChildScrollUp(refreshView.getScrollableView()) && refreshView.allowPullDown()){
                        refreshView.setStatePTD();
                        return true;
                    }else if(dy < 0 && !ScrollingUtil.canChildScrollDown(refreshView.getScrollableView()) && refreshView.allowPullUp()){
                        refreshView.setStatePBU();
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public boolean consumeTouchEvent(MotionEvent event){
        if(refreshView.isRefreshVisible() || refreshView.isLoadingVisible()) return false;

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mTouchY;
                if(refreshView.isStatePTD()){
                    dy = Math.min(refreshView.getMaxHeadHeight() * 2,dy);
                    dy = Math.max(0,dy);

                    refreshView.getAnimProcessor().scrollHeadByMove(dy);
                    return true;
                }else if(refreshView.isStatePBU()){
                    //加载更多
                    dy = Math.min(refreshView.getBottomHeight() * 2,Math.abs(dy));
                    dy = Math.max(0,dy);

                    refreshView.getAnimProcessor().scrollBottomByMove(dy);
                    return true;
                }else
                    return false;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(refreshView.isStatePTD()){
                    refreshView.getAnimProcessor().dealPullDownRelease();
                    return true;
                }else if(refreshView.isStatePBU()){
                    refreshView.getAnimProcessor().dealPullUpRelease();
                    return true;
                }
        }
        return false;
    }
}
