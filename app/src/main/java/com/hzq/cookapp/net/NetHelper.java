package com.hzq.cookapp.net;

import android.text.TextUtils;

import com.hzq.cookapp.CookApplication;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.db.entity.CookDatabaseHelper;
import com.hzq.cookapp.model.CategoryInfo;
import com.hzq.cookapp.model.CookModel;
import com.hzq.cookapp.utils.CookUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author: hezhiqiang
 * @date: 2017/6/22
 * @version:
 * @description:
 */

public class NetHelper {
    private static CookApi cookApi;
    private static CookApplication mApp;
    private NetHelper(){}

    public static void init(CookApplication application){
        mApp = application;
        cookApi = application.getRetrofit().create(CookApi.class);
    }

    public static void getCategroy(final com.hzq.cookapp.callback.Callback<List<CategoryEntity>> call){
        Call<Response<CategoryInfo>> allCooks = cookApi.getAllCooks(CookUtils.getAppKey(mApp));
        final RequestCall<CategoryInfo> callback = new RequestCall<CategoryInfo>() {
            @Override
            public void success(final CategoryInfo categoryInfo) {
                Task.callInBackground(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        saveCategroyInfo(categoryInfo);
                        CookDatabaseHelper.getCategroyDao().insert(result);
                        return true;
                    }
                }).continueWith(new Continuation<Boolean, Void>() {
                    @Override
                    public Void then(Task<Boolean> task) throws Exception {
                        call.call(result);
                        result.clear();
                        return null;
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
            }
        };
        allCooks.enqueue(new Callback<Response<CategoryInfo>>() {
            @Override
            public void onResponse(Call<Response<CategoryInfo>> call, retrofit2.Response<Response<CategoryInfo>> response) {
                callback.parseResponse(response);
            }

            @Override
            public void onFailure(Call<Response<CategoryInfo>> call, Throwable t) {
                callback.onFailure(t);
            }
        });

    }

    private static List<CategoryEntity> result = new ArrayList<>();
    private static List<CategoryEntity> saveCategroyInfo(CategoryInfo categoryInfo) {
        if(categoryInfo != null){
            CategoryEntity info = categoryInfo.categoryInfo;
            if(info != null){
                if (TextUtils.isEmpty(info.getParentId())){
                    info.setParentId("-100");
                }
                result.add(info);
            }

            if(categoryInfo.childs != null && categoryInfo.childs.size() > 0){
                for (CategoryInfo child : categoryInfo.childs) {
                    saveCategroyInfo(child);
                }
            }
        }
        return result;
    }

    public static void getCooksByCId(String cid,
                                     String name,
                                     int page,
                                     int size,
                                     final RequestCall<ListDataResponse<CookModel>> callback){
        Call<Response<ListDataResponse<CookModel>>> cooksByCategroyId =
                cookApi.getCooksByCategroyId(CookUtils.getAppKey(mApp), cid, name, page, size);
        cooksByCategroyId.enqueue(new Callback<Response<ListDataResponse<CookModel>>>() {
            @Override
            public void onResponse(Call<Response<ListDataResponse<CookModel>>> call, retrofit2.Response<Response<ListDataResponse<CookModel>>> response) {
                callback.parseResponse(response);
            }

            @Override
            public void onFailure(Call<Response<ListDataResponse<CookModel>>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getCooksByCId(String cid,RequestCall<ListDataResponse<CookModel>> callback){
        getCooksByCId(cid,"",1,100,callback);
    }

    public static void getCookById(String id, final RequestCall<CookModel> callback){
        Call<Response<CookModel>> cookById = cookApi.getCookById(CookUtils.getAppKey(mApp), id);
        cookById.enqueue(new Callback<Response<CookModel>>() {
            @Override
            public void onResponse(Call<Response<CookModel>> call, retrofit2.Response<Response<CookModel>> response) {
                callback.parseResponse(response);
            }

            @Override
            public void onFailure(Call<Response<CookModel>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }



}
