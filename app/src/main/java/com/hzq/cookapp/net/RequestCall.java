package com.hzq.cookapp.net;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author: hezhiqiang
 * @date: 2017/6/22
 * @version:
 * @description:
 */

public abstract class RequestCall<T> {
    public abstract void success(T t);
    public void error(String code,String msg){

    }


    public void parseResponse(retrofit2.Response<Response<T>> response){
        if(TextUtils.equals(response.body().retCode,"200")){
            success(response.body().result);
        }else{
            error(response.body().retCode,response.body().msg);
        }
    }

    public void onFailure(Throwable t){
        Log.d("NetHelper",t.getMessage());
        error("-1",t.getMessage());
    }
}
