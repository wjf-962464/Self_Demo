package org.jxxy.debug.corekit.recyclerview

import androidx.viewbinding.ViewBinding

abstract class CommonSelectViewHolder<V : ViewBinding, T : MultipleType>(
    view: V,
    protected val listener: ItemSelectListener
) :
    MultipleViewHolder2<V, T>(
        view
    ),
    BaseSelectBehavior {

    private var curState: Int = -1
    override fun dispatchState(status: Int) {
        if (curState == status) {
            // 状态一致，不变更
            return
        }
        curState = status
        stateHandle(status)
    }

    override fun stateHandle(status: Int) {
    }

    override fun onNormalState() {
    }

    override fun onSelectedState() {
    }
}
