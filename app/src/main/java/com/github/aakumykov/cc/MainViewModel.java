package com.github.aakumykov.cc;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.github.aakumykov.cc.data_models.CurrencyBoard;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

    private final MutableLiveData<CurrencyBoard> mCurrencyBoardLiveData;
    private final MutableLiveData<Integer> mProgressMessageLiveData;
    private final MutableLiveData<String> mErrorMessageLiveData;

    private String mDataSourceURL;
    private CurrencyBoardProvider mCurrencyBoardProvider;
    private boolean mRefreshIsRunning = false;
    private final Context mAppContext;


    public MainViewModel(@NonNull Application application) {
        super(application);

        mAppContext = application.getApplicationContext();

        mCurrencyBoardLiveData = new MutableLiveData<>();
        mProgressMessageLiveData = new MutableLiveData<>();
        mErrorMessageLiveData = new MutableLiveData<>();
    }


    public void setDataSourceURL(String s) {
        mDataSourceURL = s;
    }


    public MutableLiveData<CurrencyBoard> getCurrencyBoard() {
        return mCurrencyBoardLiveData;
    }

    public MutableLiveData<String> getErrorMsg() {
        return mErrorMessageLiveData;
    }

    public MutableLiveData<Integer> getProgressMessage() {
        return mProgressMessageLiveData;
    }


    public void onRefreshRequested() {
        if (!mRefreshIsRunning) {
            mRefreshIsRunning = true;
            loadDataFromNetwork();
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onViewCreated() {
        if (null == mCurrencyBoardProvider)
            mCurrencyBoardProvider = new CurrencyBoardProvider(mDataSourceURL);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onViewStarted() {
        if (null == mCurrencyBoardLiveData.getValue())
            loadData();
    }

    private void loadData() {
        CurrencyBoard existingCB = mCurrencyBoardLiveData.getValue();
        if (null == existingCB) {
            if (CachedDataManager.cacheFileExists(mAppContext)) {
                loadDataFromCache();
            } else {
                loadDataFromNetwork();
            }
        }
    }

    private void loadDataFromCache() {
        CachedDataManager.readCachedString(mAppContext, new CachedDataManager.iFileReadCallbacks() {
            @Override
            public void onFileReadSuccess(String stringData) {
                processLoadedData(stringData, false);
            }

            @Override
            public void onFileReadError(String errorMsg) {
                loadDataFromNetwork();
            }
        });
    }

    private void loadDataFromNetwork() {

        mProgressMessageLiveData.setValue(R.string.updating_data);

        NetworkDataLoader.fetchData(mDataSourceURL, new NetworkDataLoader.iDataFetchCallbacks() {
            @Override
            public void onDataFetchSuccess(String jsonString) {
                processLoadedData(jsonString, true);
            }

            @Override
            public void onDataFetchFailed(String errorMsg) {
                showErrorMsg(errorMsg);
            }
        });
    }

    private void processLoadedData(String stringData, boolean isDataFromNetwork) {

        mRefreshIsRunning = false;

        CurrencyBoard currencyBoard = DataParser.parseData(stringData);

        Handler handler = new Handler(Looper.getMainLooper());

        if (null == currencyBoard) {
            handler.post(this::loadDataFromNetwork);
            return;
        }

        if (currencyBoard.isExpired()) {
            handler.post(this::loadDataFromNetwork);
            return;
        }

        handler.post(() -> {
            mCurrencyBoardLiveData.setValue(currencyBoard);
        });

        if (isDataFromNetwork)
            CachedDataManager.saveStringToCache(stringData, mAppContext, null);
    }

    private void showErrorMsg(String errorMsg) {
        mErrorMessageLiveData.setValue(errorMsg);
    }

    public boolean refreshIsRunning() {
        return mRefreshIsRunning;
    }
}
