package org.jxxy.debug.http.bean

class BaseResp<T> {
    val code: Int? = null
    var message: String? = null
    var data: T? = null
}
