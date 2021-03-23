package com.wjf.self_library.util.log;

/**
 * @author sun
 */
public enum LogLevel {

    /** VERBOSE */
    Verbose(0),
    /** DEBUG */
    Debug(1),
    /** INFO */
    Info(2),
    /** WARNING */
    Warning(3),
    /** ERROR */
    Error(4),

    /** NONE, 关闭任何日志信息 */
    None(5);

    private final int value;

    LogLevel(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
