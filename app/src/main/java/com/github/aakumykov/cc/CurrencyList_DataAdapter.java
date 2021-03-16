package com.github.aakumykov.cc;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.Currency;
import com.github.aakumykov.cc.databinding.ListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CurrencyList_DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Currency> mCurrencyList;


    public CurrencyList_DataAdapter() {
        mCurrencyList = new ArrayList<>();
    }


    public void setList(List<Currency> currencyList) {
        mCurrencyList.clear();
        mCurrencyList.addAll(currencyList);
        notifyDataSetChanged();
    }


    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding listItemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CurrencyViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Currency currency = mCurrencyList.get(position);
        CurrencyViewHolder currencyViewHolder = (CurrencyViewHolder) holder;
        currencyViewHolder.initialize(currency);
    }

    @Override
    public int getItemCount() {
        return mCurrencyList.size();
    }
}
