package com.wjf.self_demo.activity;

import android.Manifest;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityFishBinding;
import com.wjf.self_demo.view.FishDrawable;
import com.wjf.self_library.common.BaseActivity;

/** @author WJF */
public class FishActivity extends BaseActivity<ActivityFishBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_fish;
    }

    @Override
    protected void initView() {
        view.fishImg.setImageDrawable(new FishDrawable());
        addPermission(Manifest.permission.CALL_PHONE)
                .addPermission(Manifest.permission.ACCEPT_HANDOVER)
                .requestPermission();
    }

    @Override
    protected void doSomethingAfterGranted() {
        super.doSomethingAfterGranted();
    }

    @Override
    protected void initData() {}
}
