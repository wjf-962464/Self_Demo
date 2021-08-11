package com.wjf.self_demo.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wjf.self_demo.BuildConfig;
import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityViewModelBinding;
import com.wjf.self_demo.jetpack.NameViewModel;
import com.wjf.self_library.common.BaseActivity;
import com.wjf.self_library.util.log.LogUtil;

/** @author asus */
public class ViewModelActivity extends BaseActivity<ActivityViewModelBinding> {
    private NameViewModel model;
    private final boolean flag = false;

    @Override
    public int setLayout() {
        return R.layout.activity_view_model;
    }

    @Override
    protected void initView() {

        model = new ViewModelProvider(this).get(NameViewModel.class);
        view.setModel(model);
        model.getCurrentName().postValue(model.getCurrentName().getValue() + "1");
        model.getCurrentName()
                .observe(
                        this,
                        new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                LogUtil.d(BuildConfig.TAG, this.getClass(), "onChanged: " + s);
                            }
                        });

        //        model = ViewModelProviders.of(this).get(NameViewModel.class);
        LogUtil.d(BuildConfig.TAG, this.getClass(), "model: " + model.getCurrentName().getValue());
    }

    @Override
    protected void initData() {}

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        LogUtil.d(BuildConfig.TAG, this.getClass(), "onRetainCustomNonConfigurationInstance: ");
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Nullable
    @Override
    public Object getLastNonConfigurationInstance() {
        LogUtil.d(BuildConfig.TAG, this.getClass(), "getLastNonConfigurationInstance: ");
        return super.getLastNonConfigurationInstance();
    }
}
