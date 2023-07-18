package com.wjf.self_demo.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SelfViewHolder
import com.wjf.self_demo.databinding.ItemListIndexBinding
import com.wjf.self_demo.entity.IndexListMenu
import org.jxxy.debug.corekit.recyclerview.SingleTypeAdapter
import org.jxxy.debug.corekit.util.singleClick

/**
 * @author : Wangjf
 * @date : 2021/4/6
 */
class IndexListAdapter : SingleTypeAdapter<IndexListMenu>() {
    private var total = 0L

    class IndexListViewHolder(view: ItemListIndexBinding) : SelfViewHolder<ItemListIndexBinding, IndexListMenu>(view) {
        override fun setHolder(entity: IndexListMenu) {
            val start = System.currentTimeMillis()
            view.menuText.text = entity.des
            view.root.singleClick { v: View? ->
                val intent = Intent()
                intent.putExtra("data", entity.data)
                intent.setClass(context, entity.gotoClass)
                context.startActivity(intent)
            }
            Log.d("wjftc", "onBindViewHolder: ${System.currentTimeMillis() - start} ms")
        }
    }

    override fun createViewHolder(viewType: Int, inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        val start = System.currentTimeMillis()
        val holder = IndexListViewHolder(ItemListIndexBinding.inflate(inflater, parent, false))
        val dif = System.currentTimeMillis() - start
        total += dif
        Log.d("wjftc", "createViewHolder: $dif ms in ${Thread.currentThread().name} $total ms")
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    fun addAll(list: List<IndexListMenu>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        data.addAll(list)
        notifyDataSetChanged()
    }
}
