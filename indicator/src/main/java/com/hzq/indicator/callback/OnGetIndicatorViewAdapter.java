package com.hzq.indicator.callback;

import android.content.Context;

import com.hzq.indicator.impl.IPagerIndicator;
import com.hzq.indicator.impl.IPagerTitleView;

/**
 * @author: hezhiqiang
 * @date: 2017/7/5
 * @version:
 * @description:
 */

public class OnGetIndicatorViewAdapter {
    public IPagerTitleView getTitleView(Context context, int index){
        return null;
    }

    public IPagerIndicator getIndicator(Context context){
        return null;
    }
}
