package com.wjf.loadLayout.util

import android.os.Handler
import android.os.Looper

object LoadUtil {
    fun isMainThread(): Boolean = Looper.getMainLooper() == Looper.myLooper()

    inline fun doInMainThread(handler: Handler, crossinline block: () -> Unit) {
        if (isMainThread()) {
            block()
        } else {
            handler.post { block() }
        }
    }
}
