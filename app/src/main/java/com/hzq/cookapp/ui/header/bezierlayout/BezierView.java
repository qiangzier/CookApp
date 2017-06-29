package com.hzq.cookapp.ui.header.bezierlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.hzq.cookapp.R;
import com.hzq.cookapp.ui.header.IHeaderView;
import com.hzq.cookapp.ui.listener.AnimatorListenerAdapter;
import com.hzq.cookapp.ui.listener.OnAnimEndListener;

/**
 * @author: hezhiqiang
 * @date: 2017/6/29
 * @version:
 * @description:
 */

public class BezierView extends FrameLayout implements IHeaderView{

    public BezierView(@NonNull Context context) {
        this(context,null);
    }

    public BezierView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private View headView;
    private WaveView waveView;
    private RippleView rippleView;
    private RoundDotView r1;
    private RoundProgressView r2;

    private void init() {
        headView = LayoutInflater.from(getContext()).inflate(R.layout.view_bezier,null);
        waveView = (WaveView) headView.findViewById(R.id.waveView);
        rippleView = (RippleView) headView.findViewById(R.id.rippleView);
        r1 = (RoundDotView) headView.findViewById(R.id.r1);
        r2 = (RoundProgressView) headView.findViewById(R.id.r2);

        addView(headView);
    }

    public void setWaveColor(@ColorInt int color){
        waveView.setWaveColor(color);
    }

    public void setRippleColor(@ColorInt int color){
        rippleView.setRippleColor(color);
    }

    public void setRountProgressColor(@ColorInt int color){
        r2.setColor(color);
    }

    /**
     * 限定值
     * @param a
     * @param b
     * @return
     */
    public float limitValue(float a,float b){
        float value = 0;
        final float min = Math.min(a,b);
        final float max = Math.max(a,b);
        value = value > min ? value : min;
        value = value < max ? value : max;
        return value;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if(rippleView.getVisibility() == VISIBLE) rippleView.setVisibility(GONE);
        waveView.setHeadHeight((int) (headHeight * limitValue(1,fraction)));
        waveView.setWaveHeight((int) (maxHeadHeight * Math.max(0,fraction - 1)));
        waveView.invalidate();

        /**处理圈圈**/
        r1.setCir_x((int) (30 * limitValue(1,fraction)));
        r1.setVisibility(VISIBLE);
        r1.invalidate();

        r2.setVisibility(GONE);
        r2.animate().scaleX((float) 0.1);
        r2.animate().scaleY((float) 0.1);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        waveView.setHeadHeight((int) (headHeight * limitValue(1,fraction)));
        waveView.setWaveHeight((int) (maxHeadHeight * Math.max(0,fraction - 1)));
        waveView.invalidate();

        r1.setCir_x((int) (30 * limitValue(1,fraction)));
        r1.invalidate();
    }

    private ValueAnimator waveAnimator,circleAnimator;

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        waveView.setHeadHeight((int) headHeight);
        waveAnimator = ValueAnimator.ofInt(waveView.getWaveHeight(),0,-300,0,-100,0);
        waveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                waveView.setWaveHeight((int)animation.getAnimatedValue() / 2);
                waveView.invalidate();
            }
        });
        waveAnimator.setInterpolator(new DecelerateInterpolator());
        waveAnimator.setDuration(800);
        waveAnimator.start();


        circleAnimator = ValueAnimator.ofFloat(1,0);
        circleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                r1.setVisibility(GONE);
                r2.setVisibility(VISIBLE);
                r2.animate().scaleX((float) 1.0);
                r2.animate().scaleY((float) 1.0);
                r2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        r2.startAnim();
                    }
                },200);
            }
        });

        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                r1.setCir_x((int) (-value * 40));
                r1.invalidate();
            }
        });

        circleAnimator.setInterpolator(new DecelerateInterpolator());
        circleAnimator.setDuration(300);
        circleAnimator.start();

    }

    @Override
    public void onFinish(final OnAnimEndListener animEndListener) {
        r2.stopAnim();
        r2.animate().scaleX((float) 0.0);
        r2.animate().scaleY((float) 0.0);
        rippleView.setListener(new RippleView.OnRippleEndListener() {
            @Override
            public void onRippleEnd() {
                animEndListener.onAnimEnd();
            }
        });
        rippleView.startReveal();
    }

    @Override
    public void reset() {
        if(waveAnimator != null && waveAnimator.isRunning()) waveAnimator.cancel();
        waveView.setWaveHeight(0);
        if(circleAnimator != null && circleAnimator.isRunning()) circleAnimator.cancel();
        r1.setVisibility(VISIBLE);
        r2.stopAnim();
        r2.setScaleX(0);
        r2.setScaleY(0);
        r2.setVisibility(GONE);
        rippleView.stopAnim();
        rippleView.setVisibility(GONE);
    }
}
