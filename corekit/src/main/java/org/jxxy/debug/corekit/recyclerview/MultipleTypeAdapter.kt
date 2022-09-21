package org.jxxy.debug.corekit.recyclerview

import androidx.viewbinding.ViewBinding
import com.orhanobut.logger.Logger
import org.jxxy.debug.corekit.util.nullOrNot

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class MultipleTypeAdapter protected constructor() :
    BaseTypeAdapter<MultipleType, MultipleViewHolder<ViewBinding, MultipleType>>() {
    companion object {
        private const val NONE_TYPE = -1
    }

    override fun bindHolder(
        holder: MultipleViewHolder<ViewBinding, MultipleType>,
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
        if (data.getOrNull(position)?.viewType() == null) {
            Logger.d("aaa")
        }
        return data.getOrNull(position)?.viewType() ?: NONE_TYPE
    }
}
