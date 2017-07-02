package com.hzq.cookapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class GeneratorActivity extends BaseActivity {

    private Fragment newInstance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
        AppBarLayout barLayout = (AppBarLayout) findViewById(R.id.app_bar);
        if(TextUtils.isEmpty(getIntent().getStringExtra("title"))){
            barLayout.setVisibility(View.GONE);
        }else{
            initToolbar();
            setTitle(getIntent().getStringExtra("title"));
        }

        Intent intent = getIntent();
        if(intent != null){
            Class cls = (Class) intent.getSerializableExtra("clz");
            if(cls != null){
                try {
                    newInstance = (Fragment) Class.forName(cls.getName()).newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container,newInstance)
                            .commit();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void startAction(Context context,Class clz,String title){
        Intent intent = new Intent(context,GeneratorActivity.class);
        intent.putExtra("clz",clz);
        intent.putExtra("title",title);
        context.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
    }
}
