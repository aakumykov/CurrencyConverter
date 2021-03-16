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
        String name = currency.getName();
        mListItemBinding.titleView.setText(name);
//        mListItemBinding.valueView.setText(String.valueOf(currency.getValue()));
        loadCountryFlag(currency.getCharCode());
    }

    private void loadCountryFlag(String charCode) {
        String countryCode = charCode.substring(0,2).toLowerCase();
        String flagImageURL = "https://www.countryflags.io/"+countryCode+"/flat/64.png";

//        Picasso.get().load(flagImageURL).into(mListItemBinding.flagView);
    }
}
