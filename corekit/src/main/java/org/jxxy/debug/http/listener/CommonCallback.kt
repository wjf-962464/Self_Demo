package org.jxxy.debug.http.listener

import org.jxxy.debug.http.bean.ErrorResponse

interface CommonCallback<Response> {
    fun success(msg: String?, data: Response?)
    fun otherCode(code: Int?, msg: String?, data: Response?)
    fun error(e: ErrorResponse? = null)
}
