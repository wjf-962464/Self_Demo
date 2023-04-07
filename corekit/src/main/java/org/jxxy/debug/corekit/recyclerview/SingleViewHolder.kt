package org.jxxy.debug.corekit.recyclerview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SingleViewHolder<V : ViewBinding, T>(val view: V) :
    RecyclerView.ViewHolder(view.root), ViewHolderTag<T> {
    val context: Context = itemView.context

    /**
     * holder操作，含payload
     * 默认空实现
     *
     * @param entity 数据实体
     */
    override fun setHolder(entity: T, payload: Any) {
    }
}
