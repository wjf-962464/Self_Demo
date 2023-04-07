package com.wjf.self_demo.nestedrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wjf.self_demo.databinding.HolderColorCubeBinding
import org.jxxy.debug.corekit.recyclerview.MultipleTypeAdapter
import org.jxxy.debug.corekit.recyclerview.MultipleViewHolder2
import org.jxxy.debug.corekit.util.dp

class ChildListAdapter : MultipleTypeAdapter() {
    override fun createViewHolder(
        viewType: Int,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder? {
        return when (viewType) {
            1 -> {
                ChildColorCubeViewHolder(HolderColorCubeBinding.inflate(inflater, parent, false))
            }
            else -> null
        }
    }
}

class ChildColorCubeViewHolder(view: HolderColorCubeBinding) :
    MultipleViewHolder2<HolderColorCubeBinding, ColorCubeEntity>(
        view
    ) {
    init {
        (itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
        (itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            it.topMargin = 10f.dp()
            it.marginStart = 15f.dp()
            it.marginEnd = 15f.dp()
        }
    }

    override fun setHolder(entity: ColorCubeEntity) {
        view.root.setBackgroundColor(entity.color)
    }
}
