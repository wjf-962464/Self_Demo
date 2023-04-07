package com.wjf.self_demo.nestedrecycler

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.orhanobut.logger.Logger
import com.wjf.self_demo.databinding.FragmentParentBinding
import org.jxxy.debug.corekit.common.BaseFragment
import org.jxxy.debug.corekit.recyclerview.MultipleType

class ParentFragment : BaseFragment<FragmentParentBinding>() {
    private val adapter by lazy { ParentListAdapter(childFragmentManager) }
    private var currentPosition: Int = 0
    fun init(position: Int) {
        currentPosition = position
    }

    override fun bindLayout(): FragmentParentBinding {
        return FragmentParentBinding.inflate(layoutInflater)
    }

    override fun initView() {
        Logger.d("我是父tab页$currentPosition")
        find.parentRecycler.let {
            it.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            it.adapter = this@ParentFragment.adapter
        }
    }

    override fun subscribeUi() {
        val data = mutableListOf<MultipleType>()
        data.add(ColorCubeEntity(Color.parseColor("#FFBB86FC")))
        data.add(ColorCubeEntity(Color.parseColor("#FF03DAC5")))
        data.add(ColorCubeEntity(Color.parseColor("#FF6200EE")))
        data.add(ColorCubeEntity(Color.parseColor("#FF018786")))
        val tab = ParentTabEntity()
        tab.tabs = mutableListOf("秒杀爆款", "海鲜", "猪肉")
        data.add(tab)
        adapter.add(data)
    }
}
