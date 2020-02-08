package com.ranaaditya.nitt_central.Models;

import com.google.gson.annotations.SerializedName;

public class RequiredModel {
    @SerializedName("name")
    String name;
    @SerializedName("type")
    int type;
    @SerializedName("amount")
    int amount;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
