package com.wjf.loadLayout.target

import android.widget.FrameLayout

interface ITarget {
    override fun equals(target: Any?): Boolean

    fun replaceView(target: Any): FrameLayout
}
