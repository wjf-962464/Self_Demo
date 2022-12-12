package com.wjf.self_demo.nestedrecycler.widget

import android.view.View
import android.view.ViewGroup

object FindTarget {
    fun findChildScrollTarget(sourceView: View?): NestedChildRecyclerView? {
        if (sourceView == null || sourceView.visibility != View.VISIBLE) {
            return null
        }
        if (sourceView is NestedChildRecyclerView) {
            return sourceView
        }
        if (sourceView !is ViewGroup) {
            return null
        }
        val childCount = sourceView.childCount
        for (i in childCount - 1 downTo 0) {
            val view = sourceView.getChildAt(i)
            val centerX = (view.left + view.right) / 2
            val contentLeft = sourceView.scrollX
            if (centerX <= contentLeft || centerX >= contentLeft + sourceView.width) {
                continue
            }
            val target = findChildScrollTarget(view)
            if (target != null) {
                return target
            }
        }
        return null
    }
}
