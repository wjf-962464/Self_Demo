package com.wjf.self_demo.activity;

import com.wjf.loadLayout.callback.ICallback;
import com.wjf.loadLayout.callback.SuccessCallback;
import com.wjf.loadLayout.core.LoadLayoutManager;
import com.wjf.loadLayout.core.LoadService;
import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityLoadLayoutBinding;
import com.wjf.self_demo.loadlayout.EmptyCallback;
import com.wjf.self_library.common.BaseActivity;

/** @author WJF */
public class LoadLayoutActivity extends BaseActivity<ActivityLoadLayoutBinding> {
    private LoadService loadService;

    @Override
    public int setLayout() {
        return R.layout.activity_load_layout;
    }

    @Override
    protected void initView() {
        LoadLayoutManager manager =
                new LoadLayoutManager.Builder()
                        .addCallBack(
                                new EmptyCallback(
                                        new ICallback.CallOnListener() {
                                            @Override
                                            public void statusResponse() {
                                                loadService.showCallback(SuccessCallback.class);
                                            }
                                        }))
                        .build();
        loadService = manager.bind(this, getLifecycle());
        loadService.showCallback(EmptyCallback.class);
    }

    @Override
    protected void initData() {}
}
