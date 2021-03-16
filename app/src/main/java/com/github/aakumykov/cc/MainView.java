package com.github.aakumykov.cc;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.github.aakumykov.cc.databinding.ActivityMainBinding;

public class MainView extends AppCompatActivity {

    private final String TAG = MainView.class.getSimpleName();
    private ActivityMainBinding mViewBinding;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewModel = new ViewModelProvider.NewInstanceFactory().create(MainViewModel.class);

        mViewModel.getPageState().observe(this, new Observer<ePageState>() {
            @Override
            public void onChanged(ePageState pageState) {
                applyPageState(pageState);
            }
        });

        mViewModel.getCurrencyBoard().observe(this, new Observer<CurrencyBoard>() {
            @Override
            public void onChanged(CurrencyBoard currencyBoard) {
                Log.d(TAG, "Курс валют обновился");
            }
        });

        mViewModel.getErrorMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mViewBinding.errorView.setText(s);
            }
        });


        mViewBinding.swipeRefreshLayout.setColorSchemeResources(
                R.color.purple_200,
                R.color.purple_700,
                R.color.teal_700,
                R.color.teal_200
        );

        mViewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {

        });

        getLifecycle().addObserver(mViewModel);
    }

    private void applyPageState(ePageState pageState) {
       switch (pageState) {
            case REFRESHING:
                showRefreshThrobber();
                break;
            case READY:
                hideRefreshThrobber();
                break;
            default:
                throw new IllegalArgumentException("Unknown ePageState value: "+pageState);
        }
    }

    private void showRefreshThrobber() {
        mViewBinding.swipeRefreshLayout.setRefreshing(true);
    }

    private void hideRefreshThrobber() {
        mViewBinding.swipeRefreshLayout.setRefreshing(false);
    }

    // TODO: уведомление об устаревших данных
    private void showToast(int stringResourceId) {
        Toast.makeText(this, stringResourceId, Toast.LENGTH_SHORT).show();
    }
}