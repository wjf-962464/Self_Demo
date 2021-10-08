package com.wjf.loadLayout.core

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import com.wjf.loadLayout.callback.ICallback
import com.wjf.loadLayout.util.Constants
import com.wjf.loadLayout.util.LoadUtil

class LoadService(
    private val loadLayout: FrameLayout,
    builder: LoadManager.Builder,
    private val lifecycle: Lifecycle
) {
    private val callbacks: MutableMap<Class<out ICallback>, ICallback> = mutableMapOf()
    private var defaultCallback: ICallback? = null
    private var preCallback: ICallback? = null
    private val handler: Handler

    init {
        initCallbacks(builder)
        handler = Handler(Looper.getMainLooper())
    }

    private fun initCallbacks(builder: LoadManager.Builder) {
        for (callback in builder.callbacks) {
            callback.setWithLayout(loadLayout.context)
            callbacks[callback.javaClass] = callback
        }
        defaultCallback = builder.callbacks[0]
        defaultCallback?.bindLayout(loadLayout[Constants.ROOT_VIEW])
    }

    fun showCallback(clazz: Class<out ICallback>) {
        checkCallback(clazz)
        val callback = callbacks[clazz]
        showCallback(callback)
    }

    private fun showCallback(callback: ICallback?) {
        callback?.let {
            if (!LoadUtil.isMainThread()) {
                handler.post {
                    showLayout(callback)
                }
            } else {
                showLayout(callback)
            }
        }
    }

    fun setCallback(
        clazz: Class<out ICallback>,
        transform: (view: View) -> Unit,
    ) {
        checkCallback(clazz)
        val callback = callbacks[clazz]
        callback?.getRootView()?.let {
            transform.invoke(it)
        }
    }

    private fun showLayout(callback: ICallback) {
        if (preCallback != null) {
            if (preCallback == callback) {
                return
            }
            preCallback?.getRootView()?.let {
                loadLayout.removeViewAt(Constants.EXTRA_VIEW_INDEX)
            }
        }
        if (callback != defaultCallback) {
            loadLayout.addView(callback.getRootView())
        } else {
            loadLayout[Constants.ROOT_VIEW].visibility = FrameLayout.VISIBLE
        }
        preCallback = callback
    }

    fun showDefault() {
        defaultCallback?.let {
            showCallback(it)
        }
    }

    private fun checkCallback(clazz: Class<out ICallback>) {
        if (!callbacks.containsKey(clazz)) {
            throw IllegalArgumentException("LoadService does not have this callback:$clazz")
        }
    }
}
