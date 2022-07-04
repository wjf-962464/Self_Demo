package org.jxxy.debug.http

interface CommonCallback<Response> {
    fun success(msg: String?, data: Response?)
    fun otherCode(code: Int?, msg: String?, data: Response?)
    fun error(e: ErrorResponse? = null)
}
