package com.hzq.cookapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hzq.cookapp.db.CookDatabaseHelper;
import com.hzq.cookapp.net.NetHelper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: hezhiqiang
 * @date: 2017/6/21
 * @version:
 * @description:
 */

public class CookApplication extends Application {

    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();
        NetHelper.init(this);
        Fresco.initialize(this);
    }

    private void initRetrofit(){
        CookDatabaseHelper.init(this);

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logger) //添加log拦截器
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com/v1/cook/")
                .callFactory(mOkHttpClient) //配置okhttp
                .addConverterFactory(GsonConverterFactory.create()) //配置json转换器
                .build();

    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }

}
