package com.hzq.cookapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hzq.cookapp.fragment.CookListFragment;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class CookListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CookListFragment fragment = new CookListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",getIntent().getStringExtra("cid"));
        bundle.putString("name",getIntent().getStringExtra("name"));
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit();
    }

    public static void startAction(Context context,String cid,String title){
        Intent intent = new Intent(context,CookListActivity.class);
        intent.putExtra("cid",cid);
        intent.putExtra("name",title);
        context.startActivity(intent);
    }
}
