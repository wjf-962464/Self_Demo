package com.wjf.self_library.http

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.wjf.self_library.BuildConfig
import com.wjf.self_library.common.BaseApplication
import org.jxxy.debug.corekit.util.toast
import java.net.ConnectException
import java.net.SocketTimeoutException
import rx.Subscriber

/** @author WJF
 */
class BaseSubscriber<T>(private val isLoading: Boolean, val success: ((HttpResult<T>) -> Unit)) :
    Subscriber<HttpResult<T>>() {
    override fun onStart() {
        if (isLoading) {
            // 显示加载库
            Log.i(BuildConfig.TAG, "need loading")
        }
    }

    override fun onCompleted() {
        if (isLoading) {
            // 关闭加载库
            Log.i(BuildConfig.TAG, "close loading")
        }
    }

    override fun onError(e: Throwable) {
        Logger.e(e, "onError{ message: ${e.message} ;e：$e}")
        when (e) {
            is SocketTimeoutException -> {
                "请检查您的网络".toast()
            }
            is ConnectException -> {
                "网络繁忙，请稍后再试试吧".toast()
            }
            else -> {
                "服务器繁忙，请稍后再试试吧".toast()
            }
        }
    }

    override fun onNext(t: HttpResult<T>) {
//        Logger.json(Gson().toJson(t))
        success.invoke(t)
    }
}
