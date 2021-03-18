package com.github.aakumykov.cc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.aakumykov.cc.data_models.Currency;
import com.github.aakumykov.cc.data_models.CurrencyBoard;

import java.util.List;

public class ConverterDialogFragment extends DialogFragment {

    private static final String TAG = ConverterDialogFragment.class.getSimpleName();
    private List<Currency> mCurrencyList;
    private Spinner mSpinner1;
    private Spinner mSpinner2;
    private CustomArrayAdapter mSpinnerAdapter1;
    private CustomArrayAdapter mSpinnerAdapter2;


    public ConverterDialogFragment(CurrencyBoard currencyBoard) {
        mCurrencyList = currencyBoard.getCurrencyList();

        mCurrencyList.add(0, new Currency(
                "Российский рубль",
                1,
                "RUR",
                1
        ));
    }

    @NonNull @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.converter_dialog, null);

        mSpinnerAdapter1 = new CustomArrayAdapter(view.getContext(), -1, mCurrencyList);
        mSpinnerAdapter2 = new CustomArrayAdapter(view.getContext(), -1, mCurrencyList);

        mSpinner1 = view.findViewById(R.id.firstCurrencySelector);
        mSpinner2 = view.findViewById(R.id.secondCurrencySelector);

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected()");
            }
        });

        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected()");
            }
        });

        mSpinner1.setAdapter(mSpinnerAdapter1);
        mSpinner2.setAdapter(mSpinnerAdapter2);

        mSpinner1.setSelection(findRublePosition(mCurrencyList));
        mSpinner2.setSelection(findDollarPosition(mCurrencyList));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    private int findRublePosition(List<Currency> currencyList) {
        for (int i=0; i<mCurrencyList.size(); i++) {
            Currency currency = mCurrencyList.get(i);
            if ("RUB".equals(currency.getCharCode()))
                return i;
        }
        return 0;
    }

    private int findDollarPosition(List<Currency> currencyList) {
        for (int i=0; i<mCurrencyList.size(); i++) {
            Currency currency = mCurrencyList.get(i);
            if ("USD".equals(currency.getCharCode()))
                return i;
        }
        return 0;
    }

}
