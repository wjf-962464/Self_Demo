package org.jxxy.debug.corekit.http

import org.jxxy.debug.corekit.mmkv.PersistenceUtil

object TokenManager {
    private const val TOKEN_KEY = "tokenValue"
    private const val TOKEN_NULL = ""

    fun login(tokenValue: String?) {
        PersistenceUtil.putValue(TOKEN_KEY, tokenValue)
    }

    fun getToken(): String? {
        val token: String? = PersistenceUtil.getValue(TOKEN_KEY)
        return if (token.isNullOrEmpty())
            null
        else
            token
    }

    fun loginOut() {
        PersistenceUtil.putValue(TOKEN_KEY, TOKEN_NULL)
    }
}
