package org.jxxy.debug.corekit.http.bean

import androidx.lifecycle.MutableLiveData

class ResLiveData<T> : MutableLiveData<Resource<T>>() {

    val data: T?
        get() = value?.response

    val status: Resource.Status?
        get() = value?.status

    val error: ErrorResponse?
        get() = value?.error

    /**
     * 成功时发送数据
     */
    fun success(data: T) {
        postValue(Resource.success(data))
    }

    /**
     * 错误时发送Throwable
     */
    fun error(error: ErrorResponse, data: T? = null) {
        postValue(Resource.error(error, data))
    }

    fun immediateSuccess(data: T) {
        value = Resource.success(data)
    }
}
