package com.ranaaditya.nitt_central.Models;

import com.google.gson.annotations.SerializedName;

public class LinkModel {
    @SerializedName("name")
    String name;
    @SerializedName("link")
    String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
