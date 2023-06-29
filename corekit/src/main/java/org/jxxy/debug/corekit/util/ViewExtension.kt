package org.jxxy.debug.corekit.util

import android.graphics.Rect
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import org.jxxy.debug.corekit.common.BaseApplication

fun View?.isShowing(): Boolean = this?.visibility == View.VISIBLE

fun View?.show() {
    if (this?.visibility != View.VISIBLE) {
        this?.visibility = View.VISIBLE
    }
}

fun View?.hide() {
    if (this?.visibility != View.INVISIBLE) {
        this?.visibility = View.INVISIBLE
    }
}

fun View?.gone() {
    if (this?.visibility != View.GONE) {
        this?.visibility = View.GONE
    }
}

inline fun <T : View> T.singleClick(time: Long = 500, increase: Boolean = false, range: Int = 10, crossinline block: (T) -> Unit) {
    if (increase) {
        this.increaseTouchRange(range)
    }
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

// 触摸区域扩大
fun View.increaseTouchRange(range: Int = 10) {
    val scale = context.resources.displayMetrics.density
    val result = (range * scale + 0.5f).toInt()
    isEnabled = true
    if (this.parent is ViewGroup) {
        val group = this.parent as ViewGroup
        group.post {
            val rect = Rect()
            this.getHitRect(rect)
            rect.top -= result // increase top hit area
            rect.left -= result // increase left hit area
            rect.bottom += result // increase bottom hit area
            rect.right += result // increase right hit area
            group.touchDelegate = TouchDelegate(rect, this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(Int.MAX_VALUE, value)
    get() = getTag(Int.MAX_VALUE) as? Long ?: 0

/**
 * 尺寸相关
 */
inline fun <reified T> Float.dp(): T {
    val result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, BaseApplication.context().resources.displayMetrics)
    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Int.dp(): T {
    return this.toFloat().dp()
}

inline fun <reified T> Float.sp(): T {
    val result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, BaseApplication.context().resources.displayMetrics)
    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Int.sp(): T {
    return this.toFloat().sp()
}
