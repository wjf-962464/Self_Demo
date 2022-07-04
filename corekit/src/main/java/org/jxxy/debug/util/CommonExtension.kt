package org.jxxy.debug.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import org.jxxy.debug.common.BaseApplication

fun Context.toast(msg: String?, shortShow: Boolean = true) {
    if (msg.isNullOrEmpty()) {
        return
    }
    Toast.makeText(this, msg, if (shortShow) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}

fun Context.toast(@StringRes resId: Int?, shortShow: Boolean = true) {
    resId?.let {
        Toast.makeText(this, it, if (shortShow) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }
}

fun String?.toast(shortShow: Boolean = true) {
    if (this.isNullOrEmpty()) {
        return
    }
    Toast.makeText(
        BaseApplication.context(),
        this,
        if (shortShow) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    ).show()
}

inline fun <T> T?.nullOrNot(ifNull: () -> Unit, notNull: (T) -> Unit) {
    this?.apply {
        notNull(this)
        return
    }
    ifNull()
}
