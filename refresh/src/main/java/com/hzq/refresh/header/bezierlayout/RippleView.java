package com.hzq.refresh.header.bezierlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hzq.refresh.listener.AnimatorListenerAdapter;

/**
 * @author: hezhiqiang
 * @date: 2017/6/29
 * @version:
 * @description: 水波纹效果
 */

public class RippleView extends View {

    private Paint paint;
    private int r;

    private ValueAnimator va;

    OnRippleEndListener listener;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public RippleView(Context context) {
        this(context,null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setRippleColor(@ColorInt int color){
        if(paint != null)
            paint.setColor(color);
    }

    public void startReveal(){
        setVisibility(VISIBLE);
        if(va == null){
            int bigRadius = (int)(Math.sqrt(Math.pow(getHeight(),2) + Math.pow(getWidth(),2)));
            va = ValueAnimator.ofInt(0,bigRadius / 2);
            va.setDuration(bigRadius);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    r = (int)animation.getAnimatedValue() * 2;
                    invalidate();
                }
            });
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(listener != null)
                        listener.onRippleEnd();
                }
            });
        }
        va.start();
    }

    public void stopAnim(){
        if(va != null && va.isRunning())
            va.cancel();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2,getHeight() / 2,r,paint);
    }


    public void setListener(OnRippleEndListener listener) {
        this.listener = listener;
    }

    public interface OnRippleEndListener{
        void onRippleEnd();
    }
}
