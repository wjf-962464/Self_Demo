package com.wjf.self_demo.loadlayout

import com.wjf.loadLayout.callback.ICallback
import com.wjf.self_demo.R

class LoadingCallback : ICallback() {

    override fun layoutResource(): Int = R.layout.callback_loading

    override fun show() {
        TODO("Not yet implemented")
    }
}
