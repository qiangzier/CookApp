package com.hzq.refresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.hzq.refresh.listener.AnimatorListenerAdapter;

import static android.view.View.GONE;

/**
 * @author: hezhiqiang
 * @date: 2017/6/27
 * @version:
 * @description:
 */

public class AnimProcessor {
    private PullUpAndDownRefreshView refreshView;
    private static final  float animFraction = 1f;
    //动画变换率
    private DecelerateInterpolator decelerateInterpolator;
    public AnimProcessor(PullUpAndDownRefreshView upAndDownPullAdapter){
        this.refreshView = upAndDownPullAdapter;
        decelerateInterpolator = new DecelerateInterpolator(8);
    }

    public void init(){

    }

    public void scrollHeadByMove(float moveY){
        float offertY = decelerateInterpolator.getInterpolation(moveY / refreshView.getMaxHeadHeight() / 2) * moveY / 2;
        if(refreshView.getHeader().getVisibility() != View.VISIBLE)
            refreshView.getHeader().setVisibility(View.VISIBLE);
        if(refreshView.isPureScrollModeOn())
            refreshView.getHeader().setVisibility(View.GONE);
        refreshView.getHeader().getLayoutParams().height = (int)Math.abs(offertY);
        refreshView.getHeader().requestLayout();

        if(!refreshView.isFloatRefresh()){
            refreshView.getScrollableView().setTranslationY(offertY);
            translateExHead((int)offertY);
        }
        refreshView.onPullingDown(offertY);
    }

    public void scrollBottomByMove(float moveY){
        float offsetY = decelerateInterpolator.getInterpolation(moveY / refreshView.getBottomHeight() / 2) * moveY / 2;
        if(refreshView.getFooter().getVisibility() != View.VISIBLE)
            refreshView.getFooter().setVisibility(View.VISIBLE);
        if(refreshView.isPureScrollModeOn())
            refreshView.getFooter().setVisibility(View.GONE);
        refreshView.getFooter().getLayoutParams().height = (int) Math.abs(offsetY);
        refreshView.getFooter().requestLayout();

        refreshView.getScrollableView().setTranslationY(-offsetY);
        refreshView.onPullingUp(-offsetY);
    }

    public void dealPullDownRelease(){
        if(!refreshView.isPureScrollModeOn() && refreshView.getHeader().getLayoutParams().height >
                refreshView.getHeadHeight() - refreshView.getTouchSlop()){
            animHeadToRefresh();
        }else{
            animHeadBack();
        }

    }

    public void dealPullUpRelease(){
        int height = refreshView.getFooter().getLayoutParams().height;
        float touchHeight = refreshView.getBottomHeight() - refreshView.getTouchSlop();
        if(!refreshView.isPureScrollModeOn() && height >= touchHeight){
            animBottomToLoad();
        }else{
            animBottomBack();
        }
    }

