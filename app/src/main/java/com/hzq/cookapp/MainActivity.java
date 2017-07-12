package com.hzq.cookapp;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.hzq.cookapp.adapter.ViewPagerAdapter;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.utils.CookUtils;
import com.hzq.cookapp.viewmodel.MainViewModel;
import com.hzq.indicator.TabIndicator;
import com.hzq.indicator.callback.OnGetIndicatorViewAdapter;
import com.hzq.indicator.impl.IPagerIndicator;
import com.hzq.indicator.impl.indicators.WrapPagerIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabIndicator)
    TabIndicator tabIndicator;


    private ViewPagerAdapter pagerAdapter = null;
    private List<CategoryEntity> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolbar();
        setTitle("厨房助手");
        CookUtils.setImmersiveStatusBar(this);
        CookUtils.setImmersiveStatusBarToolbar(getToolbar(), this);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),data);
        viewPager.setAdapter(pagerAdapter);

        tabIndicator.setGetIndicatorViewAdapter(new OnGetIndicatorViewAdapter() {
            @Override
            public IPagerIndicator getIndicator(Context context) {
                //设置指示器
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(getResources().getColor(R.color.colorPrimary_80));
//                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
//                indicator.setColors(getResources().getColor(R.color.colorPrimary));
                return indicator;
            }
        });
        //关联ViewPager
        tabIndicator.setupWithViewPager(viewPager);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookChannelActivity.startAction(MainActivity.this);
            }
        });

        MainViewModel.getInstance(this).getObservableData().observe(this, observer);
    }

    Observer<List<CategoryEntity>> observer = new Observer<List<CategoryEntity>>() {
        @Override
        public void onChanged(@Nullable List<CategoryEntity> selectCategoryEntities) {
            pagerAdapter.setCategroyData(selectCategoryEntities);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event != null && event.type == MessageEvent.CLICK_MODE){
            viewPager.setCurrentItem(event.position - 1);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
