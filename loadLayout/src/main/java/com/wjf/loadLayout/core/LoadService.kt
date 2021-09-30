package com.wjf.loadLayout.core

import androidx.lifecycle.Lifecycle
import com.wjf.loadLayout.callback.ICallback

class LoadService(
    private val loadLayout: LoadLayout,
    builder: LoadLayoutManager.Builder,
    private val lifecycle: Lifecycle
) {
    private val callbacks: MutableMap<Class<out ICallback>, ICallback> = mutableMapOf()

    init {
        initCallbacks(builder)
    }

    private fun initCallbacks(builder: LoadLayoutManager.Builder) {
        for (callback in builder.callbacks) {
            callback.setWithLayout(loadLayout.context)
            callbacks[callback.javaClass] = callback
        }
    }

    fun addCallback(callback: ICallback) {
        callback.setWithLayout(loadLayout.context)
        callbacks[callback.javaClass] = callback
    }

    fun showCallback(clazz: Class<out ICallback>) {
        checkCallback(clazz)
        val callback = callbacks[clazz]
        callback?.let { loadLayout.showCallback(it) }
    }

    private fun checkCallback(clazz: Class<out ICallback>) {
        if (!callbacks.containsKey(clazz)) {
            throw IllegalArgumentException("LoadService does not have this callback:$clazz")
        }
    }
}
