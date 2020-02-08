package com.ranaaditya.nitt_central.Models;

import com.google.gson.annotations.SerializedName;

public class FormModel {
    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }
}
