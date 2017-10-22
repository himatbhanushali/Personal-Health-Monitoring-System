package com.krishnchinya.personalhealthmonitoringsystem.other;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by KrishnChinya on 3/26/17.
 */

public interface RetrofitObjectAPI {

        @GET("dailymed/services/v2/spls.json?pagesize=5")
        Call<Medicine> getMedicines(@Query("drug_name") String drugname);

        @GET("search/{phrase}")
        Call<FoodItems> getFoodItem(@Path("phrase") String itemname, @Query("results") String result, @Query("fields") String fields,
                                    @Query("appId") String appId, @Query("appKey") String appKey);

        @GET("{phrase}")
        Call<fooddetails> getFooddetails(@Path("phrase") String itemname, @Query("id") String itemid, @Query("appId") String appId,
                                    @Query("appKey") String appKey);


}