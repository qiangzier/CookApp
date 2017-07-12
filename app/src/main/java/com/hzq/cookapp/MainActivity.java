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
import com.hzq.cookapp.viewmodel.MainViewModel;
import com.hzq.indicator.TabIndicator;
import com.hzq.indicator.callback.OnGetIndicatorViewAdapter;
import com.hzq.indicator.impl.IPagerIndicator;
import com.hzq.indicator.impl.indicators.WrapPagerIndicator;

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
        initToolbar();
        setTitle("厨房助手");

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
//                GeneratorActivity.startAction(MainActivity.this, CategroyFragment.class,"");
            }
        });

        MainViewModel.getInstance(this).getObservableData().observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntity> selectCategoryEntities) {
                pagerAdapter.setCategroyData(selectCategoryEntities);
            }
        });
    }

}
