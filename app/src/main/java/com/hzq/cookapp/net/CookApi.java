package com.hzq.cookapp.net;

import com.hzq.cookapp.model.CategoryInfo;
import com.hzq.cookapp.model.CookModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: hezhiqiang
 * @date: 2017/6/21
 * @version:
 * @description:
 */

public interface CookApi {
    @GET("category/query")
    Call<Response<CategoryInfo>> getAllCooks(@Query("key") String cookKey);

    @GET("menu/search")
    Call<Response<ListDataResponse<CookModel>>> getCooksByCategroyId(
            @Query("key") String cookKey,
            @Query("cid") String cid,
            @Query("name") String name,
            @Query("page") int page,
            @Query("size") int size);

    @GET("menu/query")
    Call<Response<CookModel>> getCookById(@Query("key") String cookKey,@Query("id") String id);

}
