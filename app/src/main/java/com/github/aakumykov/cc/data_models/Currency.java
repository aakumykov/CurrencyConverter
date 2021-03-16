package com.github.aakumykov.cc.data_models;

import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("ID") private String id;
    @SerializedName("Name") private String name;
    @SerializedName("NumCode") private int numCode;
    @SerializedName("CharCode") private String charCode;
    @SerializedName("Nominal") private int nominal;
    @SerializedName("Value") private float value;
    @SerializedName("Previous") private float prevValue;

    public Currency() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public float getValue() {
        return value;
    }

    public float getPrevValue() {
        return prevValue;
    }
}
