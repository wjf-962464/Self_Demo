package com.wjf.self_library.http

data class HttpRequest(
    val Request_Time: String,
    val Request_Url: String,
    val Request_Method: String,
    val Request_Params: MutableMap<String, String>
)
