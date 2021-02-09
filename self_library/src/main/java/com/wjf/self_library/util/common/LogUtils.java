package com.wjf.self_library.util.common;


import com.wjf.self_library.util.log.LogHandler;
import com.wjf.self_library.util.log.LogLevel;

/**
 * @author sun
 */
class LogUtils {
    private static final String TAG = "androidUtil";

    public static void error(Exception e) {
        LogHandler.moduleLog(TAG,
                LogLevel.Error,
                TAG,
                e.getMessage(),
                e);
    }
}
