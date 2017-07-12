package com.hzq.cookapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hzq.cookapp.callback.Callback;
import com.hzq.cookapp.db.CookDatabaseHelper;
import com.hzq.cookapp.utils.CategroyDataStore;
import com.hzq.cookapp.utils.CookUtils;

/**
 * @author: hezhiqiang
 * @date: 2017/7/11
 * @version:
 * @description:
 */

public class SplashActivity extends BaseActivity {

    private long startTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        CookUtils.setImmersiveStatusBar(this);
        startTime = System.currentTimeMillis();
        CookDatabaseHelper.init(getApplicationContext());
        CategroyDataStore.getParentCateGroy(new Callback<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(System.currentTimeMillis() - startTime > 1000) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }else{
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
        });
    }
}
