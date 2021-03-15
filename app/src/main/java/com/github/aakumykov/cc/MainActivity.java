package com.github.aakumykov.cc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.aakumykov.cc.databinding.ActivityMainBinding;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private transient ActivityMainBinding mViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        mViewBinding.swipeRefreshLayout.setOnRefreshListener(() -> {

        });

        Gson gson = new Gson();

        String jsonString = "{\"Date\": \"2021-03-16T11:30:00+03:00\",\"PreviousDate\": \"2021-03-13T11:30:00+03:00\",\"PreviousURL\": \"\\/\\/www.cbr-xml-daily.ru\\/archive\\/2021\\/03\\/13\\/daily_json.js\",\"Timestamp\": \"2021-03-15T17:00:00+03:00\",\"Valute\": {\"AUD\": {\"ID\": \"R01010\",\"NumCode\": \"036\",\"CharCode\": \"AUD\",\"Nominal\": 1,\"Name\": \"Австралийский доллар\",\"Value\": 56.7033,\"Previous\": 56.9982},\"AZN\": {\"ID\": \"R01020A\",\"NumCode\": \"944\",\"CharCode\": \"AZN\",\"Nominal\": 1,\"Name\": \"Азербайджанский манат\",\"Value\": 43.1028,\"Previous\": 43.2655},\"GBP\": {\"ID\": \"R01035\",\"NumCode\": \"826\",\"CharCode\": \"GBP\",\"Nominal\": 1,\"Name\": \"Фунт стерлингов Соединенного королевства\",\"Value\": 102.0337,\"Previous\": 102.4997},\"AMD\": {\"ID\": \"R01060\",\"NumCode\": \"051\",\"CharCode\": \"AMD\",\"Nominal\": 100,\"Name\": \"Армянских драмов\",\"Value\": 13.8828,\"Previous\": 13.9418},\"BYN\": {\"ID\": \"R01090B\",\"NumCode\": \"933\",\"CharCode\": \"BYN\",\"Nominal\": 1,\"Name\": \"Белорусский рубль\",\"Value\": 28.2399,\"Previous\": 28.3509},\"BGN\": {\"ID\": \"R01100\",\"NumCode\": \"975\",\"CharCode\": \"BGN\",\"Nominal\": 1,\"Name\": \"Болгарский лев\",\"Value\": 44.6725,\"Previous\": 44.8795},\"BRL\": {\"ID\": \"R01115\",\"NumCode\": \"986\",\"CharCode\": \"BRL\",\"Nominal\": 1,\"Name\": \"Бразильский реал\",\"Value\": 13.1899,\"Previous\": 13.2777},\"HUF\": {\"ID\": \"R01135\",\"NumCode\": \"348\",\"CharCode\": \"HUF\",\"Nominal\": 100,\"Name\": \"Венгерских форинтов\",\"Value\": 23.8136,\"Previous\": 24.0023},\"HKD\": {\"ID\": \"R01200\",\"NumCode\": \"344\",\"CharCode\": \"HKD\",\"Nominal\": 10,\"Name\": \"Гонконгских долларов\",\"Value\": 94.3136,\"Previous\": 94.694},\"DKK\": {\"ID\": \"R01215\",\"NumCode\": \"208\",\"CharCode\": \"DKK\",\"Nominal\": 1,\"Name\": \"Датская крона\",\"Value\": 11.7498,\"Previous\": 11.8023},\"USD\": {\"ID\": \"R01235\",\"NumCode\": \"840\",\"CharCode\": \"USD\",\"Nominal\": 1,\"Name\": \"Доллар США\",\"Value\": 73.2317,\"Previous\": 73.5081},\"EUR\": {\"ID\": \"R01239\",\"NumCode\": \"978\",\"CharCode\": \"EUR\",\"Nominal\": 1,\"Name\": \"Евро\",\"Value\": 87.3508,\"Previous\": 87.7981},\"INR\": {\"ID\": \"R01270\",\"NumCode\": \"356\",\"CharCode\": \"INR\",\"Nominal\": 10,\"Name\": \"Индийских рупий\",\"Value\": 10.1029,\"Previous\": 10.1049},\"KZT\": {\"ID\": \"R01335\",\"NumCode\": \"398\",\"CharCode\": \"KZT\",\"Nominal\": 100,\"Name\": \"Казахстанских тенге\",\"Value\": 17.4756,\"Previous\": 17.5378},\"CAD\": {\"ID\": \"R01350\",\"NumCode\": \"124\",\"CharCode\": \"CAD\",\"Nominal\": 1,\"Name\": \"Канадский доллар\",\"Value\": 58.7499,\"Previous\": 58.5116},\"KGS\": {\"ID\": \"R01370\",\"NumCode\": \"417\",\"CharCode\": \"KGS\",\"Nominal\": 100,\"Name\": \"Киргизских сомов\",\"Value\": 86.3305,\"Previous\": 86.6565},\"CNY\": {\"ID\": \"R01375\",\"NumCode\": \"156\",\"CharCode\": \"CNY\",\"Nominal\": 1,\"Name\": \"Китайский юань\",\"Value\": 11.2612,\"Previous\": 11.3015},\"MDL\": {\"ID\": \"R01500\",\"NumCode\": \"498\",\"CharCode\": \"MDL\",\"Nominal\": 10,\"Name\": \"Молдавских леев\",\"Value\": 41.577,\"Previous\": 41.4714},\"NOK\": {\"ID\": \"R01535\",\"NumCode\": \"578\",\"CharCode\": \"NOK\",\"Nominal\": 10,\"Name\": \"Норвежских крон\",\"Value\": 86.7356,\"Previous\": 86.8767},\"PLN\": {\"ID\": \"R01565\",\"NumCode\": \"985\",\"CharCode\": \"PLN\",\"Nominal\": 1,\"Name\": \"Польский злотый\",\"Value\": 19.0673,\"Previous\": 19.1328},\"RON\": {\"ID\": \"R01585F\",\"NumCode\": \"946\",\"CharCode\": \"RON\",\"Nominal\": 1,\"Name\": \"Румынский лей\",\"Value\": 17.8858,\"Previous\": 17.9731},\"XDR\": {\"ID\": \"R01589\",\"NumCode\": \"960\",\"CharCode\": \"XDR\",\"Nominal\": 1,\"Name\": \"СДР (специальные права заимствования)\",\"Value\": 104.591,\"Previous\": 105.2349},\"SGD\": {\"ID\": \"R01625\",\"NumCode\": \"702\",\"CharCode\": \"SGD\",\"Nominal\": 1,\"Name\": \"Сингапурский доллар\",\"Value\": 54.4231,\"Previous\": 54.6326},\"TJS\": {\"ID\": \"R01670\",\"NumCode\": \"972\",\"CharCode\": \"TJS\",\"Nominal\": 10,\"Name\": \"Таджикских сомони\",\"Value\": 64.2665,\"Previous\": 64.5091},\"TRY\": {\"ID\": \"R01700J\",\"NumCode\": \"949\",\"CharCode\": \"TRY\",\"Nominal\": 10,\"Name\": \"Турецких лир\",\"Value\": 96.8596,\"Previous\": 97.1584},\"TMT\": {\"ID\": \"R01710A\",\"NumCode\": \"934\",\"CharCode\": \"TMT\",\"Nominal\": 1,\"Name\": \"Новый туркменский манат\",\"Value\": 20.9533,\"Previous\": 21.0324},\"UZS\": {\"ID\": \"R01717\",\"NumCode\": \"860\",\"CharCode\": \"UZS\",\"Nominal\": 10000,\"Name\": \"Узбекских сумов\",\"Value\": 69.6191,\"Previous\": 69.9174},\"UAH\": {\"ID\": \"R01720\",\"NumCode\": \"980\",\"CharCode\": \"UAH\",\"Nominal\": 10,\"Name\": \"Украинских гривен\",\"Value\": 26.4088,\"Previous\": 26.5066},\"CZK\": {\"ID\": \"R01760\",\"NumCode\": \"203\",\"CharCode\": \"CZK\",\"Nominal\": 10,\"Name\": \"Чешских крон\",\"Value\": 33.4041,\"Previous\": 33.4797},\"SEK\": {\"ID\": \"R01770\",\"NumCode\": \"752\",\"CharCode\": \"SEK\",\"Nominal\": 10,\"Name\": \"Шведских крон\",\"Value\": 86.0466,\"Previous\": 86.5188},\"CHF\": {\"ID\": \"R01775\",\"NumCode\": \"756\",\"CharCode\": \"CHF\",\"Nominal\": 1,\"Name\": \"Швейцарский франк\",\"Value\": 78.8031,\"Previous\": 79.075},\"ZAR\": {\"ID\": \"R01810\",\"NumCode\": \"710\",\"CharCode\": \"ZAR\",\"Nominal\": 10,\"Name\": \"Южноафриканских рэндов\",\"Value\": 49.0855,\"Previous\": 49.0872},\"KRW\": {\"ID\": \"R01815\",\"NumCode\": \"410\",\"CharCode\": \"KRW\",\"Nominal\": 1000,\"Name\": \"Вон Республики Корея\",\"Value\": 64.5298,\"Previous\": 64.7084},\"JPY\": {\"ID\": \"R01820\",\"NumCode\": \"392\",\"CharCode\": \"JPY\",\"Nominal\": 100,\"Name\": \"Японских иен\",\"Value\": 67.105,\"Previous\": 67.3552}}}";
        CurrencyBoard currencyBoard = gson.fromJson(jsonString, CurrencyBoard.class);
        Log.d("1", String.valueOf(currencyBoard));

        mViewBinding.errorView.setText("Число валют: "+currencyBoard.getValuteCount());
        mViewBinding.errorView.setVisibility(View.VISIBLE);
    }
}