package org.jxxy.debug.corekit.http

import android.content.Context

interface TokenManagerCallback {
    fun gotoLoginPage(context: Context)
    fun judgeLoginPage(context: Context): Boolean
}
