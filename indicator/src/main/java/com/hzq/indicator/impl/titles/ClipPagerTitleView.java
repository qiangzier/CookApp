package com.hzq.indicator.impl.titles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.hzq.indicator.IndicatorUtils;
import com.hzq.indicator.impl.IMeasureablePagerTitleView;

/**
 * @author: hezhiqiang
 * @date: 2017/7/4
 * @version:
 * @description: title过度时颜色一起过度变化
 */

public class ClipPagerTitleView extends View implements IMeasureablePagerTitleView {
    private String mText;
    private int mTextColor;
    private int mClipColor;
    private boolean mLeftToRight;
    private float mClipPercent;

    private Paint mPaint;
    private Rect mTextBounds = new Rect();

    public ClipPagerTitleView(Context context) {
        super(context);
        int textSize = IndicatorUtils.dp2px(context,16);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        int padding = IndicatorUtils.dp2px(context,10);
        setPadding(padding,0,padding,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureTextBounds();
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    private void measureTextBounds(){
        mPaint.getTextBounds(mText,0,mText == null ? 0 : mText.length(),mTextBounds);
    }

    private int measureWidth(int widthMeasureSpec){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = size;
        switch (mode){
            case MeasureSpec.AT_MOST:
                int width = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                result = Math.min(width,size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec){
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = size;
        switch (mode){
            case MeasureSpec.AT_MOST:
                int height = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                result = Math.min(height,size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (getWidth() - mTextBounds.width()) / 2;
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        int y = (int) ((getHeight() - metrics.bottom - metrics.top) / 2);
        //画底层
        mPaint.setColor(mTextColor);
        canvas.drawText(mText,x,y,mPaint);

        //画Clip层
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        if(mLeftToRight){
            canvas.clipRect(0,0,getWidth() * mClipPercent,getHeight());
        }else{
            canvas.clipRect(getWidth() * (1 - mClipPercent),0,getWidth(),getHeight());
        }
        mPaint.setColor(mClipColor);
        canvas.drawText(mText,x,y,mPaint);
        canvas.restore();
    }

    public void setClipColor(int mClipColor) {
        this.mClipColor = mClipColor;
        invalidate();
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public void setTextSize(float textSize){
        mPaint.setTextSize(textSize);
        requestLayout();
    }

    @Override
    public void setText(String mText) {
        this.mText = mText;
        requestLayout();
    }

    @Override
    public int getContentLeft() {
        int contentWidth = mTextBounds.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //获取文本内容的高度
        float contentHeight = fontMetrics.bottom - fontMetrics.top;
        //总高度/2 - 内容高度/2 = 内容top
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        int contentWidth = mTextBounds.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float contentHeight = fontMetrics.bottom - fontMetrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    @Override
    public void onSelected(int index, int totalCount) {

    }

    @Override
    public void onDeselect(int index, int totalCount) {

    }

    @Override
    public void inLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        //离开当前View时
        mLeftToRight = !leftToRight;
        mClipPercent = 1.0f - leavePercent;
        invalidate();
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        mLeftToRight = leftToRight;
        mClipPercent = enterPercent;
        invalidate();
    }
}
