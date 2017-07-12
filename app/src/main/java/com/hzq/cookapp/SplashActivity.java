package com.hzq.cookapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hzq.cookapp.callback.Callback;
import com.hzq.cookapp.db.CookDatabaseHelper;
import com.hzq.cookapp.utils.CategroyDataStore;

/**
 * @author: hezhiqiang
 * @date: 2017/7/11
 * @version:
 * @description:
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CookDatabaseHelper.init(getApplicationContext());
        CategroyDataStore.getParentCateGroy(new Callback<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        },1000);
    }
}
