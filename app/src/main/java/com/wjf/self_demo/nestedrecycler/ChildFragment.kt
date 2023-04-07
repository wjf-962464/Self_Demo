package com.wjf.self_demo.nestedrecycler

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.orhanobut.logger.Logger
import com.wjf.self_demo.databinding.FragmentChildBinding
import org.jxxy.debug.corekit.common.BaseFragment
import org.jxxy.debug.corekit.recyclerview.MultipleType

class ChildFragment : BaseFragment<FragmentChildBinding>() {
    private var currentPosition: Int = 0
    private var category: String = ""
    private val adapter by lazy { ChildListAdapter() }
    fun init(position: Int, category: String) {
        currentPosition = position
        this.category = category
    }

    override fun initView() {
        Logger.d("我是子tab页$currentPosition $category")
        find.childRecycler.let {
            it.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            it.adapter = adapter
        }
    }

    private val colors = mutableListOf<String>("#FFBB86FC", "#FF03DAC5", "#FF6200EE", "#FF018786")
    override fun subscribeUi() {
        val data = mutableListOf<MultipleType>()
        var colorIndex = 0
        for (i in 0 until 100) {
            data.add(ColorCubeEntity(Color.parseColor(colors[(colorIndex++) % colors.size])))
        }
        adapter.add(data)
    }

    override fun bindLayout(): FragmentChildBinding {
        return FragmentChildBinding.inflate(layoutInflater)
    }
}
