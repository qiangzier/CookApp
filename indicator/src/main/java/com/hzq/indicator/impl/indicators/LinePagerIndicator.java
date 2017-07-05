package com.hzq.indicator.impl.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hzq.indicator.IndicatorUtils;
import com.hzq.indicator.impl.ArgbEvaluatorHolder;
import com.hzq.indicator.impl.IPagerIndicator;
import com.hzq.indicator.impl.PositionData;

import java.util.Arrays;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 2017/7/4
 * @version:
 * @description: 直线ViewPager指示器，带颜色渐变
 */

public class LinePagerIndicator extends View implements IPagerIndicator {

    public static final int MODE_MATCH_EDGE = 0;    //直线宽度 == title宽度 - 2 * mXOffset    （与title等宽）
    public static final int MODE_WRAP_CONTENT = 1;  //直线宽度 == title内容宽度 - 2 * mXOffset （与title文字等宽）
    public static final int MODE_EXACTLY = 2;       //直线宽度 == mLineWith(自定义宽度)

    private int mMode;  //默认为Mode_match_edge模式

    //控制线性动画
    private LinearInterpolator startInterpolator = new LinearInterpolator();
    private LinearInterpolator endInterpolator = new LinearInterpolator();

    private float mYOffset;         //想对于底部的偏移量，如果你想让直线位于title上方，设置它即可
    private float mXOffset;         //横向left与right的偏移量
    private float mLineHeight;      //指示条的高度
    private float mLineWidth;       //指示条的宽度
    private float mRoundRadius;     //圆角半径
    private boolean mRoundRadiusSet; //是否设置圆角

    private Paint mPaint;
    private List<PositionData> mPositionDataList;
    private List<Integer> mColors;

    private RectF mLineRect = new RectF();

    public LinePagerIndicator(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mLineHeight = IndicatorUtils.dp2px(context,3);
        mLineWidth = IndicatorUtils.dp2px(context,10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mLineRect,mRoundRadius,mRoundRadius,mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mPositionDataList == null || mPositionDataList.isEmpty()) return;
        //计算颜色
        if(mColors != null && mColors.size() > 0){
            int currColor = mColors.get(Math.abs(position) % mColors.size());
            int nextColor = mColors.get(Math.abs(position + 1) % mColors.size());
            int color = ArgbEvaluatorHolder.eval(positionOffset,currColor,nextColor);
            mPaint.setColor(color);
        }

        //计算锚点位置
        PositionData current = mPositionDataList.get(Math.min(mPositionDataList.size() - 1,position));
        PositionData next = mPositionDataList.get(Math.min(mPositionDataList.size() - 1,position + 1));

        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;

        if(mMode == MODE_MATCH_EDGE) {
            leftX = current.mLeft + mXOffset;
            nextLeftX = next.mLeft + mXOffset;
            rightX = current.mRight - mXOffset;
            nextRightX = next.mRight - mXOffset;
        } else if(mMode == MODE_WRAP_CONTENT) {
            leftX = current.mContentLeft + mXOffset;
            nextLeftX = next.mContentLeft + mXOffset;
            rightX = current.mContentRight - mXOffset;
            nextRightX = next.mContentRight - mXOffset;
        } else {
            leftX = current.mLeft + (current.width() - mLineWidth) / 2;
            nextLeftX = next.mLeft + (next.width() - mLineWidth) / 2;
            rightX = current.mLeft + (current.width() + mLineWidth) / 2;
            nextRightX = next.mLeft + (next.width() + mLineWidth) / 2;
        }

        mLineRect.left = leftX + (nextLeftX - leftX) * startInterpolator.getInterpolation(positionOffset);
        mLineRect.right = rightX + (nextRightX - rightX) * endInterpolator.getInterpolation(positionOffset);
        mLineRect.top = getHeight() - mLineHeight -mYOffset;
        mLineRect.bottom = getHeight() - mYOffset;

        //设置圆角
        if(mRoundRadiusSet)
            mRoundRadius = mLineHeight / 2;

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        this.mPositionDataList = dataList;
    }

    public void setMode(int mode) {
        if(mode == MODE_EXACTLY || mode == MODE_MATCH_EDGE || mode == MODE_WRAP_CONTENT)
            this.mMode = mode;
        else
            throw new IllegalArgumentException("mode "+ mode + " not suported");
    }

    public void setColors(Integer... mColors) {
        this.mColors = Arrays.asList(mColors);
    }

    public void setYOffset(float mYOffset) {
        this.mYOffset = IndicatorUtils.dp2px(getContext(),mYOffset);
    }

    public void setXOffset(float mXOffset) {
        this.mXOffset = IndicatorUtils.dp2px(getContext(),mXOffset);
    }

    public void setLineHeight(float mLineHeight) {
        this.mLineHeight = mLineHeight;
    }

    public void setRoundRadiusSet(boolean mRoundRadiusSet) {
        this.mRoundRadiusSet = mRoundRadiusSet;
    }
}
