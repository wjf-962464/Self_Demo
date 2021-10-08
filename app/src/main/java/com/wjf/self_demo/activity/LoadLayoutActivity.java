package com.wjf.self_demo.activity;

import android.view.View;

import com.wjf.loadLayout.callback.ICallback;
import com.wjf.loadLayout.core.LoadManager;
import com.wjf.loadLayout.core.LoadService;
import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityLoadLayoutBinding;
import com.wjf.self_demo.databinding.CallbackEmptyBinding;
import com.wjf.self_demo.loadlayout.EmptyCallback;
import com.wjf.self_demo.loadlayout.LoadingCallback;
import com.wjf.self_library.common.BaseActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/** @author WJF */
public class LoadLayoutActivity extends BaseActivity<ActivityLoadLayoutBinding> {
    private LoadService loadService;

    @Override
    public int setLayout() {
        return R.layout.activity_load_layout;
    }

    @Override
    protected void initView() {
        ExecutorService singleThreadPool =
                new ThreadPoolExecutor(
                        1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024));
        LoadManager manager =
                new LoadManager.Builder()
                        .addCallBack(
                                new EmptyCallback(
                                        new ICallback.CallOnListener() {
                                            @Override
                                            public void statusResponse() {
                                                loadService.showCallback(LoadingCallback.class);

                                                singleThreadPool.execute(
                                                        () -> {
                                                            try {
                                                                Thread.sleep(3000L);
                                                                loadService.showDefault();
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                        });
                                            }
                                        }))
                        .addCallBack(new LoadingCallback())
                        .build();
        loadService = manager.bind(this, getLifecycle());
        loadService.setCallback(
                EmptyCallback.class,
                new Function1<View, Unit>() {
                    @Override
                    public Unit invoke(View view) {
                        CallbackEmptyBinding binding = CallbackEmptyBinding.bind(view);
                        binding.callbackMsg.setText("这是改过的消息");
                        binding.callbackTitle.setText("这是改过的标题");
                        return null;
                    }
                });

        loadService.showCallback(LoadingCallback.class);

        singleThreadPool.execute(
                () -> {
                    try {
                        Thread.sleep(3000L);
                        loadService.showCallback(EmptyCallback.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void initData() {}
}
