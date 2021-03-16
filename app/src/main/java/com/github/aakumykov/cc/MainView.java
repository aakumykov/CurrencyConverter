package com.github.aakumykov.cc;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.github.aakumykov.cc.databinding.ActivityMainBinding;

public class MainView extends AppCompatActivity {

    private final String TAG = MainView.class.getSimpleName();
    private ActivityMainBinding mViewBinding;
    private MainViewModel mViewModel;
    private CurrencyList_DataAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareView();
        prepareViewModel();
        prepareLiveData();
    }


    private void prepareView() {

        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mViewBinding.getRoot());


        mViewBinding.swipeRefreshLayout.setColorSchemeResources(
                R.color.purple_200,
                R.color.purple_700,
                R.color.teal_700,
                R.color.teal_200
        );

        mViewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mViewModel.onRefreshRequested();
        });


        mListAdapter = new CurrencyList_DataAdapter();

        mViewBinding.recyclerView.setAdapter(mListAdapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recyclerView.addItemDecoration(prepareItemDecoration());
    }

    private void prepareViewModel() {
        mViewModel = new ViewModelProvider.NewInstanceFactory().create(MainViewModel.class);

        // TODO: брать из настроек
        mViewModel.setDataSourceURL("https://www.cbr-xml-daily.ru/daily_json.js");

        getLifecycle().addObserver(mViewModel);
    }

    private void prepareLiveData() {

        mViewModel.getPageState().observe(this, new Observer<ePageState>() {
            @Override
            public void onChanged(ePageState pageState) {
                applyPageState(pageState);
            }
        });

        mViewModel.getCurrencyBoard().observe(this, new Observer<CurrencyBoard>() {
            @Override
            public void onChanged(CurrencyBoard currencyBoard) {
                updateView(currencyBoard);
            }
        });

        mViewModel.getErrorMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mViewBinding.errorView.setText(s);
                mViewBinding.errorView.setVisibility(View.VISIBLE);
            }
        });
    }


    private void applyPageState(ePageState pageState) {
       switch (pageState) {
            case REFRESHING:
                disableRefreshListener();
                showRefreshThrobber();
                break;

            case READY:
                enableRefreshListener();
                hideRefreshThrobber();
                break;

            default:
                throw new IllegalArgumentException("Unknown ePageState value: "+pageState);
        }
    }

    private void disableRefreshListener() {
        mViewBinding.swipeRefreshLayout.setEnabled(false);
    }

    private void enableRefreshListener() {
        mViewBinding.swipeRefreshLayout.setEnabled(true);
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

    private RecyclerView.ItemDecoration prepareItemDecoration() {

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        Drawable drawable = ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.list_item_divider,
                null
        );

        dividerItemDecoration.setDrawable(drawable);
        return dividerItemDecoration;
    }

    private void updateView(CurrencyBoard currencyBoard) {
        mListAdapter.setList(currencyBoard.getCurrencyList());
    }
}