package com.wjf.self_library.http

data class HttpRequest(
    val time: String,
    val url: String,
    val method: String,
    val params: MutableMap<String, String>
)
