package org.jxxy.debug.corekit.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jxxy.debug.corekit.R
import org.jxxy.debug.corekit.util.ResourceUtil

abstract class BaseTypeAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected val data: MutableList<T> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return createViewHolder(viewType, LayoutInflater.from(parent.context), parent)
            ?: throw IllegalStateException(
                ResourceUtil.getString(
                    R.string.exception_adapter_create_failed,
                    viewType
                )
            )
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
    ): VH?

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitData(list: List<T>) {
        val start = itemCount
        val count = list.size
        this.data.addAll(list)
        notifyItemRangeInserted(start, count)
    }

    fun clearData() {
        val count = itemCount
        this.data.clear()
        notifyItemRangeRemoved(0, count)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindHolder(holder, position)
    }

    protected abstract fun bindHolder(
        holder: VH,
        position: Int,
        payload: Any? = null
    )
}
