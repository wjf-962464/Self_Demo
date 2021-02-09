package com.wjf.self_library.util.log;

/**
 * @author sun
 */
public interface LogListener {
    /**
     * debug 日志回调
     *
     * @param tag 标签
     * @param msg 内容
     */
    void v(String tag, String msg);

    /**
     * debug 日志回调
     *
     * @param tag 标签
     * @param msg 内容
     */
    void d(String tag, String msg);

    /**
     * warning 日志回调
     *
     * @param tag 标签
     * @param msg 内容
     */
    void w(String tag, String msg);

    /**
     * info 日志回调
     *
     * @param tag 标签
     * @param msg 内容
     */
    void i(String tag, String msg);

    /**
     * error 日志回调
     *
     * @param tag 标签
     * @param msg 内容
     */
    void e(String tag, String msg);

    /**
     * error 日志回调, 带异常信息
     *
     * @param tag 标签
     * @param msg 内容
     * @param e   异常信息
     */
    void e(String tag, String msg, Exception e);
}
