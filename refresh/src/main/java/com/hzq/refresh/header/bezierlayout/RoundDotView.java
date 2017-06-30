package com.hzq.refresh.header.bezierlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: hezhiqiang
 * @date: 2017/6/29
 * @version:
 * @description:
 */

public class RoundDotView extends View {

    private Paint paint;
    private float r = 15;
    private int num = 7;
    private int color;

    private int cir_x;

    public void setCir_x(int cir_x) {
        this.cir_x = cir_x;
    }

    public RoundDotView(Context context) {
        this(context,null);
    }

    public RoundDotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(114,114,114));
    }

    public void setColor(@ColorInt int color){
        this.color = color;
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth() / num - 10;
        for (int i = 0; i < num; i++) {
            switch (i){
                case 0:
                    paint.setAlpha(35);
                    canvas.drawCircle(getMeasuredWidth() / 2 - cir_x * 3 - 3 * w / 3 * 2,getMeasuredHeight() / 2,r,paint);
                    break;
                case 1:
                    paint.setAlpha(105);
                    canvas.drawCircle(getMeasuredWidth() / 2 - cir_x * 2 - 2 * w / 3 * 2,getMeasuredHeight() / 2,r,paint);
                    break;
                case 2:
                    paint.setAlpha(145);
                    canvas.drawCircle(getMeasuredWidth() / 2 - cir_x * 1 - w / 3 * 2,getMeasuredHeight() / 2,r,paint);
                    break;
                case 3:
                    paint.setAlpha(255);
                    canvas.drawCircle(getMeasuredWidth() / 2,getMeasuredHeight() / 2,r,paint);
                    break;
                case 4:
                    paint.setAlpha(145);
                    canvas.drawCircle(getMeasuredWidth() / 2 + cir_x * 1 + w / 3 * 2,getMeasuredHeight() / 2,r,paint);
                    break;
                case 5:
                    paint.setAlpha(105);
                    canvas.drawCircle(getMeasuredWidth() / 2 + cir_x * 2 + 2 * w / 3 * 2,getMeasuredHeight() / 2,r,paint);
                    break;
                case 6:
                    paint.setAlpha(35);
                    canvas.drawCircle(getMeasuredWidth() / 2 + cir_x * 3 + 3 * w / 3 * 2,getMeasuredHeight() / 2,r,paint);
                    break;
            }
        }
    }
}
