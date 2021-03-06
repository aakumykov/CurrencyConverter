package com.github.aakumykov.cc.converter_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.github.aakumykov.cc.R;
import com.github.aakumykov.cc.data_models.Currency;

import java.util.ArrayList;
import java.util.List;

public class ConverterDialogFragment extends DialogFragment {

    private static final String TAG = ConverterDialogFragment.class.getSimpleName();
    private static final String ENTERED_NUMBER = "ENTERED_NUMBER";
    private static final String CURRENCY_LIST = "CURRENCY_LIST";

    private final ArrayList<Currency> mCurrencyList;

    private Spinner mSpinner1;
    private Spinner mSpinner2;

    private EditText mNumberInput;
    private TextView mConversionResultView;

    private Float mEnteredNumber;
    private Currency mSelectedCurrency1;
    private Currency mSelectedCurrency2;


    public ConverterDialogFragment(List<Currency> currencyList) {
        mCurrencyList = new ArrayList<>(currencyList);
    }


    @NonNull @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (null != savedInstanceState) {
            mEnteredNumber = savedInstanceState.getFloat(ENTERED_NUMBER);
            mCurrencyList.addAll(savedInstanceState.getParcelableArrayList(CURRENCY_LIST));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);

        mConversionResultView = view.findViewById(R.id.conversionResultView);

        mNumberInput = view.findViewById(R.id.numberInput);
        mNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String text = s.toString();

                    if (TextUtils.isEmpty(text))
                        mEnteredNumber = null;
                    else {
                        text = text.replaceAll(",", ".");
                        mEnteredNumber = Float.parseFloat(text);
                    }
                }
                catch (Exception e) {
                    mEnteredNumber = -1F;
                }

                onConverterValuesChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, s.toString());
            }
        });

        view.findViewById(R.id.swapValuesWidget).setOnClickListener(v -> {
            swapCurrencyValues();
        });

        ArrayAdapter<Currency> arrayAdapter = new CurrencyArrayAdapter(getContext(), -1, mCurrencyList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner1 = view.findViewById(R.id.spinner1);
        mSpinner2 = view.findViewById(R.id.spinner2);

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCurrency1 = mCurrencyList.get(position);
                onConverterValuesChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedCurrency1 = null;
            }
        });

        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCurrency2 = mCurrencyList.get(position);
                onConverterValuesChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedCurrency2 = null;
            }
        });

        mSpinner1.setAdapter(arrayAdapter);
        mSpinner2.setAdapter(arrayAdapter);

        preselectUsualCurrencies();

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (null != mEnteredNumber)
            outState.putFloat(ENTERED_NUMBER, mEnteredNumber);

        outState.putParcelableArrayList(CURRENCY_LIST, mCurrencyList);
    }


    public void onConverterValuesChanged() {
        String displayedResult;

        if (null == mSelectedCurrency1 || null == mSelectedCurrency2)
            return;

        String placeholderText = getString(R.string.result_view_placeholder);

        if (null == mEnteredNumber) {
            displayedResult = placeholderText;
        }
        else if (mEnteredNumber > 0) {
            displayedResult = makeConvertion();
        }
        else if (0 == mEnteredNumber) {
            displayedResult = "0";
        }
        else if (-1 == mEnteredNumber) {
            displayedResult = getString(R.string.number_error);
        }
        else {
            displayedResult = placeholderText;
        }

        mConversionResultView.setText(displayedResult);
    }

    private String makeConvertion() {

        float value1 = mSelectedCurrency1.getValue() / mSelectedCurrency1.getNominal();

        float value2 = mSelectedCurrency2.getValue() / mSelectedCurrency2.getNominal();

        float result = (mEnteredNumber * value1) / value2;

//        int resultInt = Math.round(result * 10000);
//        result = resultInt / 10000F;

        return String.valueOf(result);
    }

    private void swapCurrencyValues() {
        int oldPosition1 = mSpinner1.getSelectedItemPosition();
        mSpinner1.setSelection(mSpinner2.getSelectedItemPosition());
        mSpinner2.setSelection(oldPosition1);

        mNumberInput.setText(mConversionResultView.getText());

        onConverterValuesChanged();
    }

    private void preselectUsualCurrencies() {
        int rubIndex = findCurrencyIndex("RUB");
        if (rubIndex >= 0)
            mSpinner1.setSelection(rubIndex);

        int usdIndex = findCurrencyIndex("USD");
        if (usdIndex >= 0)
            mSpinner2.setSelection(usdIndex);
    }

    private int findCurrencyIndex(String charCode) {
        for (int i=0; i<mCurrencyList.size(); i++) {
            Currency currency = mCurrencyList.get(i);
            if (charCode.equals(currency.getCharCode()))
                return i;
        }
        return -1;
    }
}
