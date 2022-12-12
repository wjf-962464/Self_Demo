package com.wjf.self_demo.nestedrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.wjf.self_demo.databinding.HolderColorCubeBinding
import org.jxxy.debug.corekit.recyclerview.MultipleType
import org.jxxy.debug.corekit.recyclerview.MultipleTypeAdapter
import org.jxxy.debug.corekit.recyclerview.MultipleViewHolder
import org.jxxy.debug.corekit.util.dp

class ChildListAdapter : MultipleTypeAdapter() {
    override fun createViewHolder(
        viewType: Int,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): MultipleViewHolder<ViewBinding, MultipleType>? {
        return when (viewType) {
            1 -> {
                ChildColorCubeViewHolder(HolderColorCubeBinding.inflate(inflater, parent, false))
                    as? MultipleViewHolder<ViewBinding, MultipleType>
            }
            else -> null
        }
    }
}

class ChildColorCubeViewHolder(view: HolderColorCubeBinding) :
    MultipleViewHolder<HolderColorCubeBinding, ColorCubeEntity>(
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

    override fun setHolder(entity: ColorCubeEntity, context: Context) {
        view.root.setBackgroundColor(entity.color)
    }
}
