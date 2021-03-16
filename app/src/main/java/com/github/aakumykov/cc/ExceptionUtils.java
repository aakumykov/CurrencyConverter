package com.github.aakumykov.cc;

public class ExceptionUtils {
    public static String getErrorMessage(Exception e) {
        String msg = e.getMessage();
        return (null != msg) ? msg : e.getClass().getSimpleName();
    }
}
