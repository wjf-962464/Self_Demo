package org.jxxy.debug.http

import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.*
import org.jxxy.debug.R
import org.jxxy.debug.util.ResourceUtil
import org.jxxy.debug.util.toast
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.UnknownHostException

fun <T, D> BaseResp<D>.process(
    resLiveData: ResLiveData<T>,
    callback: LiveDataCallback<T, D>
) {
    if (this.code == 0) {
        callback.success(resLiveData, this.message, this.data)
    } else {
        callback.otherCode(resLiveData, this.code, this.message, this.data)
    }
}

fun <D> BaseResp<D>.process(
    callback: CommonCallback<D>
) {
    if (this.code == 0) {
        callback.success(this.message, this.data)
    } else {
        callback.otherCode(this.code, this.message, this.data)
    }
}

inline fun <D : Any> CoroutineScope.request(
    crossinline block: suspend () -> BaseResp<D>?,
    callback: CommonCallback<D>,
) {
    this.launch(exceptionHandler(callback) + Dispatchers.IO) {
        val response = async {
            block.invoke()
        }
        response.await()?.process(callback)
    }
}

inline fun <T, D : Any> BaseViewModel.request(
    resLiveData: ResLiveData<T>,
    crossinline block: suspend () -> BaseResp<D>?,
    callback: LiveDataCallback<T, D>
) {
    viewModelScope.launch(exceptionHandler(resLiveData, callback) + Dispatchers.IO) {
        val response = async {
            block.invoke()
        }
        response.await()?.process(resLiveData, callback)
    }
}

inline fun BaseViewModel.post(crossinline block: () -> Unit) {
    viewModelScope.launch(Dispatchers.Main.immediate) {
        block.invoke()
    }
}

fun <T, D> exceptionHandler(
    resLiveData: ResLiveData<T>,
    callback: LiveDataCallback<T, D>?
): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        callback?.error(resLiveData, getErrorResponse(e))
    }
}

fun <D> exceptionHandler(
    callback: CommonCallback<D>?
): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        callback?.error(getErrorResponse(e))
    }
}

private fun getErrorResponse(e: Throwable): ErrorResponse {
    val response = when (e) {
        is ErrorResponse -> e

        is ConnectException, is UnknownHostException -> {
            // 网络错误
            ErrorResponse(
                ErrorType.NETWORK_ERROR,
                -1,
                ResourceUtil.getString(R.string.network_error_retry_hint)
            )
        }
        is HttpException -> {
            // 服务器异常
            ErrorResponse(
                ErrorType.SERVICE_ERROR,
                e.code(),
                ResourceUtil.getString(R.string.network_error_retry_timeout)
            )
        }
        is JsonParseException, is MalformedJsonException, is EOFException -> {
            // 其它异常
            ErrorResponse(
                ErrorType.OTHER_ERROR,
                -2, ResourceUtil.getString(R.string.network_unknow_error)
            )
        }
        else -> {
            // 其它异常
            ErrorResponse(
                ErrorType.OTHER_ERROR,
                -3,
                ResourceUtil.getString(R.string.network_unknow_error)
            )
        }
    }
    response.message.toast()
    return response
}