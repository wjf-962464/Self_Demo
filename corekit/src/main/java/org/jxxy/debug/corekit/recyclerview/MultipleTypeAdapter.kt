package org.jxxy.debug.corekit.recyclerview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

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

    private val map: SparseArray<CommonMap<MultipleType, RecyclerView.ViewHolder>> = SparseArray()
    private val data: MutableList<MultipleType> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mapHolder = map.get(viewType) ?: throw IllegalStateException(TYPE_ERROR_MSG)
        return mapHolder.createViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mapHolder =
            map.get(getItemViewType(position)) ?: throw IllegalStateException(TYPE_ERROR_MSG)
        data.getOrNull(position)?.let {
            mapHolder.bindViewHolder(it, holder)
        }
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

    abstract fun bindMap(map: SparseArray<CommonMap<MultipleType, RecyclerView.ViewHolder>>)
    private fun initMap() {
        bindMap(map)
    }

    init {
        initMap()
    }
}
