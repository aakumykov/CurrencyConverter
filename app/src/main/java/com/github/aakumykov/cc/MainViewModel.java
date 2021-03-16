package com.github.aakumykov.cc;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.github.aakumykov.cc.data_models.CurrencyBoard;

public class MainViewModel extends ViewModel implements LifecycleObserver {

    MutableLiveData<ePageState> pageStateLiveData;
    MutableLiveData<CurrencyBoard> currencyBoardLiveData;
    MutableLiveData<String> errorMsgLiveData;

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


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onStart() {
        if (null == currencyBoardLiveData.getValue() || CurrencyBoardProvider.dataIsExpired()) {

            pageStateLiveData.setValue(ePageState.REFRESHING);

            CurrencyBoardProvider.getBoard(new CurrencyBoardProvider.iDataLoadCallbacks() {
                @Override
                public void onDataLoadSuccess(CurrencyBoard currencyBoard) {
                    pageStateLiveData.setValue(ePageState.READY);
                    currencyBoardLiveData.setValue(currencyBoard);
                }

                @Override
                public void onDataLoadFailed(String errorMsg) {
                    pageStateLiveData.setValue(ePageState.READY);
                    errorMsgLiveData.setValue(errorMsg);
                }

            });
        }
    }
}
