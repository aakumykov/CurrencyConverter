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

    private MutableLiveData<ePageState> pageStateLiveData;
    private MutableLiveData<CurrencyBoard> currencyBoardLiveData;
    private MutableLiveData<String> errorMsgLiveData;

    private String mDataSourceURL;
    private CurrencyBoardProvider mCurrencyBoardProvider;

    public MainViewModel() {
        this.pageStateLiveData = new MutableLiveData<>();
        this.currencyBoardLiveData = new MutableLiveData<>();
        this.errorMsgLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<ePageState> getPageState() {
        return pageStateLiveData;
    }

    public MutableLiveData<CurrencyBoard> getCurrencyBoard() {
        return currencyBoardLiveData;
    }

    public MutableLiveData<String> getErrorMsg() {
        return errorMsgLiveData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onViewCreated() {
        if (null == mCurrencyBoardProvider)
            mCurrencyBoardProvider = new CurrencyBoardProvider(mDataSourceURL);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onViewStarted() {
        if (null == currencyBoardLiveData.getValue())
            loadData();
    }

    private void loadData() {

        pageStateLiveData.setValue(ePageState.REFRESHING);

        Handler handler = new Handler(Looper.getMainLooper());

        mCurrencyBoardProvider.getData(new CurrencyBoardProvider.iDataRetriveCallbacks() {
            @Override
            public void onDataRetriveSuccess(CurrencyBoard currencyBoard) {
                handler.post(() -> {
                    pageStateLiveData.setValue(ePageState.READY);
                    currencyBoardLiveData.setValue(currencyBoard);
                });
            }

            @Override
            public void onDataRetriveFailed(String errorMsg) {
                handler.post(() -> {
                    pageStateLiveData.setValue(ePageState.READY);
                    errorMsgLiveData.setValue(errorMsg);
                });
            }
        });
    }

    public void setDataSourceURL(String s) {
        mDataSourceURL = s;
    }
}
