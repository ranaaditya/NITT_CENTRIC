package com.ranaaditya.nitt_central.API;

import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.Models.LoginResponseModel;
import com.ranaaditya.nitt_central.Models.ShopModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    String base_url="http://10.1.96.247:8000/";

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> login(@Field("Roll") String roll,
                                   @Field("Password") String pass);

    @FormUrlEncoded
    @POST("getShops")
    Call<ArrayList<ShopModel>> getShops(@Field("latitude") Double lat,
                                        @Field("longitude") Double lon,
                                        @Field("token") String token);

    @GET("getForms")
    Call<ArrayList<FormModel>> getForms();
}
