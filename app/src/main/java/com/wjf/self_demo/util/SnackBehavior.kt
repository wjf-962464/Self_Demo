package com.wjf.self_demo.util

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import kotlin.math.min

class SnackBehavior(context: Context, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        Log.d("wjftc", "onDependentViewChanged: ${dependency.translationY - dependency.height}")
        child.translationY = min(0f, dependency.translationY - dependency.height)
        return true
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
        Log.d("wjftc", "onDependentViewRemoved: ")
        child.translationY = 0f
    }
}
