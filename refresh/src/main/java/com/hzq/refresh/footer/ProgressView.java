package com.hzq.refresh.footer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hzq.refresh.R;

/**
 * @author: hezhiqiang
 * @date: 2017/6/28
 * @version:
 * @description:
 */

public class ProgressView extends View {
    public static final float SCALE=1.0f;

    //scale x ,y
    private float[] scaleFloats=new float[]{SCALE,
            SCALE,
            SCALE};
    private Paint mPaint;
    private int color;

    public static final int DEFAULT_SIZE = 50;
    private boolean mHasAnimation;
    int mIndicatorColor;

    private BaseIndicatorController baseIndicatorController;

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.Blue));
        mPaint.setStyle(Paint.Style.FILL);

        baseIndicatorController = new BallPulseIndicator();
        baseIndicatorController.setTarget(this);
    }

    public void setIndicatorColor(int color){
        mIndicatorColor = color;
        mPaint.setColor(mIndicatorColor);
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        baseIndicatorController.draw(canvas,mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation){
            mHasAnimation=true;
            baseIndicatorController.initAnimation();
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                baseIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                baseIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }
    }

    public void stopAnim(){
        baseIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
    }
    public void startAnim(){
        baseIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        baseIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        baseIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = dp2px(DEFAULT_SIZE);
        int h = dp2px(DEFAULT_SIZE);
        setMeasuredDimension(w,h);
    }

    private int dp2px(int dpValue){
        return (int)getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
