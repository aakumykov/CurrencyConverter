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

    private List<Currency> mStringList = new ArrayList<>();

    private ArrayAdapter mArrayAdapter1;
    private ArrayAdapter mArrayAdapter2;

    private Spinner mSpinner1;
    private Spinner mSpinner2;

    private EditText mNumberInput;
    private View mSwapValuesWidget;
    private TextView mConversionResultView;

    private Float mEnteredNumber;
    private Currency mSelectedCurrency1;
    private Currency mSelectedCurrency2;


    public ConverterDialogFragment(List<Currency> stringList) {
        mStringList.clear();
        mStringList.addAll(stringList);
    }


    @NonNull @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (null != savedInstanceState) {
            mEnteredNumber = savedInstanceState.getFloat(ENTERED_NUMBER);
        }

        // TODO: попробовать platform-версию AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);

        mNumberInput = view.findViewById(R.id.numberInput);
        mSwapValuesWidget = view.findViewById(R.id.swapValuesWidget);
        mConversionResultView = view.findViewById(R.id.conversionResultView);

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

                performConvertion();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, s.toString());
            }
        });

        mSwapValuesWidget.setOnClickListener(v -> {
            swapCurrencyValues();
        });

        mArrayAdapter1 = new CurrencyArrayAdapter(getContext(), -1, mStringList);
        mArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mArrayAdapter2 = new CurrencyArrayAdapter(getContext(), -1, mStringList);
        mArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner1 = view.findViewById(R.id.spinner1);
        mSpinner2 = view.findViewById(R.id.spinner2);

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCurrency1 = mStringList.get(position);
                performConvertion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedCurrency1 = null;
            }
        });

        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCurrency2 = mStringList.get(position);
                performConvertion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedCurrency2 = null;
            }
        });

        mSpinner1.setAdapter(mArrayAdapter1);
        mSpinner2.setAdapter(mArrayAdapter1);

//        mSpinner2.setSelection(2);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mEnteredNumber)
            outState.putFloat(ENTERED_NUMBER, mEnteredNumber);
    }


    public void performConvertion() {
        String displayedResult;

        if (null == mSelectedCurrency1 || null == mSelectedCurrency2)
            return;

        if (null == mEnteredNumber) {
            displayedResult = "";
        }
        else if (mEnteredNumber > 0) {

            float value1 = mSelectedCurrency1.getValue() / mSelectedCurrency1.getNominal();
            float value2 = mSelectedCurrency2.getValue() / mSelectedCurrency1.getNominal();
            float result = (mEnteredNumber * value1) / value2;

//            int resultInt = Math.round(result * 10000);
//            float result = resultInt / 10000F;

            displayedResult = String.valueOf(result);
        }
        else if (0 == mEnteredNumber) {
            displayedResult = "0";
        }
        else if (-1 == mEnteredNumber) {
            displayedResult = getString(R.string.number_error);
        }
        else {
            displayedResult = "";
        }

        mConversionResultView.setText(displayedResult);
    }


    private void swapCurrencyValues() {
        int oldPosition1 = mSpinner1.getSelectedItemPosition();
        mSpinner1.setSelection(mSpinner2.getSelectedItemPosition());
        mSpinner2.setSelection(oldPosition1);

        mNumberInput.setText(mConversionResultView.getText());

        performConvertion();
    }
}
