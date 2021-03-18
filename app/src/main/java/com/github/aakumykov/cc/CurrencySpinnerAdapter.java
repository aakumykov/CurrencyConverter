package com.github.aakumykov.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.aakumykov.cc.data_models.Currency;

import java.util.List;

public class CurrencySpinnerAdapter extends ArrayAdapter<Currency> {

    private List<Currency> mCurrencyList;
    private int mResourceId;

    public CurrencySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Currency> currencyList) {
        super(context, resource, currencyList);

        mResourceId = resource;
        mCurrencyList.addAll(currencyList);
    }

    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Currency currency = mCurrencyList.get(position);

        View itemView = LayoutInflater.from(parent.getContext()).inflate(mResourceId, parent);

        ((TextView) itemView.findViewById(R.id.titleView)).setText(currency.getName());
        ((TextView) itemView.findViewById(R.id.valueView)).setText(String.valueOf(currency.getValue()));

        return itemView;
    }
}
