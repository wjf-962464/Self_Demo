package com.wjf.loadLayout.target

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wjf.loadLayout.util.Constants

class ActivityTarget : ITarget {

    override fun equals(target: Any?): Boolean = target is Activity

    override fun replaceView(target: Any): FrameLayout {
        val activity = target as Activity
        val contentFrameLayout = activity.findViewById<ViewGroup>(android.R.id.content)
        val oldView = contentFrameLayout.getChildAt(Constants.ROOT_VIEW)
/*        contentFrameLayout.removeViewAt(Constants.ROOT_VIEW)
        val loadLayout = LoadLayout(activity.baseContext)
        loadLayout.layoutParams = oldView.layoutParams
        loadLayout.addView(oldView)*/
        oldView.visibility = View.GONE
//        contentFrameLayout.addView(loadLayout)
        return contentFrameLayout as FrameLayout
    }
}
