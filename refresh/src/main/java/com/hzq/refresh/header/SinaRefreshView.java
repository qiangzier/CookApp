package com.hzq.refresh.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzq.refresh.R;
import com.hzq.refresh.listener.OnAnimEndListener;

/**
 * @author: hezhiqiang
 * @date: 2017/6/29
 * @version:
 * @description:    简单的刷新动画
 */

public class SinaRefreshView extends FrameLayout implements IHeaderView {

    private ImageView refreshArrow;
    private ImageView loadingView;
    private TextView refreshTextView;

    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String refreshingStr = "正在刷新";

    public SinaRefreshView(@NonNull Context context) {
        this(context,null);
    }

    public SinaRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SinaRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.iv_loading_layout,null);
        refreshArrow = (ImageView) rootView.findViewById(R.id.arrow);
        refreshTextView = (TextView) rootView.findViewById(R.id.tv);
        loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
        addView(rootView);
    }

    public void setArrowResource(@DrawableRes int resId){
        if(refreshArrow != null)
            refreshArrow.setImageResource(resId);
    }

    public void setTextColor(@ColorInt int color){
        if(refreshTextView != null)
            refreshTextView.setTextColor(color);
    }

    public void setPullDownStr(String pullDownStr) {
        this.pullDownStr = pullDownStr;
    }

    public void setReleaseRefreshStr(String releaseRefreshStr) {
        this.releaseRefreshStr = releaseRefreshStr;
    }

    public void setRefreshingStr(String refreshingStr) {
        this.refreshingStr = refreshingStr;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if(fraction < 1f) refreshTextView.setText(pullDownStr);
        if(fraction > 1f) refreshTextView.setText(releaseRefreshStr);
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if(fraction < 1f){
            refreshTextView.setText(pullDownStr);
            refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if(refreshArrow.getVisibility() == INVISIBLE){
                refreshArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText(refreshingStr);
        refreshArrow.setVisibility(INVISIBLE);
        loadingView.setVisibility(VISIBLE);
        ((AnimationDrawable)loadingView.getDrawable()).start();
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        refreshArrow.setVisibility(VISIBLE);
        loadingView.setVisibility(INVISIBLE);
        refreshTextView.setText(pullDownStr);
    }
}
