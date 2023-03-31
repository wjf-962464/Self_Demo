package org.jxxy.debug.corekit.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jxxy.debug.corekit.common.BaseApplication
import org.jxxy.debug.corekit.http.HttpManager

fun Context.toast(msg: String?, shortShow: Boolean = true) {
    if (msg.isNullOrEmpty()) {
        return
    }
    if (Looper.myLooper() != Looper.getMainLooper()) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            Toast.makeText(
                this@toast, msg, if (shortShow) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            ).show()
        }
    } else {
        Toast.makeText(
            this, msg, if (shortShow) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).show()
    }
}

fun Context.toast(@StringRes resId: Int?, shortShow: Boolean = true) {
    resId?.let {
        toast(ResourceUtil.getString(resId, shortShow))
    }
}

fun String?.toast(shortShow: Boolean = true) {
    if (this.isNullOrEmpty()) {
        return
    }
    BaseApplication.context().toast(this, shortShow)
}

inline fun <T> T?.nullOrNot(ifNull: () -> Unit, notNull: (T) -> Unit) {
    this?.apply {
        notNull(this)
        return
    }
    ifNull()
}

inline fun <reified C : Activity> Context.startActivity(bundle: Bundle? = null) {
    val intent = Intent(this, C::class.java)
    bundle?.let { intent.putExtras(it) }
    startActivity(intent)
}

fun Context.startActivity(clazz: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, clazz)
    bundle?.let { intent.putExtras(it) }
    startActivity(intent)
}

inline fun <reified S> HttpManager.service(): S {
    return this.service(S::class.java)
}

inline fun <reified T> Any.castToTarget(): T? {
    return this as? T
}
