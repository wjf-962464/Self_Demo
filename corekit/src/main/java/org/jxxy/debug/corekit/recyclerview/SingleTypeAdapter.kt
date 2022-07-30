package org.jxxy.debug.corekit.recyclerview

import androidx.viewbinding.ViewBinding
import org.jxxy.debug.corekit.util.nullOrNot

/**
 * 泛型1：ViewBinding,即绑定的Holder的视图生命
 * 泛型2：数据类型，即list中的泛型
 */
abstract class SingleTypeAdapter protected constructor() :
    BaseTypeAdapter<Any, SingleViewHolder<ViewBinding, Any>>() {
    companion object {
        private const val SINGLE_TYPE = 0
    }

    override fun bindHolder(
        holder: SingleViewHolder<ViewBinding, Any>,
        position: Int,
        payload: Any?
    ) {
        data.getOrNull(position)?.let {
            payload.nullOrNot({
                holder.setHolder(it, holder.itemView.context)
            }) { payload ->
                holder.setHolder(it, payload, holder.itemView.context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return SINGLE_TYPE
    }
}
