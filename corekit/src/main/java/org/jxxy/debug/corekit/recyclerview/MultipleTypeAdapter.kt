package org.jxxy.debug.corekit.recyclerview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class MultipleTypeAdapter protected constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val NONE_TYPE = -1
        private const val TYPE_ERROR_MSG = "CommonAdapter 在解析映射时没有绑定对应的ViewType"
    }

    private val map: SparseArray<CommonMap<ViewBinding, MultipleType>> = SparseArray()
    private val data: MutableList<MultipleType> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = getMap(viewType).onCreateViewHolder(LayoutInflater.from(parent.context), parent)
        return CommonMap.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            getMap(getItemViewType(position)).apply {
                bindViewHolder(it, this.view, position, holder.itemView.context)
            }
        }
    }

    fun getMap(viewType: Int): CommonMap<ViewBinding, MultipleType> {
        return map.get(viewType) ?: throw IllegalStateException(TYPE_ERROR_MSG)
    }

    override fun getItemViewType(position: Int): Int {
        return data.getOrNull(position)?.viewType() ?: NONE_TYPE
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitData(list: List<MultipleType>) {
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

    abstract fun bindMap(map: SparseArray<CommonMap<ViewBinding, MultipleType>>)
    private fun initMap() {
        bindMap(map)
    }

    init {
        initMap()
    }
}
