package org.jxxy.debug.widget

import android.graphics.Typeface
import org.jxxy.debug.common.BaseApplication

object IconFontManager {
    var iconAsset: Typeface? = null
        private set
    fun initAsset(path: String) {
        iconAsset = Typeface.createFromAsset(BaseApplication.context()?.assets, path)
    }
}
