package com.github.aakumykov.cc.data_models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Currency implements Parcelable {

    @SerializedName("ID") private String id;
    @SerializedName("Name") private String name;
    @SerializedName("NumCode") private int numCode;
    @SerializedName("CharCode") private String charCode;
    @SerializedName("Nominal") private int nominal;
    @SerializedName("Value") private float value;
    @SerializedName("Previous") private float prevValue;


    public Currency() {}

    // Конструктор для создания "фиктивной валюты"
    public Currency(String name, String charCode, int nominal, float value) {
        this.name = name;
        this.charCode = charCode;
        this.nominal = nominal;
        this.value = value;
    }


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


    @NonNull @Override
    public String toString() {
        return Currency.class.getSimpleName() + " { " + this.charCode + " }";
    }


    // Parcelable
    private final static String CREATOR = Currency.class.getSimpleName();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(numCode);
        dest.writeString(charCode);
        dest.writeInt(nominal);
        dest.writeFloat(value);
        dest.writeFloat(prevValue);
    }

}
