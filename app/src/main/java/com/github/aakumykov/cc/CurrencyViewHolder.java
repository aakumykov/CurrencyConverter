package com.github.aakumykov.cc;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.Currency;
import com.squareup.picasso.Picasso;

public class CurrencyViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mFlagView;
    private final TextView mTitleView;
    private final TextView mValueView;


    public CurrencyViewHolder(@NonNull View itemView) {
        super(itemView);

        mFlagView = itemView.findViewById(R.id.flagView);
        mTitleView = itemView.findViewById(R.id.titleView);
        mValueView = itemView.findViewById(R.id.valueView);
    }


    public void initialize(Currency currency) {

        String name = currency.getName();
        mTitleView.setText(name);

        float value = currency.getValue();
        int nominal = currency.getNominal();
        mValueView.setText( String.valueOf(value / nominal) );

        loadCountryFlag(currency.getCharCode());
    }

    private void loadCountryFlag(String charCode) {
        String countryCode = charCode.substring(0,2).toLowerCase();
        String flagImageURL = "https://www.countryflags.io/"+countryCode+"/flat/64.png";

        Picasso.get()
                .load(flagImageURL)
                .error(R.drawable.ic_flag_placeholder)
                .into(mFlagView);
    }
}
