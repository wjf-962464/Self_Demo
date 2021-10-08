package com.wjf.loadLayout.util

import android.os.Looper

object LoadUtil {
    fun isMainThread(): Boolean = Looper.getMainLooper() == Looper.myLooper()
}
