package com.wjf.barcode;

import android.util.Log;

/**
 * @author : Wangjf
 * @date : 2021/4/12
 */
public class Logger {
    public static final String TAG = "WJF_DEBUG";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }
}
