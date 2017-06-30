package com.hzq.refresh.footer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.hzq.refresh.R;

/**
 * @author: hezhiqiang
 * @date: 2017/6/28
 * @version:
 * @description:
 */

public class BottomProgressView extends ProgressView implements IBottomView{

    private int normalColor = 0xffeeeeee;
    private int animatingColor = 0xffe75946;

    public BottomProgressView(Context context) {
        this(context,null);
    }

    public BottomProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        setLayoutParams(params);
        setIndicatorColor(getResources().getColor(R.color.Orange));
//        setIndicatorId(BallPulse);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {
        setIndicatorColor(normalColor);
        stopAnim();
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        setIndicatorColor(getResources().getColor(R.color.Orange));
        startAnim();
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
//        stopAnim();
    }

    @Override
    public void onFinish() {
        stopAnim();
    }

    @Override
    public void reset() {
        stopAnim();
    }
}
