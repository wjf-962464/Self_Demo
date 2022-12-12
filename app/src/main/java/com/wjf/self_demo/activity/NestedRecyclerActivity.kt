package com.wjf.self_demo.activity

import android.view.LayoutInflater
import com.google.android.material.tabs.TabLayout
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityNestedRecyclerBinding
import com.wjf.self_demo.databinding.TabParentBinding
import com.wjf.self_demo.nestedrecycler.ParentPageAdapter
import org.jxxy.debug.corekit.common.BaseActivity
import org.jxxy.debug.corekit.util.ResourceUtil

/** @author WJF
 */
class NestedRecyclerActivity : BaseActivity<ActivityNestedRecyclerBinding>() {
    private val adapter by lazy { ParentPageAdapter(supportFragmentManager) }

    override fun initView() {
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

    override fun bindLayout(): ActivityNestedRecyclerBinding {
        return ActivityNestedRecyclerBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
        val data = mutableListOf<String>()
        for (i in 0 until 10) {
            data.add("场次${i + 1}")
        }
        adapter.submit(data)
        data.forEachIndexed { i, s ->
            val tab = view.parentTab.getTabAt(i) ?: view.parentTab.newTab()
            val tabView = TabParentBinding.inflate(LayoutInflater.from(this))
            tabView.tabTitleTv.text = s
            tab.customView = tabView.root
            if (i == 0) {
                tabSelect(tab)
            } else {
                tabDisSelect(tab)
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
