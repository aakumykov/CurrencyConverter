package com.github.aakumykov.cc.converter_dialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.github.aakumykov.cc.data_models.Currency;

import java.util.ArrayList;
import java.util.List;

public class ConverterDialogFragmentFactory extends FragmentFactory {

    private final List<Currency> mCurrencyList;

    public ConverterDialogFragmentFactory() {
        mCurrencyList = new ArrayList<>();
    }

    @NonNull @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        return new ConverterDialogFragment(mCurrencyList);
    }

    public void updateCurrencyList(List<Currency> currencyList) {
        mCurrencyList.clear();
        mCurrencyList.addAll(currencyList);

        addRussianRubleToList();
    }

    private void addRussianRubleToList() {
        Currency russianRuble = new Currency("Российский рубль", "RUB", 1, 1);
        mCurrencyList.add(0, russianRuble);
    }
}
