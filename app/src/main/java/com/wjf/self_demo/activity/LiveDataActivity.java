package com.wjf.self_demo.activity;

import android.content.Intent;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityLiveDataBinding;
import com.wjf.self_demo.jetpack.LiveDataBus;
import com.wjf.self_demo.jetpack.LiveDataBusX;
import com.wjf.self_library.common.BaseActivity;

/** @author asus */
public class LiveDataActivity extends BaseActivity<ActivityLiveDataBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_live_data;
    }

    @Override
    protected void initView() {
        view.gotoBusBtn.setOnClickListener(
                view ->
                        startActivity(
                                new Intent(LiveDataActivity.this, LiveDataBusActivity.class)));
        Runnable runnable =
                () -> {
                    while (true) {
                        LiveDataBus.getInstance().with("data", String.class).postValue("lueluelue");
                        LiveDataBusX.getInstance().with("data2", String.class).postValue("emmmmm");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void initData() {}
}
