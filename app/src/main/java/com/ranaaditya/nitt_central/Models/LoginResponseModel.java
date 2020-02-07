package com.ranaaditya.nitt_central.Models;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {
    @SerializedName("Token")
    String token;
    @SerializedName("StatusCode")
    int code;

    public LoginResponseModel(String token, int code) {
        this.token = token;
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public int getCode() {
        return code;
    }
}

