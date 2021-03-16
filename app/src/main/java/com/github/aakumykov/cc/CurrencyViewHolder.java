package com.github.aakumykov.cc;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.Currency;
import com.github.aakumykov.cc.databinding.ListItemBinding;

public class CurrencyViewHolder extends RecyclerView.ViewHolder {

    private ListItemBinding mListItemBinding;

    public CurrencyViewHolder(@NonNull ListItemBinding listItemBinding) {
        super(listItemBinding.getRoot());
        mListItemBinding = listItemBinding;
    }

    public void initialize(Currency currency) {
        mListItemBinding.titleView.setText(currency.getName());
        mListItemBinding.valueView.setText(String.valueOf(currency.getValue()));
    }
}
