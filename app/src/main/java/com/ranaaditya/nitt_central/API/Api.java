package com.ranaaditya.nitt_central.API;

import com.ranaaditya.nitt_central.Models.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    String base_url="http://localhost:8000/";

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> login(@Field("Roll") String roll,
                                   @Field("Password") String pass);
}
