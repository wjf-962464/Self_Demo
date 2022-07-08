package org.jxxy.debug.corekit.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class CommonAdapter<Holder : ViewBinding, T> protected constructor() :
    RecyclerView.Adapter<CommonAdapter.ViewHolder>() {
    private val data: MutableList<T> = mutableListOf()
    protected lateinit var holder: Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bindLayout(LayoutInflater.from(parent.context))
        return ViewHolder(holder.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = data[holder.layoutPosition]
        setHolder(entity, this.holder)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitData(list: List<T>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    protected abstract fun bindLayout(inflater: LayoutInflater)

    /**
     * 绑定View
     *
     * @param entity 数据实体
     * @param view 容器
     */
    protected abstract fun setHolder(entity: T, view: Holder)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
