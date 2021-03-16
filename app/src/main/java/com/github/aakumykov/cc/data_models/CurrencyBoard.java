package com.github.aakumykov.cc.data_models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public class CurrencyBoard {

    @SerializedName("Date") private Date mDate;
    @SerializedName("PreviousDate") private Date mPrevDate;
    @SerializedName("PreviousURL") private String mPrevURL;
    @SerializedName("Timestamp") private Date mTimestamp;
    @SerializedName("Valute") private Map<String,Currency> mCurrencyMap;

    public CurrencyBoard() {}

    public int getValuteCount() {
        return mCurrencyMap.size();
    }

}
