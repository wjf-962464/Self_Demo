package com.wjf.self_demo.activity;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityHttpBinding;
import com.wjf.self_demo.jetpack.HttpViewModel;
import com.wjf.self_library.common.BaseActivity;

/**
 * @author WJF
 */
public class HttpActivity extends BaseActivity<ActivityHttpBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_http;
    }

    @Override
    protected void initView() {}

    @Override
    protected void initData() {}

    @Override
    protected void subscribeUi() {
        HttpViewModel repository=new HttpViewModel();
        repository.login("180422","123456");
    }
}
