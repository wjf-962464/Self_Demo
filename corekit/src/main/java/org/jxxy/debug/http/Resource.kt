package org.jxxy.debug.http

class Resource<out T>(val status: Status, val response: T, val error: ErrorResponse? = null) {
    companion object {
        fun <T> success(response: T): Resource<T> {
            return Resource(Status.SUCCESS, response, null)
        }

        fun <T> error(error: ErrorResponse, response: T): Resource<T> {
            return Resource(Status.ERROR, response, error)
        }
    }

    fun onSuccess(onSuccess: (T) -> Unit): Resource<T> {
        return if (this.status == Status.SUCCESS) {
            this.also { onSuccess(this.response) }
        } else {
            this
        }
    }

    fun onError(onError: (error: ErrorResponse?) -> Unit): Resource<T> {
        return if (this.status == Status.ERROR) {
            this.also { onError(error) }
        } else {
            this
        }
    }

    enum class Status {
        SUCCESS,
        ERROR
    }
}
