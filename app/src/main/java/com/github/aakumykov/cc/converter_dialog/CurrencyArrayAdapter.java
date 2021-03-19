package com.github.aakumykov.cc.converter_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.aakumykov.cc.R;
import com.github.aakumykov.cc.data_models.Currency;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CurrencyArrayAdapter extends ArrayAdapter<Currency> {

    private List<Currency> mStringList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;


    public CurrencyArrayAdapter(@NonNull Context context, int resource, @NonNull List<Currency> objects) {
        super(context, resource, objects);
        mStringList.addAll(objects);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Nullable @Override
    public Currency getItem(int position) {
        return mStringList.get(position);
    }

    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = (null != convertView) ? convertView :
                mLayoutInflater.inflate(R.layout.spinner_item, parent, false);
        fillView(view, position);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = (null != convertView) ? convertView :
                mLayoutInflater.inflate(R.layout.spinner_item, parent, false);
        fillView(view, position);
        return view;
    }

    private void fillView(@NonNull View view, int position) {
        Currency currency = getItem(position);

        TextView titleView = view.findViewById(R.id.titleView);
        ImageView flagView = view.findViewById(R.id.flagView);

        titleView.setText(currency.getName());

        String countryFlagURL = Utils.getCreateCountryFlagURL(currency.getCharCode());

        Picasso.get().load(countryFlagURL).into(flagView);
    }
}
