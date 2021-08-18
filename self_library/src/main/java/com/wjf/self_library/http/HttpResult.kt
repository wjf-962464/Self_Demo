package com.wjf.self_library.http

import com.google.gson.Gson
import java.io.Serializable

/**
 * @author WJF
 */
data class HttpResult<T>(
    var resultCode: Int,
    var message: String,
    var data: T
) : Serializable {
    override fun toString(): String {
        return "resultCode：$resultCode\nmessage：$message\ndata：${Gson().toJson(data)}"
    }
}
