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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.converter_dialog.ConverterDialogFragment;
import com.github.aakumykov.cc.converter_dialog.ConverterDialogFragmentFactory;
import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.github.aakumykov.cc.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainView extends AppCompatActivity {

    private final String TAG = MainView.class.getSimpleName();
    private static final String FRAGMENT_TAG = "converter_dialog";

    private ActivityMainBinding mViewBinding;
    private MainViewModel mViewModel;
    private CurrencyList_DataAdapter mListAdapter;
    private Menu mMenu;
    private iItemClickListener mItemClickListener;
    private ConverterDialogFragmentFactory mDialogFragmentFactory;
    private ConverterDialogFragment mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDialogFragmentFactory = new ConverterDialogFragmentFactory();
        getSupportFragmentManager().setFragmentFactory(mDialogFragmentFactory);

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

        mItemClickListener = new iItemClickListener() {
            @Override
            public void onItemClicked(CurrencyViewHolder viewHolder) {
                Toast.makeText(
                        MainView.this,
                        String.valueOf(viewHolder.getAdapterPosition()),
                        Toast.LENGTH_SHORT
                ).show();
            }
        };

        mListAdapter = new CurrencyList_DataAdapter(mItemClickListener);

        mViewBinding.recyclerView.setAdapter(mListAdapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recyclerView.addItemDecoration(prepareItemDecoration());

        mViewBinding.converterLauncherView.setOnClickListener(v -> {
            onConverterLauncherClicked();
        });
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
                updateCurrencyInDialog(currencyBoard);
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
                if (mViewModel.refreshIsRunning())
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


    private void onConverterLauncherClicked() {
        showConvertionDialog();
    }

    private void showConvertionDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);

        if (null == fragment)
        {
             mDialogFragmentFactory =
                    (ConverterDialogFragmentFactory) fragmentManager.getFragmentFactory();

            /*mDialogFragmentFactory.updateCurrencyList(
                    mViewModel.getCurrencyBoard().getValue().getCurrencyList()
            );*/

            mDialogFragment = (ConverterDialogFragment)
                    mDialogFragmentFactory
                    .instantiate(getClassLoader(), ConverterDialogFragment.class.getName());

            mDialogFragment.show(getSupportFragmentManager(), FRAGMENT_TAG);
        }
    }

    private void updateCurrencyInDialog(CurrencyBoard currencyBoard) {
        mDialogFragmentFactory.updateCurrencyList(currencyBoard.getCurrencyList());
        if (null != mDialogFragment)
            mDialogFragment.performConvertion();
    }

    // TODO: уведомление об устаревших данных
    private void showToast(int stringResourceId) {
        Toast.makeText(this, stringResourceId, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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

        mViewBinding.infoView.setText(
                new SimpleDateFormat(
                        "Данные от dd MMMM yyyy, HH:mm",
                        new Locale("ru","RU")
                ).format(currencyBoard.getTimestamp())
        );
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