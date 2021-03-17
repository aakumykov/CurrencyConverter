package com.github.aakumykov.cc;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.github.aakumykov.cc.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainView extends AppCompatActivity {

    private final String TAG = MainView.class.getSimpleName();
    private ActivityMainBinding mViewBinding;
    private MainViewModel mViewModel;
    private CurrencyList_DataAdapter mListAdapter;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareView();
        prepareViewModel();
        prepareLiveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.actionRefresh == item.getItemId()) {
            mViewModel.onRefreshRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void prepareView() {

        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mViewBinding.getRoot());

        mListAdapter = new CurrencyList_DataAdapter();

        mViewBinding.recyclerView.setAdapter(mListAdapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recyclerView.addItemDecoration(prepareItemDecoration());
    }

    private void prepareViewModel() {
//        mViewModel = new ViewModelProvider.NewInstanceFactory().create(MainViewModel.class);
//        mViewModel = new ViewModelProvider(this, new MainViewModelFactory()).get(MainViewModel.class);
//        mViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
//        .create(MainViewModel.class);

        mViewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        // TODO: брать из настроек
        mViewModel.setDataSourceURL("https://www.cbr-xml-daily.ru/daily_json.js");

        getLifecycle().addObserver(mViewModel);
    }

    private void prepareLiveData() {

        mViewModel.getCurrencyBoard().observe(this, new Observer<CurrencyBoard>() {
            @Override
            public void onChanged(CurrencyBoard currencyBoard) {
                displayCurrencyBoard(currencyBoard);
            }
        });

        mViewModel.getErrorMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showError(s);
            }
        });

        mViewModel.getProgressMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer stringResourceId) {
                showProgressMessage(stringResourceId);
            }
        });
    }

    private void disableRefreshMenu() {
        if (null != mMenu) {
            MenuItem menuItem = mMenu.findItem(R.id.actionRefresh);
            if (null != menuItem)
                menuItem.setEnabled(false);
        }
    }

    private void enableRefreshMenu() {
        if (null != mMenu) {
            MenuItem menuItem = mMenu.findItem(R.id.actionRefresh);
            if (null != menuItem)
                menuItem.setEnabled(true);
        }
    }

    private void showRefreshThrobber() {
        mViewBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideRefreshThrobber() {
        mViewBinding.progressBar.setVisibility(View.GONE);
    }

    private void showProgressMessage(int stringResourceId) {
        mViewBinding.progressMessageView.setText(stringResourceId);
        showView(mViewBinding.progressMessageView);
        showView(mViewBinding.progressBar);
    }

    private void hideProgressMessage() {
        mViewBinding.progressMessageView.setText("");
        hideView(mViewBinding.progressMessageView);
        hideView(mViewBinding.progressBar);
    }

    private void showError(String s) {
        hideProgressMessage();
        mViewBinding.errorMessageView.setText(s);
        showView(mViewBinding.errorMessageView);
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

    private void displayCurrencyBoard(CurrencyBoard currencyBoard) {
        hideProgressMessage();
        mListAdapter.setList(currencyBoard.getCurrencyList());
    }

    private String date2string(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", new Locale("ru","RU"));
        return format.format(date);
    }

    private void showView(@Nullable View view) {
        if (null != view)
            view.setVisibility(View.VISIBLE);
    }

    private void hideView(@Nullable View view) {
        if (null != view)
            view.setVisibility(View.GONE);
    }
}