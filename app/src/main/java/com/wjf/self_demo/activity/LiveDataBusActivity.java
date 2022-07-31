package com.wjf.self_demo.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.Logger;
import com.wjf.self_demo.BuildConfig;
import com.wjf.self_demo.databinding.ActivityLiveDataBusBinding;
import com.wjf.self_demo.jetpack.LiveDataBus;
import com.wjf.self_demo.jetpack.LiveDataBusX;

import org.jxxy.debug.corekit.common.BaseActivity;

/** @author asus */
public class LiveDataBusActivity extends BaseActivity<ActivityLiveDataBusBinding> {


    @Override
    protected void initView() {
        Logger.d(BuildConfig.TAG, "initView: 绑定前333");
        LiveDataBus.getInstance()
                .with("data", String.class)
                .observe(
                        this,
                        new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Logger.d(BuildConfig.TAG, "LiveDataBus: " + s);
                            }
                        });
        LiveDataBusX.getInstance()
                .with("data2", String.class)
                .observe(
                        this,
                        new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Logger.d(BuildConfig.TAG, "LiveDataBusX: " + s);
                            }
                        });
        Logger.d(BuildConfig.TAG, "initView: 绑定后22");
    }


    @NonNull
    @Override
    protected ActivityLiveDataBusBinding bindLayout() {
        return ActivityLiveDataBusBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void subscribeUi() {

    }
}
