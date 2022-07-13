package org.jxxy.debug.corekit.widget

import android.graphics.Typeface
import org.jxxy.debug.corekit.common.BaseApplication

object IconFontManager {
    var iconAsset: Typeface? = null
        private set

    fun initAsset(path: String) {
        iconAsset = Typeface.createFromAsset(
            BaseApplication.context().assets,
            path
        )
    }
}
