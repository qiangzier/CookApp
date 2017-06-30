package com.hzq.refresh.header.bezierlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: hezhiqiang
 * @date: 2017/6/29
 * @version:
 * @description:    绘制贝塞尔曲线
 */

public class WaveView extends View {
    private int waveHeight;
    private int headHeight;

    private Path path;
    private Paint paint;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff1f2426);
    }

    public int getHeadHeight() {
        return headHeight;
    }

    public void setHeadHeight(int headHeight) {
        this.headHeight = headHeight;
    }

    public int getWaveHeight() {
        return waveHeight;
    }

    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    public void setWaveColor(@ColorInt int color){
        if(paint != null)
            paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //重置画笔
        path.reset();
        path.lineTo(0,headHeight);
        //绘制贝塞尔曲线
        path.quadTo(getMeasuredWidth() / 2,headHeight + waveHeight,getMeasuredWidth(),headHeight);
        path.lineTo(getMeasuredWidth(),0);
        canvas.drawPath(path,paint);
    }

    static Bitmap drawableToBitmap(Drawable drawable){
        int width = drawable.getIntrinsicWidth();//取drawable得长款
        int height = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565; //获取drawable的颜色绘制格式
        Bitmap bitmap = Bitmap.createBitmap(width,height,config);//建立对应的bitmap
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,width,height);
        drawable.draw(canvas); //把drawable内容画到画布中
        return bitmap;
    }
}