    private boolean isAnimHeadToRefresh = false;
    private boolean isAnimBottomToLoad = false;
    private boolean isAnimHeadBack = false;
    private boolean isAnimBottomBack = false;
    /**
     * 1、满足进入刷新的条件或者主动刷新时，把HEAD位移到刷新位置（当前位置 -- headHeight）
     */
    public void animHeadToRefresh() {
        isAnimHeadToRefresh = true;
        animLayoutByTime(refreshView.getHeader().getLayoutParams().height, refreshView.getHeadHeight(),
                animatorHeadListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimHeadToRefresh = false;
                        refreshView.setRefreshVisible(true);
                        refreshView.onRefresh();
                    }
                });
    }
    /**
     * 2、动画结束或不满足进入刷新状态条件，收起头部（当前位置 - ---0）
     */
    public void animHeadBack() {
        isAnimHeadBack = true;
        animLayoutByTime(refreshView.getHeader().getLayoutParams().height, 0,
                animatorHeadListener, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimHeadBack = false;
                    }
                });
    }

    /**
     * 3、满足进入加载更多的条件或者主动加载更多时，把footer移动到加载更多位置
     */
    public void animBottomToLoad() {
        isAnimBottomToLoad = true;
        animLayoutByTime(refreshView.getFooter().getLayoutParams().height,
                (int)refreshView.getBottomHeight(),
                animatorBottomListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimBottomToLoad = false;

                        refreshView.setLoadingVisible(true);
                        refreshView.onLoadMore();
                    }
                });
    }

    /**
     * 4、加载更多完成或者不满足进入加载更多模式的条件时，收起尾部
     */
    public void animBottomBack() {
        isAnimBottomBack = true;
        animLayoutByTime(refreshView.getFooter().getLayoutParams().height, 0, animatorBottomListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimBottomBack = false;
            }
        });
    }

    private boolean isAnimHeadHide = false;
    private boolean isAnimBottomHide = false;
    private boolean isAnimOsTop = false;
    private boolean isAnimOsBottom = false;

    /**
     * 5、当前刷新处于可见状态，向上滑动屏幕时，隐藏刷新控件
     * @param vy    手指向上滑动速度
     */
    public void animHeadHideByVy(int vy){
        isAnimHeadHide = true;
        refreshView.onRefreshCanceled();
        vy = Math.abs(vy);
        if(vy < 5000) vy = 8000;
        animLayoutByTime(refreshView.getHeader().getLayoutParams().height,
                0, 5 * Math.abs(refreshView.getHeader().getLayoutParams().height * 1000 / vy),
                animatorHeadListener,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimHeadHide = false;
                        refreshView.resetHeaderView();
                    }
                });
    }

    /**
     * 6、加载更多处于刷新状态时，向下滑动屏幕，取消并隐藏加载更多控件
     * @param vy
     */
    public void animBottomHideByVy(int vy){
        isAnimBottomHide = true;
        refreshView.onLoadmoreCanceled();
        vy = Math.abs(vy);
        if(vy < 5000) vy = 8000;
        animLayoutByTime(refreshView.getFooter().getLayoutParams().height, 0, 5 * refreshView.getFooter().getLayoutParams().height * 1000 / vy,
                animatorBottomListener, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimBottomHide = false;
                        refreshView.resetBottomView();
                    }
                });
    }

    /**
     * 执行顶部越界
     * @param vy            满足越界条件的手指滑动速度
     * @param computeTimes  从满足条件到滚动到顶部总共计算次数
     */
    public void animOverScrollTop(float vy,int computeTimes){
        if(refreshView.isOsTopLocked()) return;
        refreshView.lockOsTop();
        isAnimOsTop = true;
        refreshView.setStatePTD();
        int oh = (int)Math.abs(vy / computeTimes / 2);
        final int overHeight = oh > refreshView.getOverScrollHeight() ? refreshView.getOverScrollHeight() : oh;
        final int time = overHeight <= 50 ? 115 : (int)(0.3 * overHeight + 100);
        animLayoutByTime(0, overHeight, time, overScrollTopUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animLayoutByTime(overHeight, 0, 2 * time, overScrollTopUpListener, new android.animation.AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimOsTop = false;
                        refreshView.releaseOsTopLock();
                    }
                });
            }
        });
    }

    /**
     * 8.执行底部越界
     *
     * @param vy           满足越界条件的手指滑动速度
     * @param computeTimes 从满足条件到滚动到顶部总共计算的次数
     */
    public void animOverScrollBottom(float vy, int computeTimes) {
        if (refreshView.isOsBottomLocked()) return;
        refreshView.setStatePBU();
        int oh = (int) Math.abs(vy / computeTimes / 2);
        final int overHeight = oh > refreshView.getOverScrollHeight() ? refreshView.getOverScrollHeight() : oh;
        final int time = overHeight <= 50 ? 115 : (int) (0.3 * overHeight + 100);
        if (refreshView.autoLoadMore()) {
            refreshView.startLoadMore();
        } else {
            refreshView.lockOsBottom();
            isAnimOsBottom = true;
            animLayoutByTime(0, overHeight, time, overScrollBottomUpListener, new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animLayoutByTime(overHeight, 0, 2 * time, overScrollBottomUpListener, new android.animation.AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimOsBottom = false;
                            refreshView.releaseOsBottomLock();
                        }
                    });
                }
            });
        }
    }

    private void translateExHead(int offsetY){
        if(!refreshView.isExHeadLocked())
            refreshView.getExHeader().setTranslationY(offsetY);
    }

    private ValueAnimator.AnimatorUpdateListener animatorHeadListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            refreshView.getHeader().getLayoutParams().height = height;
            refreshView.getHeader().requestLayout();
            if(!refreshView.isFloatRefresh()){
                refreshView.getScrollableView().setTranslationY(height);
                translateExHead(height);
            }
            refreshView.onPullDownReleaseing(height);
        }
    };

    private ValueAnimator.AnimatorUpdateListener animatorBottomListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            refreshView.getFooter().getLayoutParams().height = height;
            refreshView.getFooter().requestLayout();
            refreshView.getScrollableView().setTranslationY(-height);
            refreshView.onPullUpReleaseing(height);
        }
    };

    private ValueAnimator.AnimatorUpdateListener overScrollTopUpListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if(refreshView.isOverScrollTopShow()){
                refreshView.getHeader().getLayoutParams().height = height;
                refreshView.getHeader().requestLayout();
            }else{
                refreshView.getHeader().setVisibility(View.GONE);
            }
            if(!refreshView.isFloatRefresh()) {
                refreshView.getScrollableView().setTranslationY(height);
                translateExHead(height);
            }
            refreshView.onPullDownReleaseing(height);
        }
    };

    private ValueAnimator.AnimatorUpdateListener overScrollBottomUpListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (refreshView.isOverScrollBottomShow()) {
                refreshView.getFooter().getLayoutParams().height = height;
                refreshView.getFooter().requestLayout();
            } else {
                refreshView.getFooter().setVisibility(GONE);
            }
            refreshView.getScrollableView().setTranslationY(-height);
            refreshView.onPullUpReleaseing(height);
        }
    };

    public void animLayoutByTime(int start, int end, long time,
                                 ValueAnimator.AnimatorUpdateListener listener,
                                 Animator.AnimatorListener animatorListener){
        ValueAnimator va = ValueAnimator.ofInt(start,end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(listener);
        va.addListener(animatorListener);
        va.setDuration(time);
        va.start();
    }

    public void animLayoutByTime(int start, int end,long time, ValueAnimator.AnimatorUpdateListener listener){
        ValueAnimator va = ValueAnimator.ofInt(start,end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(listener);
        va.setDuration(time);
        va.start();
    }
    public void animLayoutByTime(int start, int end, ValueAnimator.AnimatorUpdateListener listener, Animator.AnimatorListener animatorListener){
        ValueAnimator va = ValueAnimator.ofInt(start,end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(listener);
        va.addListener(animatorListener);
        va.setDuration((int)Math.abs(start - end * animFraction));
        va.start();
    }
}
