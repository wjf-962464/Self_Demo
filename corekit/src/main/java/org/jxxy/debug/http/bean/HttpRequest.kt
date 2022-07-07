package org.jxxy.debug.http.bean

class HttpRequest(
    val requestTime: String,
    val requestUrl: String,
    val requestMethod: String,
    val requestParams: MutableMap<String, String>
)
