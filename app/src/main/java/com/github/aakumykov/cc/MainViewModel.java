package com.github.aakumykov.cc;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.github.aakumykov.cc.data_models.CurrencyBoard;

public class MainViewModel extends ViewModel implements LifecycleObserver {

    private final MutableLiveData<ePageState> mPageStateLiveData;
    private final MutableLiveData<CurrencyBoard> mCurrencyBoardLiveData;
    private final MutableLiveData<String> mErrorMsgLiveData;

    private String mDataSourceURL;
    private boolean isLoading = false;
    private CurrencyBoardProvider mCurrencyBoardProvider;


    public MainViewModel() {
        mPageStateLiveData = new MutableLiveData<>();
        mCurrencyBoardLiveData = new MutableLiveData<>();
        mErrorMsgLiveData = new MutableLiveData<>();
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


    public void setDataSourceURL(String s) {
        mDataSourceURL = s;
    }

    public void onRefreshRequested() {
        if (!isLoading)
            loadData();
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

        mPageStateLiveData.setValue(ePageState.REFRESHING);

        Handler handler = new Handler(Looper.getMainLooper());

        mCurrencyBoardProvider.getData(new CurrencyBoardProvider.iDataRetriveCallbacks() {
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
    }

}
