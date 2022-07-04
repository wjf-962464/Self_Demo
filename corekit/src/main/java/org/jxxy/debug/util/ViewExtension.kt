package org.jxxy.debug.util

import android.view.View

fun View?.show() {
    if (this?.isShown == false) {
        this.visibility = View.VISIBLE
    }
}

fun View?.hide() {
    if (this?.isShown == true) {
        this.visibility = View.INVISIBLE
    }
}

fun View?.gone() {
    if (this?.isShown == true) {
        this.visibility = View.GONE
    }
}

inline fun <T : View> T.singleClick(time: Long = 500, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        val interval = currentTimeMillis - lastClickTime
        if (interval > time || interval < 0) {
            // 小于0是为了规避用户往前调手机时间
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(Int.MAX_VALUE, value)
    get() = getTag(Int.MAX_VALUE) as? Long ?: 0
