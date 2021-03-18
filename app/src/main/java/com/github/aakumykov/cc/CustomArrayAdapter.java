package com.github.aakumykov.cc;

import android.content.Context;
import android.view.KeyEvent;
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
        if (0 == position)
            return createBlankFoldedView(position, convertView, parent);
        else {
            position = position - 1;
            return createNormalFoldedView(position, convertView, parent);
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Currency country = getItem(position);
        if (null == country)
            return createBlankUnfoldedView(position, convertView, parent);
        else
            return createNormalUnfoldedView(position, country, convertView, parent);
    }

    @Nullable @Override
    public Currency getItem(int position) {
        if (0 == position)
            return null;
        return super.getItem(position - 1);
    }

    @Override
    public int getCount() {
        return mCurrencyList.size() + 1;
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

    private View createBlankFoldedView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.spinner_blank_folded_item, parent, false);
        return view;
    }

    private View createBlankUnfoldedView(int position, View convertView, ViewGroup parent) {
        if (null != convertView) {
            return convertView;
        }
        else {
            View view = mLayoutInflater.inflate(R.layout.spinner_blank_unfolded_item, parent, false);
            view.setOnClickListener(v -> {
                View rootView = parent.getRootView();
                rootView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                rootView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            });
            return view;
        }
    }

    private View createNormalFoldedView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = (null != convertView) ?
                convertView :
                mLayoutInflater.inflate(R.layout.spinner_folded_item, parent, false);
        fillView(view, mCurrencyList.get(position));
        return view;
    }

    private View createNormalUnfoldedView(int position, Currency country, View convertView, ViewGroup parent) {
        View view = (null != convertView) ? convertView
                : mLayoutInflater.inflate(R.layout.spinner_unfolded_item, parent, false);
        fillView(view, country);
        return view;
    }
}
