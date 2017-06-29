package com.hzq.cookapp.ui.header.bezierlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author: hezhiqiang
 * @date: 2017/6/29
 * @version:
 * @description:
 */

public class RoundProgressView extends View {
    private Paint mPath;
    private Paint mPantR;
    private float r = 40;
    protected int num = 7;
    private int startAngle = 270;
    private int endAngle = 0;
    private int outCir_value = 15;
    private int color;
    private int cir_x;

    public void setCir_x(int cir_x) {
        this.cir_x = cir_x;
    }

    private ValueAnimator va;

    public RoundProgressView(Context context) {
        this(context,null);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        color = Color.WHITE;
        mPath = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPantR = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPantR.setColor(color);
        mPath.setColor(Color.rgb(114,114,114));

        va = ValueAnimator.ofInt(0,360);
        va.setDuration(720);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                endAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth() / num - 10;
        mPath.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getMeasuredWidth() / 2,getMeasuredHeight() / 2,r,mPath);
        canvas.save();

        mPath.setStyle(Paint.Style.STROKE); //设置空心
        mPath.setStrokeWidth(6);
        canvas.drawCircle(getMeasuredWidth() / 2,getMeasuredHeight() / 2,r + 15,mPath);
        canvas.restore();

        mPantR.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(getMeasuredWidth() / 2 -r,getMeasuredHeight() / 2 -r,
                                    getMeasuredWidth() / 2 + r,getMeasuredHeight() / 2 + r);
        canvas.drawArc(oval,startAngle,endAngle,true,mPantR);
        canvas.save();

        mPantR.setStyle(Paint.Style.STROKE);
        mPantR.setStrokeWidth(6);
        RectF oval2 = new RectF(getMeasuredWidth() / 2 -r -outCir_value,getMeasuredHeight() / 2 -r-outCir_value,
                getMeasuredWidth() / 2 + r + outCir_value,getMeasuredHeight() / 2 + r + outCir_value);
        canvas.drawArc(oval2,startAngle,endAngle,false,mPantR);
        canvas.restore();

    }

    public void startAnim(){
        if(va != null) va.start();
    }

    public void stopAnim(){
        if(va != null && va.isRunning()) va.cancel();
    }

    public void setColor(@ColorInt int color){
        this.color = color;
        mPantR.setColor(color);
    }
}
