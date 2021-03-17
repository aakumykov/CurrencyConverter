package com.github.aakumykov.cc;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CachedDataManager {

    public interface iFileReadCallbacks {
        void onFileReadSuccess(String stringData);
        void onFileReadError(String errorMsg);
    }

    public interface iFileWriteCallbacks {
        void onFileWriteSuccess();
        void onFileWriteError(String errorMsg);
    }


    private static final String TAG = CachedDataManager.class.getSimpleName();

    private final static String FILE_NAME = "currency_board.json";


    public static void readCachedString(@NonNull Context context, iFileReadCallbacks callbacks) {

        if (!cacheFileExists(context)) {
            callbacks.onFileReadError("File '"+FILE_NAME+"' does not exists in cache dir.");
            return;
        }

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(FILE_NAME);
        }
        catch (FileNotFoundException e) {
            onFileReadException(e, callbacks);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            onFileReadException(e, callbacks);
        }
        finally {
            callbacks.onFileReadSuccess(stringBuilder.toString());
        }
    }

    public static void saveStringToCache(String stringData, @NonNull Context context, iFileWriteCallbacks callbacks) {

        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(stringData.getBytes());
        }
        catch (IOException e) {
            onFileWriteException(e, callbacks);
        }
        finally {
            callbacks.onFileWriteSuccess();
        }
    }


    private static boolean cacheFileExists(@NonNull Context context) {
        File file = new File(context.getCacheDir(), FILE_NAME);
        return file.exists();
    }

    private static void onFileReadException(@NonNull Exception e, iFileReadCallbacks callbacks) {
        String errorMsg = ExceptionUtils.getErrorMessage(e);
        Log.e(TAG, errorMsg, e);
        callbacks.onFileReadError(errorMsg);
    }

    private static void onFileWriteException(@NonNull Exception e, iFileWriteCallbacks callbacks) {
        String errorMsg = ExceptionUtils.getErrorMessage(e);
        Log.e(TAG, errorMsg, e);
        callbacks.onFileWriteError(errorMsg);
    }
}
