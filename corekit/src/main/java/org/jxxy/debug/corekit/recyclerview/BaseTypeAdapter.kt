package org.jxxy.debug.corekit.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jxxy.debug.corekit.util.castToTarget

abstract class BaseTypeAdapter<T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val data: MutableList<T> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return createViewHolder(viewType, LayoutInflater.from(parent.context), parent)
            ?: RecyclerViewHolder(parent)
    }

    /**
     * 创建holder
     *
     * @return 对应类型的ViewHolder
     */
    protected abstract fun createViewHolder(
        viewType: Int,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder?

    override fun getItemCount(): Int {
        return data.size
    }

    open fun add(bean: T) {
        val start = itemCount
        data.add(bean)
        notifyItemInserted(start)
    }

    open fun add(list: List<T>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        val start = itemCount
        val count = list.size
        data.addAll(list)
        notifyItemRangeInserted(start, count)
    }

    open fun clear() {
        val count = itemCount
        data.clear()
        notifyItemRangeRemoved(0, count)
    }

    open fun clearAndAdd(list: List<T>?, withAnim: Boolean = false) {
        val preCount = itemCount
        data.clear()
        if (!list.isNullOrEmpty()) {
            data.addAll(list)
            if (withAnim) {
                val size = list.size
                notifyItemRangeChanged(0, size)
                notifyItemRangeRemoved(size, preCount - size)
            } else {
                notifyDataSetChanged()
            }
        } else {
            notifyItemRangeRemoved(0, preCount)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.castToTarget<ViewHolderTag<T>>()?.let { tag ->
            data.getOrNull(position)?.let { entity ->
                bindHolder(tag, entity, position)
            }
        }
        holderBindExtras(holder, position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.castToTarget<ViewHolderTag<T>>()?.let { tag ->
                data.getOrNull(position)?.let { entity ->
                    bindHolder(tag, entity, position, payloads)
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    protected abstract fun bindHolder(
        holder: ViewHolderTag<T>,
        entity: T,
        position: Int
    )

    protected abstract fun bindHolder(
        holder: ViewHolderTag<T>,
        entity: T,
        position: Int,
        payloads: MutableList<Any>
    )

    protected abstract fun holderBindExtras(holder: RecyclerView.ViewHolder, position: Int)
}
