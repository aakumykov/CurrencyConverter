package com.github.aakumykov.cc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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

    private List<Currency> mCurrencyList;
    private Spinner mSpinner1;
    private Spinner mSpinner2;
    private CustomArrayAdapter mSpinnerAdapter1;
    private CustomArrayAdapter mSpinnerAdapter2;


    public ConverterDialogFragment(CurrencyBoard currencyBoard) {
        mCurrencyList = currencyBoard.getCurrencyList();
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
                if (0 == position)
                    mSpinnerAdapter1.setManualSelectionMode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position)
                    mSpinnerAdapter2.setManualSelectionMode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinner1.setAdapter(mSpinnerAdapter1);
        mSpinner2.setAdapter(mSpinnerAdapter2);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

}
