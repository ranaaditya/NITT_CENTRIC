package com.ranaaditya.nitt_central.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormModel {
    @SerializedName("name")
    String name;
    @SerializedName("id")
    int id;
    @SerializedName("links")
    ArrayList<LinkModel> links;
    @SerializedName("Required")
    ArrayList<RequiredModel> required;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<LinkModel> getLinks() {
        return links;
    }

    public ArrayList<RequiredModel> getRequired() {
        return required;
    }
}
