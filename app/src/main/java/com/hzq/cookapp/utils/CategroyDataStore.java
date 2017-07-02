package com.hzq.cookapp.utils;

import com.hzq.cookapp.callback.Callback;
import com.hzq.cookapp.db.entity.CategoryEntity;
import com.hzq.cookapp.db.CookDatabaseHelper;
import com.hzq.cookapp.net.NetHelper;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * @author: hezhiqiang
 * @date: 2017/6/23
 * @version:
 * @description:
 */

public class CategroyDataStore {


    public static void getCategroys(final Callback<List<CategoryEntity>> callback){

        Task.callInBackground(new Callable<List<CategoryEntity>>() {
            @Override
            public List<CategoryEntity> call() throws Exception {
                return CookDatabaseHelper.getCategroyDao().getCategroys1();
            }
        }).continueWith(new Continuation<List<CategoryEntity>, Boolean>() {
            @Override
            public Boolean then(Task<List<CategoryEntity>> task) throws Exception {
                if(task.getResult() == null || task.getResult().size() == 0){
                    NetHelper.getCategroy(callback);
                }else{
                    callback.call(task.getResult());
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    public static void getParentCateGroy(final Callback<Boolean> callback){
        Task.callInBackground(new Callable<CategoryEntity>() {
            @Override
            public CategoryEntity call() throws Exception {
                return CookDatabaseHelper.getCategroyDao().getParentCategroy("-100");
            }
        }).continueWith(new Continuation<CategoryEntity, Boolean>() {
            @Override
            public Boolean then(Task<CategoryEntity> task) throws Exception {
                if(task.getResult() == null){
                    NetHelper.getCategroy(new Callback<List<CategoryEntity>>() {
                        @Override
                        public void call(List<CategoryEntity> entities) {
                            callback.call(true);
                        }
                    });
                    return false;
                }else{
                    callback.call(true);
                    return true;
                }
            }
        },Task.UI_THREAD_EXECUTOR);
    }

}
