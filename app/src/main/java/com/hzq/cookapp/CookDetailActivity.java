package com.hzq.cookapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hzq.cookapp.adapter.CookDetailAdapter;
import com.hzq.cookapp.model.CookModel;
import com.hzq.cookapp.utils.CookUtils;

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
        //解决fresco图片不显示的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(
                    DraweeTransition.createTransitionSet(
                            ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
            getWindow().setSharedElementReturnTransition(
                    DraweeTransition.createTransitionSet(
                            ScalingUtils.ScaleType.CENTER_CROP, ScalingUtils.ScaleType.CENTER_CROP));
        }
        ButterKnife.bind(this);
        initToolbar();
        CookUtils.setImmersiveStatusBar(this);
        CookUtils.setImmersiveStatusBarToolbar(getToolbar(), this);

        cookModel = (CookModel) getIntent().getSerializableExtra("cook_model");
        if (cookModel == null)
            throw new NullPointerException("cookModel不能为null");

        setTitle(cookModel.name);

        if (cookModel.recipe != null && !TextUtils.isEmpty(cookModel.recipe.img)) {
            bgImg.setImageURI(cookModel.recipe.img);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CookDetailAdapter(cookModel.recipe));
    }

    public static void startAction(Context context, View sharedView, CookModel cookModel) {
        Intent intent = new Intent(context, CookDetailActivity.class);
        intent.putExtra("cook_model", cookModel);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) context,
                Pair.create(sharedView, context.getString(R.string.transition_cook_detail_imgv_bg)),
                Pair.create(sharedView, context.getString(R.string.transition_cook_detail_content))
        );
        context.startActivity(intent, options.toBundle());
    }
}
