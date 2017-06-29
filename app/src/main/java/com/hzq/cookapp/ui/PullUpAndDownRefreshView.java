package com.hzq.cookapp.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hzq.cookapp.R;
import com.hzq.cookapp.ui.footer.BottomProgressView;
import com.hzq.cookapp.ui.footer.IBottomView;
import com.hzq.cookapp.ui.header.GoogleDotView;
import com.hzq.cookapp.ui.header.IHeaderView;
import com.hzq.cookapp.ui.listener.OnAnimEndListener;
import com.hzq.cookapp.ui.listener.PullListener;
import com.hzq.cookapp.ui.listener.UpAndDownPullAdapter;
import com.hzq.cookapp.utils.DensityUtil;

/**
 * @author: hezhiqiang
 * @date: 2017/6/27
 * @version:
 * @description: 下拉刷新 和 上拉加载更多控件，支持自定义header与footer要显示的View
 */

public class PullUpAndDownRefreshView extends RelativeLayout {

    private final static int PULLING_TOP_DOWN = 0;
    private final static int PULLING_BOTTOM_UP = 1;
    private int state = -1;

    //波浪的高度,最大扩展高度
    protected float mWaveHeight;
    //头部的高度
    protected float mHeadHeight;
    //底部高度
    private float mBottomHeight;
    //是否需要下拉刷新，默认需要
    private boolean enableRefresh = true;
    //是否需要加载更多,默认需要
    private boolean enableLoadmore = true;
    //是否在越界回弹的时候显示下拉图标
    private boolean isOverScrollTopShow = true;
    //是否在越界回弹的时候显示上拉图标
    private boolean isOverScrollBottomShow = true;
    //允许的越界回弹的高度
    private float mOverScrollHeight;
    //是否隐藏刷新控件,开启越界回弹模式(开启之后刷新控件将隐藏)
    private boolean isPureScrollModeOn = false;
    //是否允许进入越界回弹模式
    private boolean enableOverScroll = true;
    //是否刷新视图可见
    private boolean isRefreshVisible = false;
    //是否加载更多视图可见
    private boolean isLoadingVisible = false;
    //是否开启悬浮刷新模式
    private boolean floatRefresh = false;
    //在添加附件header前锁住，阻止一些额外的位移动画
    private boolean isExHeadLocked = true;
    //是否自动加载更多(滑到底部时自动加载下一页)
    private boolean autoLoadMore = true;

    //头部layout
    private FrameLayout mHeadLayout;
    //整个头部
    private FrameLayout mExtraHeadLayout;

    //底部Layout
    private FrameLayout mBottomLayout;

    private IHeaderView mHeaderView;
    private IBottomView mBottomView;

    //子控件
    private View mChildView;

    private UpAndDownPullAdapter upAndDownPullAdapter;

    public void setUpAndDownPullAdapter(UpAndDownPullAdapter upAndDownPullAdapter) {
        this.upAndDownPullAdapter = upAndDownPullAdapter;
    }

    private PullListener pullListener;

    public void setPullListener(PullListener pullListener) {
        this.pullListener = pullListener;
    }

    private RefreshProcessor refreshProcessor;
    private AnimProcessor animProcessor;
    private OverScrollProcessor overScrollProcessor;

    public PullUpAndDownRefreshView(Context context) {
        this(context,null);
    }

