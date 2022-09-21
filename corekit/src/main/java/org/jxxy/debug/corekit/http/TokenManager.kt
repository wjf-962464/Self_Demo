package org.jxxy.debug.corekit.http

import android.content.Context
import org.jxxy.debug.corekit.mmkv.PersistenceUtil
import java.lang.ref.WeakReference

object TokenManager {
    private const val TOKEN_KEY = "tokenValue"
    private const val TOKEN_NULL = ""
    private var tokenTemp: String? = null
    private var tokenManagerCallback: TokenManagerCallback? = null

    fun login(tokenValue: String?) {
        tokenTemp = tokenValue
        PersistenceUtil.putValue(TOKEN_KEY, tokenValue)
        CustomHttpHeaderUtil.updateHeader()
    }

    fun getToken(): String? {
        val token: String? = PersistenceUtil.getValue(TOKEN_KEY)
        return when {
            tokenTemp.isNullOrEmpty().not() -> tokenTemp
            token.isNullOrEmpty().not() -> token
            else -> null
        }
    }

    fun loginOut() {
        tokenTemp = null
        PersistenceUtil.putValue(TOKEN_KEY, TOKEN_NULL)
        CustomHttpHeaderUtil.updateHeader()
    }

    fun gotoLogin() {
        pageContext?.get()?.let {
            tokenManagerCallback?.apply {
                if (judgeLoginPage(it).not()) {
                    gotoLoginPage(it)
                }
            }
        }
    }

    fun init(tokenManagerCallback: TokenManagerCallback) {
        TokenManager.tokenManagerCallback = tokenManagerCallback
    }

    fun register(context: Context) {
        if (pageContext?.get() == null) {
            pageContext?.clear()
        }
        pageContext = WeakReference(context)
    }

    fun unRegister(context: Context) {
        if (pageContext?.get() == context) {
            pageContext?.clear()
        }
    }

    private var pageContext: WeakReference<Context>? = null
}
