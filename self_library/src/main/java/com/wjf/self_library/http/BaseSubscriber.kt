package com.wjf.self_library.http

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.wjf.self_library.BuildConfig
import com.wjf.self_library.common.BaseApplication
import java.net.ConnectException
import java.net.SocketTimeoutException
import rx.Subscriber

/** @author WJF
 */
class BaseSubscriber<T>(val success: ((HttpResult<T>) -> Unit)) : Subscriber<HttpResult<T>>() {
    private val context: Context? by lazy { BaseApplication.context.get() }

    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Log.e(BuildConfig.TAG, "onError{ message: ${e.message} ;e：$e}")
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
