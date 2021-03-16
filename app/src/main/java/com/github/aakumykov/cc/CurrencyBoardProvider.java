package com.github.aakumykov.cc;

import android.util.Log;

import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.google.gson.JsonParseException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CurrencyBoardProvider {

    public interface iDataRetriveCallbacks {
        void onDataRetriveSuccess(CurrencyBoard currencyBoard);
        void onDataRetriveFailed(String errorMsg);
    }

    private static final String TAG = CurrencyBoardProvider.class.getSimpleName();
    private final String mDataSourceURL;
    private CurrencyBoard mCurrencyBoard;


    public CurrencyBoardProvider(String dataSourceURL) {
        mDataSourceURL = dataSourceURL;
    }

    public void getData(boolean forceLoad, iDataRetriveCallbacks callbacks) {

        if (forceLoad) {
            loadData(callbacks);
            return;
        }

        if (dataExists() && dataIsFresh())
            callbacks.onDataRetriveSuccess(mCurrencyBoard);
        else
            loadData(callbacks);
    }

    public boolean dataIsFresh() {
        if (null == mCurrencyBoard)
            return false;

        Date systemDate = new Date();
        Date dataDate = mCurrencyBoard.getTimestamp();
        long timeDiff = systemDate.getTime() - dataDate.getTime();
        return timeDiff < TimeUnit.HOURS.toHours(1);
    }

    private void loadData(iDataRetriveCallbacks callbacks) {
        
        if (cacheFileExists()) {
            loadLocalData();
            if (dataIsFresh()) {
                callbacks.onDataRetriveSuccess(mCurrencyBoard);
                return;
            }
        }

        getDataFromNetwork(callbacks);
    }

    private void loadLocalData() {

    }

    private boolean cacheFileExists() {
        return false;
    }

    private boolean dataExists() {
        return null != mCurrencyBoard;
    }

    private void getDataFromNetwork(iDataRetriveCallbacks callbacks) {

        NetworkDataLoader.fetchData(mDataSourceURL, new NetworkDataLoader.iDataFetchCallbacks() {

            @Override
            public void onDataFetchSuccess(String jsonString) {
                try {
                    mCurrencyBoard = DataParser.parseData(jsonString);
                    callbacks.onDataRetriveSuccess(mCurrencyBoard);
                }
                catch (JsonParseException e) {
                    String errorMsg = ExceptionUtils.getErrorMessage(e);
                    Log.e(TAG, errorMsg, e);
                    callbacks.onDataRetriveFailed(errorMsg);
                }
            }

            @Override
            public void onDataFetchFailed(String errorMsg) {
                callbacks.onDataRetriveFailed(errorMsg);
            }
        });
    }


}
