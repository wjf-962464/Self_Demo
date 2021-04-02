package com.wjf.self_library.util.log;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Set;

/**
 * @author sun
 */
public class LogHandler {

    private static final HashMap<String, LogLevel> modelLog = new HashMap<>();
    private static LogLevel logLevel = LogLevel.Verbose;
    private static LogListener logListener;

    private static void log(LogLevel level, String tag, String msg, Exception e) {
        if (level.value() >= logLevel.value() || e != null) {
            switch (level) {
                case Verbose:
                    if (logListener != null) {
                        logListener.v(tag, msg);
                    } else {
                        Log.v(tag, msg);
                    }
                    break;
                case Debug:
                    if (logListener != null) {
                        logListener.d(tag, msg);
                    } else {
                        Log.d(tag, msg);
                    }
                    break;
                case Info:
                    if (logListener != null) {
                        logListener.i(tag, msg);
                    } else {
                        Log.i(tag, msg);
                    }
                    break;
                case Warning:
                    if (logListener != null) {
                        logListener.w(tag, msg);
                    } else {
                        Log.w(tag, msg);
                    }
                    break;
                case Error:
                    if (logListener != null) {
                        if (e != null) {
                            logListener.e(tag, msg, e);
                        } else {
                            logListener.e(tag, msg);
                        }
                    } else {
                        if (e != null) {
                            Log.e(tag, msg, e);
                        } else {
                            Log.e(tag, msg);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 模块统一日志接口, 方便分模块管理日志,
     *
     * @param module 唯一, 模块id
     * @param level 日志级别
     * @param tag 日志标签
     * @param msg 日志内容
     */
    public static void moduleLog(String module, LogLevel level, String tag, String msg) {
        moduleLog(module, level, tag, msg, null);
    }

    /**
     * 模块统一日志接口, 方便分模块管理日志,
     *
     * @param module 唯一, 模块id
     * @param level 日志级别
     * @param tag 日志标签
     * @param msg 日志内容
     * @param e 异常信息
     */
    public static void moduleLog(
            @NonNull String module, LogLevel level, String tag, String msg, Exception e) {
        LogLevel modelLevel = modelLog.get(module);
        if (modelLevel == null) {
            modelLevel = getLogLevel();
            controlModuleLogLevel(module, modelLevel);
        }

        if (level.value() >= modelLevel.value()) {
            log(level, tag, msg, e);
        } else if (e != null || level.value() == LogLevel.Error.value()) {
            log(level, tag, msg, e);
        }
    }

    public static Set<String> getLogModules() {
        return modelLog.keySet();
    }

    /**
     * 控制模块日志是否开启, 默认状态下所有模块都是开启的
     *
     * @param module 模块名
     * @param enable 开启状态
     */
    public static synchronized void controlModuleLog(@NonNull String module, boolean enable) {
        if (enable) {
            controlModuleLogLevel(module, getLogLevel());
        } else {
            controlModuleLogLevel(module, LogLevel.None);
        }
    }

    /**
     * 控制模块日志是否开启, 默认状态下所有模块都是开启的
     *
     * @param module 模块名
     * @param level 模块日志级别
     */
    public static void controlModuleLogLevel(@NonNull String module, LogLevel level) {
        modelLog.put(module, level);
    }

    public static void d(String tag, String msg) {
        log(LogLevel.Debug, tag, msg, null);
    }

    public static void w(String tag, String msg) {
        log(LogLevel.Warning, tag, msg, null);
    }

    public static void i(String tag, String msg) {
        log(LogLevel.Info, tag, msg, null);
    }

    public static void v(String tag, String msg) {
        log(LogLevel.Verbose, tag, msg, null);
    }

    public static void e(String tag, String msg) {
        log(LogLevel.Error, tag, msg, null);
    }

    public static void e(String tag, Exception e) {
        log(LogLevel.Error, tag, e.getMessage(), null);
    }

    public static LogLevel getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(LogLevel logLevel) {
        LogHandler.logLevel = logLevel;
    }

    public static void setLogListener(LogListener logListener) {
        LogHandler.logListener = logListener;
    }
}
