package com.wjf.self_demo.fragment;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.FragmentBlankBinding;
import com.wjf.self_library.common.BaseFragment;

/** @author WJF */
public class BlankFragment extends BaseFragment<FragmentBlankBinding> {

    @Override
    public int setLayout() {
        return R.layout.fragment_blank;
    }

    @Override
    protected void initView() {
        bind.tvTitle.setText("SAd");
    }

    @Override
    protected void initData() {}
}
