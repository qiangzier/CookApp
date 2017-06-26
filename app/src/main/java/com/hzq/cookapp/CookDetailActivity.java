package com.hzq.cookapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hzq.cookapp.adapter.CookDetailAdapter;
import com.hzq.cookapp.model.CookModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: hezhiqiang
 * @date: 2017/6/24
 * @version:
 * @description:
 */

public class CookDetailActivity extends BaseActivity {

    @BindView(R.id.bgImg)
    SimpleDraweeView bgImg;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CookModel cookModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_detail_layout);
        ButterKnife.bind(this);
        initToolbar();

        cookModel = (CookModel) getIntent().getSerializableExtra("cook_model");
        if(cookModel == null)
            throw new NullPointerException("cookModel不能为null");

        if(cookModel.recipe != null && !TextUtils.isEmpty(cookModel.recipe.img)){
            bgImg.setImageURI(cookModel.recipe.img);
        }

//        toolbar.setTitle(cookModel.name);
//        toolbarLayout.setTitle(cookModel.name);

        setTitle(cookModel.name);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CookDetailAdapter(cookModel.recipe));
    }

    public static void startAction(Context context,CookModel cookModel){
        Intent intent = new Intent(context,CookDetailActivity.class);
        intent.putExtra("cook_model",cookModel);
        context.startActivity(intent);
    }
}
