package com.github.aakumykov.cc.data_models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CurrencyBoard {

    public final static int DATA_VALIDITY_PERIOD_IN_DAYS = 1;

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

    public boolean isExpired() {
        long currentTime = new Date().getTime();
        long boardTime = mTimestamp.getTime();
        return (currentTime - boardTime) > TimeUnit.DAYS.toMillis(DATA_VALIDITY_PERIOD_IN_DAYS);
    }
}
