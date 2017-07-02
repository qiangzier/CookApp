package com.hzq.cookapp;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.hzq.cookapp.adapter.ViewPagerAdapter;
import com.hzq.cookapp.db.entity.SelectCategoryEntity;
import com.hzq.cookapp.fragment.CategroyFragment;
import com.hzq.cookapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ViewPagerAdapter pagerAdapter = null;
    private List<SelectCategoryEntity> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition explode = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left);
//        //退出动画
//        getWindow().setExitTransition(explode);
//        //第一次进入使用
//        getWindow().setEnterTransition(explode);
//        //在此进入使用
//        getWindow().setReenterTransition(explode);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        setTitle("厨房助手");

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),data);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratorActivity.startAction(MainActivity.this, CategroyFragment.class,"");
            }
        });

        MainViewModel.getInstance(this).getObservableData().observe(this, new Observer<List<SelectCategoryEntity>>() {
            @Override
            public void onChanged(@Nullable List<SelectCategoryEntity> selectCategoryEntities) {
                pagerAdapter.setCategroyData(selectCategoryEntities);
            }
        });
    }

}
