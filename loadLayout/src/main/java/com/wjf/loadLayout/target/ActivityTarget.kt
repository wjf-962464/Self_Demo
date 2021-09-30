package com.wjf.loadLayout.target

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.wjf.loadLayout.core.LoadLayout

class ActivityTarget : ITarget {
    private val ROOT_VIEW = 0
    override fun equals(target: Any?): Boolean = target is Activity

    override fun replaceView(target: Any): LoadLayout {
        val activity = target as Activity
        val contentFrameLayout = activity.findViewById<ViewGroup>(android.R.id.content)
        val oldView = contentFrameLayout.getChildAt(ROOT_VIEW)
        contentFrameLayout.removeViewAt(ROOT_VIEW)
        val loadLayout = LoadLayout(activity.baseContext)
        loadLayout.layoutParams = oldView.layoutParams
//        loadLayout.addView(oldView)
        oldView.visibility = View.GONE
        contentFrameLayout.addView(loadLayout)
        return loadLayout
    }
}
