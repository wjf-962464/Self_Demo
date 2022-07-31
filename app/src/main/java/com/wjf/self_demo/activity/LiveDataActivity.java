package com.wjf.self_demo.activity;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.wjf.self_demo.databinding.ActivityLiveDataBinding;
import com.wjf.self_demo.jetpack.LiveDataBus;
import com.wjf.self_demo.jetpack.LiveDataBusX;

import org.jxxy.debug.corekit.common.BaseActivity;

/** @author asus */
public class LiveDataActivity extends BaseActivity<ActivityLiveDataBinding> {


    @Override
    protected void initView() {
        getView().gotoBusBtn.setOnClickListener(
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


    @NonNull
    @Override
    protected ActivityLiveDataBinding bindLayout() {
        return ActivityLiveDataBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void subscribeUi() {

    }
}
