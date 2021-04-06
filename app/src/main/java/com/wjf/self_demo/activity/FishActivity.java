package com.wjf.self_demo.activity;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityFishBinding;
import com.wjf.self_demo.view.FishDrawable;
import com.wjf.self_library.common.BaseActivity;

public class FishActivity extends BaseActivity<ActivityFishBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_fish;
    }

    @Override
    protected void initView() {
        view.fishImg.setImageDrawable(new FishDrawable());
    }

    @Override
    protected void initData() {

    }
}