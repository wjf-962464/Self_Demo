package com.wjf.self_demo.nestedrecycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ChildPageAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val data: MutableList<String> = mutableListOf()
    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Fragment {
        val f = ChildFragment()
        f.init(position, data.getOrNull(position).orEmpty())
        return f
    }

    fun submit(list: List<String>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}
