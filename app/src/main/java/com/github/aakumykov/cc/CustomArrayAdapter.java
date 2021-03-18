package com.github.aakumykov.cc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.aakumykov.cc.data_models.Currency;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Currency> {

    private List<Currency> mCurrencyList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;


    public CustomArrayAdapter(@NonNull Context context, int folderSpinnerItemResourceId, @NonNull List<Currency> currencyList) {
        super(context, folderSpinnerItemResourceId, currencyList);
        mCurrencyList.addAll(currencyList);
        mLayoutInflater = LayoutInflater.from(context);
    }


    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createNormalFoldedView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createNormalUnfoldedView(position, convertView, parent);
    }

    @Nullable @Override
    public Currency getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return mCurrencyList.size();
    }

    @Override
    public boolean isEnabled(int position) {
        boolean enabled = 0 != position;
        return enabled;
    }

    private void fillView(View view, Currency currency) {
        TextView textView = view.findViewById(R.id.titleView);
        ImageView flagView = view.findViewById(R.id.flagView);

        textView.setText(currency.getName());

        Picasso.get()
                .load(Utils.getCreateCountryFlagURL(currency.getCharCode()))
                .error(R.drawable.ic_country_flag_placeholder)
                .into(flagView);
    }

    private View createNormalFoldedView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = (null != convertView) ?
                convertView :
                mLayoutInflater.inflate(R.layout.spinner_folded_item, parent, false);
        fillView(view, mCurrencyList.get(position));
        return view;
    }

    private View createNormalUnfoldedView(int position, View convertView, ViewGroup parent) {
        View view = (null != convertView) ? convertView
                : mLayoutInflater.inflate(R.layout.spinner_unfolded_item, parent, false);
        fillView(view, mCurrencyList.get(position));
        return view;
    }
}
