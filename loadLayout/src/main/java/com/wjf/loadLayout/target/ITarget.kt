package com.wjf.loadLayout.target

import com.wjf.loadLayout.core.LoadLayout

interface ITarget {
    override fun equals(target: Any?): Boolean

    fun replaceView(target: Any): LoadLayout
}
