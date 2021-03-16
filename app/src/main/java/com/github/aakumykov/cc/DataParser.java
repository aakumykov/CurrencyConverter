package com.github.aakumykov.cc;

import com.github.aakumykov.cc.data_models.CurrencyBoard;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class DataParser {

    public static CurrencyBoard parseData(String jsonString) throws JsonParseException{
        return new Gson().fromJson(jsonString, CurrencyBoard.class);
    }
}
