package com.ranaaditya.nitt_central.API;

import com.ranaaditya.nitt_central.Models.FormModel;
import com.ranaaditya.nitt_central.Models.LoginResponseModel;
import com.ranaaditya.nitt_central.Models.ShopModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    String base_url="http://10.1.96.244:8000/";

    @GET("getForms")
    Call<ArrayList<FormModel>> getForms();

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> login(@Field("Roll") String roll,
                                   @Field("Password") String pass);

    @FormUrlEncoded
    @POST("getShops")
    Call<ArrayList<ShopModel>> getShops(@Field("latitude") Double lat,
                                        @Field("longitude") Double lon,
                                        @Field("token") String token);

    @FormUrlEncoded
    @POST("specificForm")
    Call<FormModel> specificForm(@Field("id") int id);

    @FormUrlEncoded
    @POST("updateForm")
    Call<String> test(@Field("formId") int id,@Field("token") String token,@Field("image")String img);

    @Multipart
    @POST("updateForm")
    Call<String> submitDoc(@Part MultipartBody.Part file);
}
