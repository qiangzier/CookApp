package com.hzq.cookapp.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;

/**
 * @author: hezhiqiang
 * @date: 2017/6/28
 * @version:
 * @description:
 */

public class OverScrollProcessor {
    private PullUpAndDownRefreshView refreshView;

    //满足越界的手势的最低速度（默认3000）
    protected int OVER_SCROLL_MIN_VX = 1000;

    private int mTouchSlop;

    private VelocityTracker moveTracker;
    private int mPointerId;
    private float vy;

    //主要为了检测fling的动作，实现越界回弹
    private float mVelocityY;

    //针对部分没有OnScrollListener的View的延时策略
    private static final int MSG_START_COMPUTE_SCROLL = 0; //开始计算
    private static final int MSG_CONTINUE_COMPUTE_SCROLL = 1;//继续计算
    private static final int MSG_STOP_COMPUTE_SCROLL = 2; //停止计算
    private int cur_delay_times = 0; //当前计算次数
    private static final int ALL_DELAY_TIMES = 60;  //10ms计算一次，总共计算20次

    public OverScrollProcessor(PullUpAndDownRefreshView pullUpAndDownRefreshView){
        this.refreshView = pullUpAndDownRefreshView;
        mTouchSlop = refreshView.getTouchSlop();
    }

    public void init(){
        final View childView = refreshView.getScrollableView();
        final GestureDetector gestureDetector = new GestureDetector(refreshView.getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(refreshView.isRefreshVisible() && distanceY >= mTouchSlop && !refreshView.isFloatRefresh()){
                    refreshView.setRefreshVisible(false);
                    refreshView.getAnimProcessor().animHeadHideByVy((int) vy);
                }
                if(refreshView.isLoadingVisible() && distanceY <= -mTouchSlop){
                    refreshView.setLoadingVisible(false);
                    refreshView.getAnimProcessor().animBottomHideByVy((int) vy);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(!refreshView.isEnableOverScroll()) return false;

                mVelocityY = velocityY;
                //即不是AbsListView也不是RecyclerView，由于这些没有实现Onscrollistener接口，无法回调状态，只能采用延时策略。
                if(Math.abs(mVelocityY) >= OVER_SCROLL_MIN_VX){
                    mHandler.sendEmptyMessage(MSG_START_COMPUTE_SCROLL);
                }else{
                    mVelocityY = 0;
                    cur_delay_times = ALL_DELAY_TIMES;
                }
                return false;
            }
        });

