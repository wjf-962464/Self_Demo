package com.wjf.self_demo.nestedrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.HolderColorCubeBinding
import com.wjf.self_demo.databinding.HolderTabChildBinding
import com.wjf.self_demo.databinding.TabParentBinding
import com.wjf.self_demo.nestedrecycler.widget.NestedCeilingHelper
import org.jxxy.debug.corekit.recyclerview.MultipleType
import org.jxxy.debug.corekit.recyclerview.MultipleTypeAdapter
import org.jxxy.debug.corekit.recyclerview.MultipleViewHolder
import org.jxxy.debug.corekit.util.ResourceUtil

class ParentTabEntity : MultipleType {
    var tabs: MutableList<String>? = null
    override fun viewType(): Int = 0
}

class ColorCubeEntity(color: Int) : MultipleType {

    val color: Int = color

    override fun viewType(): Int = 1
}

class ParentListAdapter(private val fragmentManager: FragmentManager) : MultipleTypeAdapter() {
    override fun createViewHolder(
        viewType: Int,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): MultipleViewHolder<ViewBinding, MultipleType>? {
        return when (viewType) {
            0 -> {
                ParentTabViewHolder(
                    HolderTabChildBinding.inflate(inflater, parent, false),
                    fragmentManager
                )
                    as? MultipleViewHolder<ViewBinding, MultipleType>
            }
            1 -> {
                ColorCubeViewHolder(HolderColorCubeBinding.inflate(inflater, parent, false))
                    as? MultipleViewHolder<ViewBinding, MultipleType>
            }
            else -> null
        }
    }
}

class ParentTabViewHolder(view: HolderTabChildBinding, fragmentManager: FragmentManager) :
    MultipleViewHolder<HolderTabChildBinding, ParentTabEntity>(
        view
    ) {
    private val adapter by lazy { ChildPageAdapter(fragmentManager) }

    init {
        (itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
        NestedCeilingHelper.setNestedChildContainerTag(this.itemView)
        view.parentViewPager.adapter = adapter
        view.parentTab.tabMode = TabLayout.MODE_SCROLLABLE
        view.parentTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabSelect(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabDisSelect(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                tabSelect(tab)
            }
        })
        view.parentTab.setupWithViewPager(view.parentViewPager)
    }

    override fun setHolder(entity: ParentTabEntity, context: Context) {
        entity.tabs?.let {
            adapter.submit(it)
            it.forEachIndexed { i, s ->
                val tab = view.parentTab.getTabAt(i) ?: view.parentTab.newTab()
                val tabView = TabParentBinding.inflate(LayoutInflater.from(context))
                tabView.tabTitleTv.text = s
                tab.customView = tabView.root
                if (i == 0) {
                    tabSelect(tab)
                } else {
                    tabDisSelect(tab)
                }
            }
        }
    }

    private fun tabSelect(tab: TabLayout.Tab) {
        tab.customView?.let {
            val tabView = TabParentBinding.bind(it)
            tabView.tabTitleTv.setTextColor(ResourceUtil.getColor(R.color.purple_200))
            tabView.tabLine.setBackgroundColor(ResourceUtil.getColor(R.color.purple_200))
        }
    }

    private fun tabDisSelect(tab: TabLayout.Tab) {
        tab.customView?.let {
            val tabView = TabParentBinding.bind(it)
            tabView.tabTitleTv.setTextColor(ResourceUtil.getColor(R.color.teal_200))
            tabView.tabLine.setBackgroundColor(ResourceUtil.getColor(R.color.teal_200))
        }
    }
}

class ColorCubeViewHolder(view: HolderColorCubeBinding) :
    MultipleViewHolder<HolderColorCubeBinding, ColorCubeEntity>(
        view
    ) {
    init {
        (itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
    }

    override fun setHolder(entity: ColorCubeEntity, context: Context) {
        view.root.setBackgroundColor(entity.color)
    }
}
