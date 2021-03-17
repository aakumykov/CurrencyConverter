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

    private final MutableLiveData<ePageState> mPageStateLiveData;
    private final MutableLiveData<CurrencyBoard> mCurrencyBoardLiveData;
    private final MutableLiveData<String> mErrorMsgLiveData;

    private String mDataSourceURL;
    private CurrencyBoardProvider mCurrencyBoardProvider;
    private final Context mAppContext;


    public MainViewModel(@NonNull Application application) {
        super(application);

        mAppContext = application.getApplicationContext();

        mPageStateLiveData = new MutableLiveData<>();
        mCurrencyBoardLiveData = new MutableLiveData<>();
        mErrorMsgLiveData = new MutableLiveData<>();
    }


    public void setDataSourceURL(String s) {
        mDataSourceURL = s;
    }


    public MutableLiveData<ePageState> getPageState() {
        return mPageStateLiveData;
    }

    public MutableLiveData<CurrencyBoard> getCurrencyBoard() {
        return mCurrencyBoardLiveData;
    }

    public MutableLiveData<String> getErrorMsg() {
        return mErrorMsgLiveData;
    }


    public void onRefreshRequested() {
        loadDataFromNetwork();
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

        mPageStateLiveData.setValue(ePageState.REFRESHING);

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

        CurrencyBoard currencyBoard = DataParser.parseData(stringData);

        Handler handler = new Handler(Looper.getMainLooper());

        if (null != currencyBoard) {
            handler.post(() -> {
                mPageStateLiveData.setValue(ePageState.READY);
                mCurrencyBoardLiveData.setValue(currencyBoard);
            });

            if (isDataFromNetwork)
                CachedDataManager.saveStringToCache(stringData, mAppContext, null);
        }
        else {
            // TODO: локализовать
            handler.post(this::loadDataFromNetwork);
        }
    }

    private void showErrorMsg(String errorMsg) {
        mPageStateLiveData.setValue(ePageState.READY);
        mErrorMsgLiveData.setValue(errorMsg);
    }


    /*private void loadData(boolean force) {

        mPageStateLiveData.setValue(ePageState.REFRESHING);

        Handler handler = new Handler(Looper.getMainLooper());

        mCurrencyBoardProvider.getData(force, new CurrencyBoardProvider.iDataRetriveCallbacks() {
            @Override
            public void onDataRetriveSuccess(CurrencyBoard currencyBoard) {
                handler.post(() -> {
                    mPageStateLiveData.setValue(ePageState.READY);
                    mCurrencyBoardLiveData.setValue(currencyBoard);
                });
            }

            @Override
            public void onDataRetriveFailed(String errorMsg) {
                handler.post(() -> {
                    mPageStateLiveData.setValue(ePageState.READY);
                    mErrorMsgLiveData.setValue(errorMsg);
                });
            }
        });
    }*/
}
