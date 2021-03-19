package com.github.aakumykov.cc.converter_dialog;

import androidx.annotation.NonNull;

public class Utils {
    public static String getCreateCountryFlagURL(@NonNull String countryCode) {
        countryCode = countryCode.substring(0,2).toLowerCase();
        return "https://www.countryflags.io/"+countryCode+"/flat/64.png";
    }
}
