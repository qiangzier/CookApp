package com.hzq.indicator.impl.titles;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.hzq.indicator.IndicatorUtils;
import com.hzq.indicator.impl.IMeasureablePagerTitleView;

/**
 * @author: hezhiqiang
 * @date: 2017/7/4
 * @version:
 * @description:
 */

public class SimplePagerTitleView extends TextView implements IMeasureablePagerTitleView {

    protected int mSelectedColor;
    protected int mNormalColor;

    public SimplePagerTitleView(Context context) {
        super(context);

        setGravity(Gravity.CENTER);
        int padding = IndicatorUtils.dp2px(context,10);
        setPadding(padding,0,padding,0);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(),0,getText().length(),bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        Rect bount = new Rect();
        getPaint().getTextBounds(getText().toString(),0,getText().length(),bount);
        int contentWidth = bount.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTextColor(mSelectedColor);
    }

    @Override
    public void onDeselect(int index, int totalCount) {
        setTextColor(mNormalColor);
    }

    @Override
    public void inLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }

    @Override
    public void setText(String text) {
        super.setText(text);
    }

    public void setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }

    public int getNormalColor() {
        return mNormalColor;
    }
}
