package com.github.aakumykov.cc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.converter_dialog.ConverterDialogFragment;
import com.github.aakumykov.cc.converter_dialog.ConverterDialogFragmentFactory;
import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.github.aakumykov.cc.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "converter_dialog";

    private ActivityMainBinding mViewBinding;
    private MainViewModel mViewModel;
    private CurrencyListDataAdapter mListAdapter;
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.actionRefresh == item.getItemId()) {
            onRefreshClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void prepareView() {

        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mListAdapter = new CurrencyListDataAdapter();

        mViewBinding.recyclerView.setAdapter(mListAdapter);
        mViewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.recyclerView.addItemDecoration(prepareItemDecoration());

        mViewBinding.converterLauncherView.setOnClickListener(v -> showConverterDialog());
    }

    private void prepareViewModel() {

        mViewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication()))
                .get(MainViewModel.class);

        getLifecycle().addObserver(mViewModel);
    }

    private void prepareLiveData() {

        mViewModel.getCurrencyBoard().observe(this, currencyBoard -> {
            displayCurrencyBoard(currencyBoard);
            updateCurrencyInDialog(currencyBoard);
        });

        mViewModel.getErrorMsg().observe(this, this::showError);

        mViewModel.getProgressMessage().observe(this, stringResourceId -> {
            if (mViewModel.refreshIsRunning())
                showProgress(stringResourceId);
        });
    }


    private void showProgress(int stringResourceId) {
        showProgressBar();
        showMessage(stringResourceId, R.color.progress);
    }

    private void showInfo(String text) {
        hideProgressBar();
        showMessage(text, R.color.info);
    }

    private void showError(String s) {
        hideProgressBar();
        showMessage(s, R.color.error);
    }

    private void showConverterDialog() {

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
            mDialogFragment.onConverterValuesChanged();
    }

    private void displayCurrencyBoard(CurrencyBoard currencyBoard) {

        mListAdapter.setList(currencyBoard.getCurrencyList());

        String dateString = new SimpleDateFormat(
                getString(R.string.info_view_pattern),
                Locale.getDefault()
        ).format(currencyBoard.getTimestamp());

        String infoViewText = getString(R.string.info_view_text, dateString);

        showInfo(infoViewText);
    }

    private void onRefreshClicked() {
        if (networkIsAvailable())
            mViewModel.onRefreshRequested();
        else {
            Toast.makeText(this, R.string.device_id_offline, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean networkIsAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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



    private void showMessage(int stringResourceId, int colorId) {
        showMessage(getString(stringResourceId), colorId);
    }

    private void showMessage(String text, int colorId) {
        mViewBinding.infoView.setText(text);
        mViewBinding.infoView.setBackgroundColor(getResources().getColor(colorId));
    }

    private void showProgressBar() {
        mViewBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mViewBinding.progressBar.setVisibility(View.GONE);
    }

}