package org.jxxy.debug.corekit.widget

import android.graphics.Typeface

object IconFontManager {
    var iconAsset: Typeface? = null
        private set

    fun initAsset(path: String) {
        iconAsset = Typeface.createFromAsset(
            org.jxxy.debug.corekit.common.BaseApplication.context().assets,
            path
        )
    }
}
