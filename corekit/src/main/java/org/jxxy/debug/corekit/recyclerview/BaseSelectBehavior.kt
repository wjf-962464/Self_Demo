package org.jxxy.debug.corekit.recyclerview

interface BaseSelectBehavior {
    fun dispatchState(status: Int)

    fun stateHandle(status: Int)

    fun onNormalState()

    fun onSelectedState()
}
