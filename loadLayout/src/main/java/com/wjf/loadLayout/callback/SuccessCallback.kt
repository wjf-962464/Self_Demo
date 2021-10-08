package com.wjf.loadLayout.callback

import android.view.View

class SuccessCallback() : ICallback() {
    constructor(view: View) : this() {
        bindLayout(view)
    }

    override fun layoutResource(): Int = 0
    override fun show() {
        getRootView()?.visibility = View.VISIBLE
    }
}
