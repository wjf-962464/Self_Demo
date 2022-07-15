package org.jxxy.debug.corekit.http.bean

class BaseResp<T>(
    val code: Int? = null,
    val message: String? = null,
    val data: T? = null,
)
