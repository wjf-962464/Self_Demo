package com.wjf.self_library.util.log;

import com.wjf.self_library.BuildConfig;

import java.util.logging.Logger;

public class LogUtil {
    public static String makeLogTag(String tag) {
        return BuildConfig.TAG + tag;
    }

    public static void v(String tag, Class<?> clz, String msg) {
        LogHandler.v(tag, clz.getSimpleName() + "_" + msg);
    }

    public static void i(String tag, Class<?> clz, String msg) {
        LogHandler.i(tag, clz.getSimpleName() + "_" + msg);
    }

    public static void w(String tag, Class<?> clz, String msg) {
        LogHandler.w(tag, clz.getSimpleName() + "_" + msg);
    }

    public static void d(String tag, Class<?> clz, String msg) {
        LogHandler.d(tag, clz.getSimpleName() + "_" + msg);
    }

    public static void e(String tag, Class<?> clz, String msg) {
        LogHandler.e(tag, clz.getSimpleName() + "_" + msg);
    }

    public static void e(String tag, Class<?> clz, String msg, Exception e) {
        LogHandler.e(tag, clz.getSimpleName() + "_" + msg + ", Exception===>" + e.getMessage());
    }
}
