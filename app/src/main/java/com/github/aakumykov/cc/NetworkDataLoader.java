package com.github.aakumykov.cc;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

// TODO: проверять доступность сети
public class NetworkDataLoader {

    public interface iDataFetchCallbacks {
        void onDataFetchSuccess(String jsonString);
        void onDataFetchFailed(String errorMsg);
    }

    public static void fetchData(String url, iDataFetchCallbacks callbacks) {

        new Thread(() -> {

            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                ResponseBody responseBody = response.body();
                if (null != responseBody) {
                    String jsonString = responseBody.string();
                    callbacks.onDataFetchSuccess(jsonString);
                }
                else {
                    callbacks.onDataFetchFailed("Response is null");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

}