        childView.setOnTouchListener(new View.OnTouchListener() {
            int maxVelocity = ViewConfiguration.get(refreshView.getContext()).getScaledMaximumFlingVelocity();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //手势监听的两个任务： 1.监听fling的动作，获取速度，2.监听滚共状态的变化
                obtainTracker(event);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mPointerId = event.getPointerId(0);
                        break;
                    case MotionEvent.ACTION_UP:
                        moveTracker.computeCurrentVelocity(1000,maxVelocity);
                        vy = moveTracker.getXVelocity(mPointerId);
                        releaseTracker();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        releaseTracker();
                        break;
                }
                return gestureDetector.onTouchEvent(event);
            }
        });

        if(!refreshView.isEnableOverScroll())return;

        if(childView instanceof AbsListView){
            ((AbsListView)childView).setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(refreshView.allowOverScroll() && firstVisibleItem == 0 || ((AbsListView)childView).getLastVisiblePosition() == totalItemCount - 1){
                        if(mVelocityY >= OVER_SCROLL_MIN_VX && ScrollingUtil.isAbsListViewToTop((AbsListView) childView)){
                            refreshView.getAnimProcessor().animOverScrollTop(mVelocityY,cur_delay_times);
                            mVelocityY = 0;
                            cur_delay_times = ALL_DELAY_TIMES;
                        }
                        if(mVelocityY <= -OVER_SCROLL_MIN_VX && ScrollingUtil.isAbsListViewToBottom((AbsListView) childView)){
                            refreshView.getAnimProcessor().animOverScrollBottom(mVelocityY,cur_delay_times);
                            mVelocityY = 0;
                            cur_delay_times = ALL_DELAY_TIMES;
                        }
                    }
                }
            });
        }else if(childView instanceof RecyclerView){
            ((RecyclerView)childView).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if(refreshView.allowOverScroll() && newState == RecyclerView.SCROLL_STATE_IDLE){
                        if(mVelocityY >= OVER_SCROLL_MIN_VX && ScrollingUtil.isRecyclerViewToTop((RecyclerView) childView)){
                            refreshView.getAnimProcessor().animOverScrollTop(mVelocityY,cur_delay_times);
                            mVelocityY = 0;
                            cur_delay_times = ALL_DELAY_TIMES;
                        }
                        if(mVelocityY <= -OVER_SCROLL_MIN_VX && ScrollingUtil.isRecyclerViewToBottom((RecyclerView) childView)){
                            refreshView.getAnimProcessor().animOverScrollBottom(mVelocityY,cur_delay_times);
                            mVelocityY = 0;
                            cur_delay_times = ALL_DELAY_TIMES;
                        }
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }

    private void obtainTracker(MotionEvent event){
        if(null == moveTracker){
            moveTracker = VelocityTracker.obtain();
        }
        moveTracker.addMovement(event);
    }

    private void releaseTracker(){
        if(moveTracker != null){
            moveTracker.clear();
            moveTracker.recycle();
            moveTracker = null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_COMPUTE_SCROLL:
                    cur_delay_times = -1; //这里没有break,写作-1方便计数
                case MSG_CONTINUE_COMPUTE_SCROLL:
                    cur_delay_times++;

                    View mChildView = refreshView.getScrollableView();

                    if (!(mChildView instanceof AbsListView || mChildView instanceof RecyclerView)) {

                        if (refreshView.allowOverScroll() && mVelocityY >= OVER_SCROLL_MIN_VX && (mChildView != null && Math.abs(mChildView.getScrollY()) <= 2 * mTouchSlop)) {
                            refreshView.getAnimProcessor().animOverScrollTop(mVelocityY, cur_delay_times);
                            mVelocityY = 0;
                            cur_delay_times = ALL_DELAY_TIMES;
                        }

                        if (refreshView.allowOverScroll() && mVelocityY <= -OVER_SCROLL_MIN_VX && mChildView != null) {
                            if (mChildView instanceof WebView) {
                                WebView webview = (WebView) (mChildView);
                                if (Math.abs(webview.getContentHeight() * webview.getScale() - (webview.getHeight() + webview.getScrollY())) <= 2 * mTouchSlop) {
                                    refreshView.getAnimProcessor().animOverScrollBottom(mVelocityY, cur_delay_times);
                                    mVelocityY = 0;
                                    cur_delay_times = ALL_DELAY_TIMES;
                                }
                            } else if (mChildView instanceof ViewGroup) {
                                View subChildView = ((ViewGroup) mChildView).getChildAt(0);
                                if (subChildView != null && subChildView.getMeasuredHeight() <= mChildView.getScrollY() + mChildView.getHeight()) {
                                    //滚动到了底部
                                    refreshView.getAnimProcessor().animOverScrollBottom(mVelocityY, cur_delay_times);
                                    mVelocityY = 0;
                                    cur_delay_times = ALL_DELAY_TIMES;
                                }
                            } else if (mChildView.getScrollY() >= mChildView.getHeight()) {
                                refreshView.getAnimProcessor().animOverScrollBottom(mVelocityY, cur_delay_times);
                                mVelocityY = 0;
                                cur_delay_times = ALL_DELAY_TIMES;
                            }
                        }
                    }

                    if (cur_delay_times < ALL_DELAY_TIMES)
                        mHandler.sendEmptyMessageDelayed(MSG_CONTINUE_COMPUTE_SCROLL, 10);
                    break;
                case MSG_STOP_COMPUTE_SCROLL:
                    cur_delay_times = ALL_DELAY_TIMES;
                    break;
            }
        }
    };

}
