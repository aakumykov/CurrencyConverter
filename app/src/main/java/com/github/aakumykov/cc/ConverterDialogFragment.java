package com.github.aakumykov.cc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.aakumykov.cc.data_models.Currency;
import com.github.aakumykov.cc.data_models.CurrencyBoard;

import java.util.List;

public class ConverterDialogFragment extends DialogFragment {

    private List<Currency> mCurrencyList;
    private Spinner mSpinner1;
    private Spinner mSpinner2;
    private CustomArrayAdapter mSpinnerAdapter;


    public ConverterDialogFragment(CurrencyBoard currencyBoard) {
        mCurrencyList = currencyBoard.getCurrencyList();
    }

    @NonNull @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.converter_dialog, null);

        mSpinner1 = view.findViewById(R.id.firstCurrencySelector);
        mSpinner2 = view.findViewById(R.id.secondCurrencySelector);

        mSpinnerAdapter = new CustomArrayAdapter(view.getContext(), -1, mCurrencyList);

        mSpinner1.setAdapter(mSpinnerAdapter);
        mSpinner2.setAdapter(mSpinnerAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}
