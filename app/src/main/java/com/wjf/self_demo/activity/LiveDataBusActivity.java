package com.wjf.self_demo.activity;

import androidx.lifecycle.Observer;

import com.wjf.self_demo.BuildConfig;
import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityLiveDataBusBinding;
import com.wjf.self_demo.jetpack.LiveDataBus;
import com.wjf.self_demo.jetpack.LiveDataBusX;
import com.wjf.self_library.common.BaseActivity;
import com.wjf.self_library.util.log.LogUtil;

/** @author asus */
public class LiveDataBusActivity extends BaseActivity<ActivityLiveDataBusBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_live_data_bus;
    }

    @Override
    protected void initView() {
        LogUtil.d(BuildConfig.TAG, this.getClass(), "initView: 绑定前333");
        LiveDataBus.getInstance()
                .with("data", String.class)
                .observe(
                        this,
                        new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                LogUtil.d(BuildConfig.TAG, this.getClass(), "LiveDataBus: " + s);
                            }
                        });
        LiveDataBusX.getInstance()
                .with("data2", String.class)
                .observe(
                        this,
                        new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                LogUtil.d(BuildConfig.TAG, this.getClass(), "LiveDataBusX: " + s);
                            }
                        });
        LogUtil.d(BuildConfig.TAG, this.getClass(), "initView: 绑定后22");
    }

    @Override
    protected void initData() {}
}
