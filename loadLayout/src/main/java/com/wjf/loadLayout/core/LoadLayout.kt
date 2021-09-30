package com.wjf.loadLayout.core

import android.content.Context
import android.widget.FrameLayout
import com.wjf.loadLayout.callback.ICallback

class LoadLayout(context: Context) : FrameLayout(context) {
    private val EXTRAL_VIEW_INDEX = 1
    private var preCallback: ICallback? = null
    fun showCallback(callback: ICallback) {
        if (preCallback != null) {
            if (preCallback == callback) {
                return
            }
            callback.getRootView()?.let {
                removeView(it)
            }
        }
        addView(callback.getRootView())
        preCallback = callback
    }
}
