package org.jxxy.debug.corekit.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.jxxy.debug.corekit.util.nullOrNot

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class SingleTypeAdapter<V : ViewBinding, T> protected constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val SINGLE_TYPE = 0
    }

    private lateinit var view: V
    private val data: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        view = bindLayout(LayoutInflater.from(parent.context), parent)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindHolder(position)
    }

    protected fun bindHolder(position: Int, payload: Any? = null) {
        data.getOrNull(position)?.let {
            payload.nullOrNot({
                setHolder(it, this.view, position, this.view.root.context)
            }) { payload ->
                setHolder(it, this.view, position, payload, this.view.root.context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return SINGLE_TYPE
    }

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

    /**
     * 设置布局
     *
     * @return 布局id
     */
    protected abstract fun bindLayout(layoutInflater: LayoutInflater, parent: ViewGroup): V

    /**
     * 绑定View
     *
     * @param entity 数据实体
     * @param view 容器
     */
    protected abstract fun setHolder(entity: T, view: V, position: Int, context: Context)
    protected fun setHolder(entity: T, view: V, position: Int, payload: Any, context: Context) {
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}