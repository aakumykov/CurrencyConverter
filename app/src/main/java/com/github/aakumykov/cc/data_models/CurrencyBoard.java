package com.github.aakumykov.cc.data_models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CurrencyBoard {

    @SerializedName("Date") private Date mDate;
    @SerializedName("PreviousDate") private Date mPrevDate;
    @SerializedName("PreviousURL") private String mPrevURL;
    @SerializedName("Timestamp") private Date mTimestamp;
    @SerializedName("Valute") private Map<String,Currency> mCurrencyMap;

    public CurrencyBoard() {}

    public Date getTimestamp() {
        return mTimestamp;
    }

    public List<Currency> getCurrencyList() {
        return new ArrayList<>(mCurrencyMap.values());
    }
}
