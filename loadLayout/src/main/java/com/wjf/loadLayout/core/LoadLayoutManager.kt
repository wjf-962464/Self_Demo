package com.wjf.loadLayout.core

import androidx.lifecycle.Lifecycle
import com.wjf.loadLayout.callback.ICallback
import com.wjf.loadLayout.target.ActivityTarget
import com.wjf.loadLayout.target.ITarget

class LoadLayoutManager private constructor(private val builder: Builder) {

    private constructor() : this(Builder())

    companion object {
        val instance: LoadLayoutManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LoadLayoutManager()
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

        fun addCallBack(callback: ICallback): Builder {
            callbacks.add(callback)
            return this
        }

        fun build(): LoadLayoutManager {
            return LoadLayoutManager(this)
        }
    }
}
