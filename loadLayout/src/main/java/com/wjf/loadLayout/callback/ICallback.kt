package com.wjf.loadLayout.callback

import android.content.Context
import android.view.View
import org.jxxy.debug.corekit.util.singleClick

abstract class ICallback() {
    private var callOnListener: CallOnListener? = null
    private var rootView: View? = null
    private var context: Context? = null

    constructor(callOnListener: CallOnListener?) : this() {
        this.callOnListener = callOnListener
    }

    interface CallOnListener {
        fun statusResponse()
    }

    fun setWithLayout(context: Context) {
        this.context = context
    }

    fun getRootView(): View? {
        val resId = layoutResource()
        if (resId == 0 || rootView != null) {
            return rootView
        }
        if (context == null) {
            throw IllegalArgumentException("ICallback's fun getRootView() context is null")
        }
        rootView = View.inflate(context, resId, null)
        rootView?.singleClick {
            callOnListener?.statusResponse()
        }
        return rootView
    }

    fun bindLayout(view: View) {
        rootView = view
    }

    abstract fun layoutResource(): Int
    abstract fun show()

    fun attach() {
    }

    fun detach() {}
}
