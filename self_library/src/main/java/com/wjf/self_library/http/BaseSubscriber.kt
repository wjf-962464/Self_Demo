package com.wjf.self_library.http

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.wjf.self_library.BuildConfig
import com.wjf.self_library.common.BaseApplication
import rx.Subscriber
import java.net.ConnectException
import java.net.SocketTimeoutException

/** @author WJF
 */
class BaseSubscriber<T>(private val isLoading: Boolean, val success: ((HttpResult<T>) -> Unit)) :
    Subscriber<HttpResult<T>>() {
    private val context: Context? by lazy { BaseApplication.context.get() }
    override fun onStart() {
        if (isLoading) {
            //显示加载库
            Log.i(BuildConfig.TAG, "need loading")
        }
    }

    override fun onCompleted() {
        if (isLoading) {
            //关闭加载库
            Log.i(BuildConfig.TAG, "close loading")
        }
    }

    override fun onError(e: Throwable) {
        Logger.e(e, "onError{ message: ${e.message} ;e：$e}")
        when (e) {
            is SocketTimeoutException -> {
                Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show()
            }
            is ConnectException -> {
                Toast.makeText(context, "网络繁忙，请稍后再试试吧", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "服务器繁忙，请稍后再试试吧", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNext(t: HttpResult<T>) {
//        Logger.json(Gson().toJson(t))
        success.invoke(t)
    }
}
