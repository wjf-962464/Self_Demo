package com.wjf.loadLayout.core

import androidx.lifecycle.Lifecycle
import com.wjf.loadLayout.callback.ICallback
import com.wjf.loadLayout.callback.SuccessCallback
import com.wjf.loadLayout.target.ActivityTarget
import com.wjf.loadLayout.target.ITarget

class LoadManager private constructor(private val builder: Builder) {

    private constructor() : this(Builder())

    companion object {
        val INSTANCE: LoadManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LoadManager()
        }
    }

    fun bind(view: Any, lifecycle: Lifecycle): LoadService {
        val target = builder.targetList.find {
            it == view
        } ?: throw IllegalArgumentException("This type of view does not support binding currently ")
        val loadLayout = target.replaceView(view)
        return LoadService(loadLayout, builder, lifecycle)
    }

    class Builder {
        val targetList: MutableList<ITarget> = mutableListOf(ActivityTarget())
        val callbacks: MutableList<ICallback> = mutableListOf()

        init {
            callbacks.add(SuccessCallback())
        }

        fun addCallBack(callback: ICallback): Builder {
            callbacks.add(callback)
            return this
        }

        fun setDefaultCallback(callback: ICallback): Builder {
            callbacks[0] = callback
            return this
        }

        fun build(): LoadManager {
            return LoadManager(this)
        }
    }
}
