package org.jxxy.debug.corekit.util

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object ResourceUtil {
    fun getString(@StringRes resId: Int): String {
        return org.jxxy.debug.corekit.common.BaseApplication.context().getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return org.jxxy.debug.corekit.common.BaseApplication.context().getString(resId, formatArgs)
    }

    @ColorInt
    fun getColor(@ColorRes colorResId: Int): Int {
        return ContextCompat.getColor(
            org.jxxy.debug.corekit.common.BaseApplication.context(),
            colorResId
        )
    }

    fun getDrawable(drawableResId: Int): Drawable? {
        return ContextCompat.getDrawable(
            org.jxxy.debug.corekit.common.BaseApplication.context(),
            drawableResId
        )
    }
}
