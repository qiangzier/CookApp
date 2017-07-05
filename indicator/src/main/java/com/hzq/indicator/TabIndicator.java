package com.hzq.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hzq.indicator.callback.OnGetIndicatorViewAdapter;
import com.hzq.indicator.helper.ViewPagerHelper;
import com.hzq.indicator.impl.CommonNavigator;
import com.hzq.indicator.impl.CommonNavigatorAdapter;
import com.hzq.indicator.impl.IPagerIndicator;
import com.hzq.indicator.impl.IPagerTitleView;
import com.hzq.indicator.impl.indicators.LinePagerIndicator;
import com.hzq.indicator.impl.titles.ClipPagerTitleView;

/**
 * @author: hezhiqiang
 * @date: 2017/7/3
 * @version:
 * @description:
 */

public class TabIndicator extends FrameLayout {

    private IPagerNavigator navigator;
    //注意：以下属性只有在使用默认TitleView和Indicator时才起作用，
    private int mTextColor;         //tab title未选中颜色
    private int mSelectTextColor;   //tab title选中颜色
    private int mTextSize;          //tab title字体大小
    private int mIndicatorHeight;   //tab indicator 高度
    private int mIndicatorColor;    //tab indicator 颜色

    private DataSetObserver dataSetObserver;
    private PagerAdapter pagerAdapter;

    private OnGetIndicatorViewAdapter getIndicatorViewAdapter; //通过这个适配器获取TitleView与IndicatorView

    public TabIndicator(@NonNull Context context) {
        this(context,null);
    }

    public TabIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TabIndicator,defStyleAttr,0);
        mTextColor = a.getColor(R.styleable.TabIndicator_tab_textColor, Color.BLACK);
        mSelectTextColor = a.getColor(R.styleable.TabIndicator_tab_selectTextColor, Color.BLUE);
        mIndicatorColor = a.getColor(R.styleable.TabIndicator_tab_indicatorColor,Color.BLUE);
        mTextSize = a.getDimensionPixelSize(R.styleable.TabIndicator_tab_textSize,16);
        mIndicatorHeight = a.getDimensionPixelSize(R.styleable.TabIndicator_tab_indicatorHeight,3);
        a.recycle();
    }

    /**适配ViewPager 指示器**/
    public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){
        if(navigator != null){
            navigator.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    public void onPageSelected(int position){
        if(navigator != null){
            navigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state){
        if(navigator != null){
            navigator.onPageScrollStateChanged(state);
        }
    }

    public IPagerNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(IPagerNavigator navigator) {
        if(this.navigator == navigator){
            return;
        }
        if(this.navigator != null)
            this.navigator.onDetachFromIndicator();

        this.navigator = navigator;

        removeAllViews();
        if(this.navigator instanceof View){
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView((View) navigator,lp);
            navigator.onAttachToIndicator();
        }
    }

    /**
     * 与ViewPager绑定
     * @param viewPager
     */
    public void setupWithViewPager(ViewPager viewPager){
        pagerAdapter = viewPager.getAdapter();
        if(pagerAdapter == null)
            throw new NullPointerException("PagerAdapter is null");
        final IPagerNavigator navigator = getDefaultNavigator(viewPager);
        setNavigator(navigator);
        ViewPagerHelper.bind(this,viewPager);
        //PagerAdapter数据更新时通知TabIndicator刷新数据
        if(dataSetObserver == null){
            dataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    navigator.notifyDataSetChanged();
                }
            };
            pagerAdapter.registerDataSetObserver(dataSetObserver);
        }
    }


    //内置一个默认的指示器
    private IPagerNavigator getDefaultNavigator(final ViewPager viewPager){
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setSkimOver(true); //夸多页面切换，中间也是否显示 "掠过"效果
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return pagerAdapter.getCount();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                if(getIndicatorViewAdapter != null){
                    IPagerTitleView titleView = getIndicatorViewAdapter.getTitleView(context,index);
                    if(titleView != null) {
                        titleView.setText(pagerAdapter.getPageTitle(index).toString());
                        return titleView;
                    }
                }

                //默认title效果
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(pagerAdapter.getPageTitle(index).toString());
                clipPagerTitleView.setTextColor(mTextColor);
                clipPagerTitleView.setClipColor(mSelectTextColor);
                clipPagerTitleView.setTextSize(mTextSize);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                if(getIndicatorViewAdapter != null){
                    IPagerIndicator iPagerIndicator = getIndicatorViewAdapter.getIndicator(context);
                    if(iPagerIndicator != null)
                        return iPagerIndicator;
                }
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(mIndicatorColor);
                indicator.setLineHeight(mIndicatorHeight);
                return indicator;
            }
        });
        return commonNavigator;
    }

    public void setGetIndicatorViewAdapter(OnGetIndicatorViewAdapter getIndicatorViewAdapter) {
        this.getIndicatorViewAdapter = getIndicatorViewAdapter;
    }
}
