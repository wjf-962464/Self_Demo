package org.jxxy.debug.corekit.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class MultipleViewHolder<T : MultipleType>(itemView: View) :
    RecyclerView.ViewHolder(itemView), ViewHolderTag<T> {
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
