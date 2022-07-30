package org.jxxy.debug.corekit.recyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SingleViewHolder<V : ViewBinding, T>(val view: V) :
    RecyclerView.ViewHolder(view.root) {
    /**
     * holder操作
     *
     * @param entity 数据实体
     * @param context 上下文
     */
    abstract fun setHolder(entity: T, context: Context)

    /**
     * holder操作，含payload
     *
     * @param entity 数据实体
     * @param context 上下文
     */
    open fun setHolder(
        entity: T,
        payload: Any,
        context: Context
    ) {
    }
}
