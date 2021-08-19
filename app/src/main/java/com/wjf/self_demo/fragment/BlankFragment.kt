package com.wjf.self_demo.fragment

import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.FragmentBlankBinding
import com.wjf.self_library.common.BaseFragment

class BlankFragment : BaseFragment<FragmentBlankBinding>() {
    override fun setLayout(): Int = R.layout.fragment_blank

    override fun initView() {
        view.tvTitle.text = "略略略"
    }

    override fun initData() {
    }
}
