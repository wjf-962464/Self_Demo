package com.wjf.self_demo.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wjf.self_demo.databinding.ItemListIndexBinding
import com.wjf.self_demo.entity.IndexListMenu
import org.jxxy.debug.corekit.recyclerview.SingleTypeAdapter
import org.jxxy.debug.corekit.recyclerview.SingleViewHolder
import org.jxxy.debug.corekit.util.singleClick

/**
 * @author : Wangjf
 * @date : 2021/4/6
 */
class IndexListAdapter : SingleTypeAdapter<IndexListMenu>() {

    class IndexListViewHolder(view: ItemListIndexBinding) :
        SingleViewHolder<ItemListIndexBinding, IndexListMenu>(view) {
        override fun setHolder(entity: IndexListMenu) {
            view.menuText.text = entity.des
            view.root.singleClick { v: View? ->
                val intent = Intent()
                intent.putExtra("data", entity.data)
                intent.setClass(context, entity.gotoClass)
                context.startActivity(intent)
            }
        }
    }

    override fun createViewHolder(
        viewType: Int,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return IndexListViewHolder(ItemListIndexBinding.inflate(inflater, parent, false))
    }
}
