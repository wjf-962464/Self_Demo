package com.wjf.self_library.common

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.wjf.self_library.BuildConfig
import java.util.*

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class CommonAdapter<Holder : ViewDataBinding, T> protected constructor() :
    RecyclerView.Adapter<CommonAdapter.ViewHolder>() {
    protected val data: MutableList<T>
    protected lateinit var context: Context
    private lateinit var holder: Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        holder = DataBindingUtil.inflate(LayoutInflater.from(context), setLayout(), parent, false)
        return ViewHolder(holder.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = data[position]
        holder.setIsRecyclable(false)
        Log.d(BuildConfig.TAG, "position:$position")
        setHolder(entity, this.holder)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitList(data: List<T>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.data.clear()
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    protected abstract fun setLayout(): Int

    /**
     * 绑定View
     *
     * @param entity 数据实体
     * @param view 容器
     */
    protected abstract fun setHolder(entity: T, view: Holder)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    init {
        data = ArrayList()
    }
}
