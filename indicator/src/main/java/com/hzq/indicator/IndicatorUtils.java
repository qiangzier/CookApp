package com.hzq.indicator;

import android.content.Context;

/**
 * @author: hezhiqiang
 * @date: 2017/7/4
 * @version:
 * @description:
 */

public class IndicatorUtils {

    public static int dp2px(Context context,double dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
