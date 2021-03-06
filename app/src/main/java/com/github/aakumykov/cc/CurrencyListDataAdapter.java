package com.github.aakumykov.cc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakumykov.cc.data_models.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyListDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Currency> mCurrencyList;


    public CurrencyListDataAdapter() {
        mCurrencyList = new ArrayList<>();
    }


    public void setList(List<Currency> currencyList) {
        mCurrencyList.clear();
        mCurrencyList.addAll(currencyList);
        notifyDataSetChanged();
    }


    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CurrencyViewHolder(itemView);
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
