package com.ranaaditya.nitt_central.Models;

import com.google.gson.annotations.SerializedName;

public class ShopModel {
    @SerializedName("name")
    String name;
    @SerializedName("id")
    int id;
    @SerializedName("upiId")
    String upi;

    public String getName() {
        return name;
    }
    public int getId(){return id;}

    public String getUpi() {
        return upi;
    }
}
