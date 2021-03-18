package com.github.aakumykov.cc;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.Currency;
import com.squareup.picasso.Picasso;

public class CurrencyViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private ImageView mFlagView;
    private TextView mTitleView;
    private TextView mValueView;
    private iItemClickListener mItemClickListener;

    public CurrencyViewHolder(@NonNull View itemView) {
        super(itemView);

        mItemView = itemView.findViewById(R.id.itemView);
        mFlagView = itemView.findViewById(R.id.flagView);
        mTitleView = itemView.findViewById(R.id.titleView);
        mValueView = itemView.findViewById(R.id.valueView);

        mItemView.setOnClickListener(v -> {
            mItemClickListener.onItemClicked(this);
        });
    }

    public void setClickListener(iItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void initialize(Currency currency) {

        String name = currency.getName();
        mTitleView.setText(name);

        String value = String.valueOf(currency.getValue());
        mValueView.setText(value);

        loadCountryFlag(currency.getCharCode());
    }

    private void loadCountryFlag(String charCode) {
        Picasso.get()
            .load(Utils.getCreateCountryFlagURL(charCode))
            .error(R.drawable.ic_country_flag_placeholder)
            .into(mFlagView);
    }
}
