package org.jxxy.debug.corekit.http.bean

class Resource<out T>(val status: Status, val response: T?, val error: ErrorResponse? = null) {
    companion object {
        fun <T> success(response: T): Resource<T> {
            return Resource(Status.SUCCESS, response, null)
        }

        fun <T> error(error: ErrorResponse, response: T?): Resource<T> {
            return Resource(Status.ERROR, response, error)
        }
    }

    fun onSuccess(onSuccess: (T?) -> Unit): Resource<T> {
        if (this.status == Status.SUCCESS)
            onSuccess(response)
        return this
    }

    fun onError(onError: (error: ErrorResponse?, response: T?) -> Unit): Resource<T> {
        if (status == Status.ERROR)
            onError(error, response)
        return this
    }

    enum class Status {
        SUCCESS,
        ERROR
    }
}
