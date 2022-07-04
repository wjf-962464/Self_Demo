package org.jxxy.debug.http

class ErrorResponse(
    val errorType: ErrorType, // 错误类型
    val errorCode: Int, // 错误代码
    override val message: String?, // 错误信息
    val data: Any? = null
) : Exception(message)