    public PullUpAndDownRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullUpAndDownRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化资源
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullUpAndDownRefreshView,defStyleAttr,0);
        mWaveHeight = a.getDimensionPixelSize(R.styleable.PullUpAndDownRefreshView_tr_wave_height, (int) DensityUtil.dp2px(context, 120));
        mHeadHeight = a.getDimensionPixelSize(R.styleable.PullUpAndDownRefreshView_tr_head_height, (int) DensityUtil.dp2px(context, 80));
        mBottomHeight = a.getDimensionPixelSize(R.styleable.PullUpAndDownRefreshView_tr_bottom_height, (int) DensityUtil.dp2px(context, 60));
        mOverScrollHeight = a.getDimensionPixelSize(R.styleable.PullUpAndDownRefreshView_tr_overscroll_height, (int) mHeadHeight);
        enableLoadmore = a.getBoolean(R.styleable.PullUpAndDownRefreshView_tr_enable_loadmore, true);
        isPureScrollModeOn = a.getBoolean(R.styleable.PullUpAndDownRefreshView_tr_pureScrollMode_on, false);
        isOverScrollTopShow = a.getBoolean(R.styleable.PullUpAndDownRefreshView_tr_overscroll_top_show, true);
        isOverScrollBottomShow = a.getBoolean(R.styleable.PullUpAndDownRefreshView_tr_overscroll_bottom_show, true);
        enableOverScroll = a.getBoolean(R.styleable.PullUpAndDownRefreshView_tr_enable_overscroll, true);
        a.recycle();

        init();
    }

    private void init() {
        if(isInEditMode()) return;
        setPullListener(new UpAndDownPullListener());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(mHeadLayout == null){
            FrameLayout headViewLayout = new FrameLayout(getContext());
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
            lp.addRule(ALIGN_PARENT_TOP);
            lp.addRule(CENTER_VERTICAL);

            FrameLayout extraHeadLayout = new FrameLayout(getContext());
            extraHeadLayout.setId(R.id.ex_header);
            LayoutParams lp1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.addView(extraHeadLayout,lp1);
            this.addView(headViewLayout,lp);

            mExtraHeadLayout = extraHeadLayout;
            mHeadLayout = headViewLayout;

            if(mHeaderView == null)
                setHeaderView(new GoogleDotView(getContext()));
        }

        if(mBottomLayout == null){
            FrameLayout bottomLayout = new FrameLayout(getContext());
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
            lp.addRule(ALIGN_PARENT_BOTTOM);
            lp.addRule(CENTER_VERTICAL);
            bottomLayout.setLayoutParams(lp);

            mBottomLayout = bottomLayout;
            this.addView(mBottomLayout);

            if(mBottomView == null){
                //设置底部view
                BottomProgressView bottomProgressView = new BottomProgressView(getContext());
                setBottomView(bottomProgressView);
            }
        }

        mChildView = getChildAt(0);

        if(mChildView == null)
            throw new NullPointerException("子必须包含一个子View（RecyclerView or ListView等等）");

        refreshProcessor = new RefreshProcessor(this);
        animProcessor = new AnimProcessor(this);
        overScrollProcessor = new OverScrollProcessor(this);
        overScrollProcessor.init();
    }

    public void setHeaderView(final IHeaderView headerView){
        if(headerView != null){
            post(new Runnable() {
                @Override
                public void run() {
                    mHeadLayout.removeAllViews();
                    mHeadLayout.addView(headerView.getView());
                }
            });
            mHeaderView = headerView;
        }
    }

    public void setBottomView(final IBottomView bottomView){
        if(bottomView != null){
            post(new Runnable() {
                @Override
                public void run() {
                    mBottomLayout.removeAllViews();
                    mBottomLayout.addView(bottomView.getView());
                }
            });
            mBottomView = bottomView;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = refreshProcessor.interceptTouchEvent(event);
        return  intercept || super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return refreshProcessor.consumeTouchEvent(event) || super.onTouchEvent(event);
    }

    public View getScrollableView(){
        return mChildView;
    }

    /**===================animProcessor start=============================**/
    public void scrollHeadByMove(float moveY){
        animProcessor.scrollHeadByMove(moveY);
    }

    public void scrollBottomByMove(float moveY){
        animProcessor.scrollBottomByMove(moveY);
    }

    public void dealPullDownRelease(){
        animProcessor.dealPullDownRelease();
    }

    public void dealPullUpRelease(){
        animProcessor.dealPullUpRelease();
    }

    public AnimProcessor getAnimProcessor(){
        return animProcessor;
    }
    /**===================animProcessor end=============================**/


    /**===================open api=============================**/
    public void setOverScrollTopShow(boolean overScrollTopShow) {
        isOverScrollTopShow = overScrollTopShow;
    }

    public void setOverScrollBottomShow(boolean overScrollBottomShow) {
        isOverScrollBottomShow = overScrollBottomShow;
    }

    public boolean isRefreshVisible() {
        return isRefreshVisible;
    }

    public boolean isLoadingVisible() {
        return isLoadingVisible;
    }

    public int getHeadHeight(){
        return (int)mHeadHeight;
    }

    public float getMaxHeadHeight() {
        return mWaveHeight;
    }

    public void setWaveHeight(float mWaveHeight) {
        this.mWaveHeight = mWaveHeight;
    }

    public void setHeadHeight(float mHeadHeight) {
        this.mHeadHeight = mHeadHeight;
    }

    public float getBottomHeight() {
        return mBottomHeight;
    }

    public void setBottomHeight(float mBottomHeight) {
        this.mBottomHeight = mBottomHeight;
    }

    public boolean isExHeadLocked(){
        return isExHeadLocked;
    }

    public boolean isPureScrollModeOn() {
        return isPureScrollModeOn;
    }

    public boolean isEnableOverScroll() {
        return enableOverScroll;
    }

    /**
     * 是否允许越界回弹
     * @param enableOverScroll
     */
    public void setEnableOverScroll(boolean enableOverScroll) {
        this.enableOverScroll = enableOverScroll;
    }

    /**
     * 是否开启纯净的越界回弹模式，开启时刷新和加载更多控件都不显示
     * @param pureScrollModeOn
     */
    public void setPureScrollModeOn(boolean pureScrollModeOn) {
        isPureScrollModeOn = pureScrollModeOn;
        if(pureScrollModeOn){
            isOverScrollTopShow = false;
            isOverScrollBottomShow = false;
            setWaveHeight(mOverScrollHeight);
            setHeadHeight(mOverScrollHeight);
            setBottomHeight(mOverScrollHeight);
        }
    }

    public boolean allowOverScroll(){
        return (!isRefreshVisible && !isLoadingVisible);
    }

    public void setRefreshVisible(boolean refreshVisible) {
        isRefreshVisible = refreshVisible;
    }

    /**
     * 设置越界高度
     * @param mOverScrollHeight
     */
    public void setmOverScrollHeight(float mOverScrollHeight) {
        this.mOverScrollHeight = mOverScrollHeight;
    }

    public int getOverScrollHeight() {
        return (int)mOverScrollHeight;
    }

    /**
     * 在越界时阻止再次进入这个状态而导致动画闪烁。  Prevent entering the overscroll-mode again on animating.
     */
    private boolean isOverScrollTopLocked = false;

    public void lockOsTop() {
        isOverScrollTopLocked = true;
    }

    public void releaseOsTopLock() {
        isOverScrollTopLocked = false;
    }

    public boolean isOsTopLocked() {
        return isOverScrollTopLocked;
    }

    private boolean isOverScrollBottomLocked = false;

    public void lockOsBottom() {
        isOverScrollBottomLocked = true;
    }

    public void releaseOsBottomLock() {
        isOverScrollBottomLocked = false;
    }

    public boolean isOsBottomLocked() {
        return isOverScrollBottomLocked;
    }

    public void setLoadingVisible(boolean loadingVisible) {
        isLoadingVisible = loadingVisible;
    }

    public boolean isOverScrollTopShow(){
        return isOverScrollTopShow;
    }

    public boolean isOverScrollBottomShow() {
        return isOverScrollBottomShow;
    }

    public boolean autoLoadMore(){
        return autoLoadMore;
    }

    /**
     * 设置是否滑到底部时自动加载更多
     * @param autoLoadMore 为true表示底部越界时主动进入加载更多模式，否则直接弹回
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    public View getHeader() {
        return mHeadLayout;
    }

    public View getExHeader(){
        return mExtraHeadLayout;
    }

    public View getFooter(){
        return mBottomLayout;
    }

    public int getTouchSlop(){
        return ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void resetHeaderView(){
        if(mHeaderView != null)
            mHeaderView.reset();
    }

    public void resetBottomView(){
        if(mBottomView != null)
            mBottomView.reset();
    }

    /**
     * ==============================refreshPullListener=======================================
     */
    public void onPullingDown(float offsetY){
        pullListener.onPullingDown(this,offsetY / mHeadHeight);
    }

    public void onPullingUp(float offsetY){
        pullListener.onPullingUp(this,offsetY / mBottomHeight);
    }

    public void onRefresh(){
        pullListener.onRefreshing(this);
    }

    public void onLoadMore(){
        pullListener.onLoadingMore(this);
    }

    public void onPullDownReleaseing(float offsetY){
        pullListener.onPullDownReleasing(this,offsetY / mHeadHeight);
    }

    public void onPullUpReleaseing(float offsetY){
        pullListener.onPullUpReleasing(this,offsetY / mBottomHeight);
    }

    /**
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    public void onRefreshCanceled(){
        pullListener.onRefreshCanceled();
    }

    /**
     * 正在加载更多时向下滑动屏幕，加载更多被取消
     */
    public void onLoadmoreCanceled(){
        pullListener.onLoadmoreCanceled();
    }

    /**
     * 开启刷新
     */
    public void startRefresh(){
        post(new Runnable() {
            @Override
            public void run() {
                setStatePTD();
                if(!isPureScrollModeOn() && mChildView != null){
                    setRefreshVisible(true);
                    animProcessor.animHeadToRefresh();
                }
            }
        });
    }

    /**
     * 手动调用finishRefresh或者finishLoadmore之后调用
     */
    public void onFinishRefreshed(){
        pullListener.onFinishRefreshed();
    }

    public void onFinishLoadMore(){
        pullListener.onFinishLoadMore();
    }

    /**
     * 开启加载更多
     */
    public void startLoadMore(){
        post(new Runnable() {
            @Override
            public void run() {
                setStatePBU();
                if(!isPureScrollModeOn() && mChildView != null){
                    setLoadingVisible(true);
                    animProcessor.animBottomToLoad();
                }
            }
        });
    }

    public boolean allowPullDown(){
        return enableRefresh;
    }

    public boolean allowPullUp(){
        return enableLoadmore;
    }

    public void setEnableLoadmore(boolean enableLoadmore) {
        this.enableLoadmore = enableLoadmore;
    }

    public boolean isFloatRefresh() {
        return floatRefresh;
    }

    /**
     * 是否开启悬浮模式
     * @param floatRefresh
     */
    public void setFloatRefresh(boolean floatRefresh) {
        this.floatRefresh = floatRefresh;
    }

    /**
     * =========================state start=============================
     */

    public boolean isStatePTD(){
        return PULLING_TOP_DOWN == state;
    }

    public boolean isStatePBU(){
        return state == PULLING_BOTTOM_UP;
    }

    public void setStatePTD(){
        state = PULLING_TOP_DOWN;
    }

    public void setStatePBU(){
        state = PULLING_BOTTOM_UP;
    }

    /**===========================state end=============================*/

    private class UpAndDownPullListener implements PullListener{

        @Override
        public void onPullingDown(PullUpAndDownRefreshView refreshLayout, float fraction) {
            mHeaderView.onPullingDown(fraction,mWaveHeight,mHeadHeight);
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onPullingDown(refreshLayout,fraction);
        }

        @Override
        public void onPullingUp(PullUpAndDownRefreshView refreshLayout, float fraction) {
            mBottomView.onPullingUp(fraction,mWaveHeight,mHeadHeight);
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onPullingUp(refreshLayout,fraction);
        }

        @Override
        public void onPullDownReleasing(PullUpAndDownRefreshView refreshLayout, float fraction) {
            mHeaderView.onPullReleasing(fraction,mWaveHeight,mHeadHeight);
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onPullDownReleasing(refreshLayout,fraction);
        }

        @Override
        public void onPullUpReleasing(PullUpAndDownRefreshView refreshLayout, float fraction) {
            mBottomView.onPullReleasing(fraction,mWaveHeight,mHeadHeight);
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onPullUpReleasing(refreshLayout,fraction);
        }

        @Override
        public void onRefreshing(PullUpAndDownRefreshView refreshLayout) {
            mHeaderView.startAnim(mWaveHeight,mHeadHeight);
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onRefreshing(refreshLayout);
        }

        @Override
        public void onLoadingMore(PullUpAndDownRefreshView refreshLayout) {
            mBottomView.startAnim(mWaveHeight,mHeadHeight);
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onLoadingMore(refreshLayout);
        }

        @Override
        public void onFinishRefreshed() {
            if(!isRefreshVisible) return;
            mHeaderView.onFinish(new OnAnimEndListener() {
                @Override
                public void onAnimEnd() {
                    if(isRefreshVisible && mChildView != null){
                        setRefreshVisible(false);
                        animProcessor.animHeadBack();
                    }
                }
            });
        }

        @Override
        public void onFinishLoadMore() {
            if(!isLoadingVisible) return;
            ScrollingUtil.scrollAViewBy(mChildView,(int)mBottomHeight);
            setLoadingVisible(false);
            animProcessor.animBottomBack();
            mBottomView.onFinish();

        }

        @Override
        public void onRefreshCanceled() {
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onRefreshCanceled();
        }

        @Override
        public void onLoadmoreCanceled() {
            if(upAndDownPullAdapter != null)
                upAndDownPullAdapter.onLoadmoreCanceled();
        }
    }

}
