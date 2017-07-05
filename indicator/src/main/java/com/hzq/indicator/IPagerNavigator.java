package com.hzq.indicator;

/**
 * @author: hezhiqiang
 * @date: 2017/7/3
 * @version:
 * @description: 抽象ViewPager指示器
 */

public interface IPagerNavigator {

    //ViewPager的三个回调
    void onPageScrolled(int position,float positionOffset,int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    /**
     * 当IPagerNavigator被添加到Indicator时调用
     */
    void onAttachToIndicator();

    /**
     * 当IPagerNavigator从Indicator上移除是被调用
     */
    void onDetachFromIndicator();

    /**
     * ViewPager内容改变时需要先调用此方法。
     */
    void notifyDataSetChanged();
}
