package com.wjf.self_demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wjf.barcode.Logger;
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

    @Nullable
    @Override
    public View onCreateView(
            @Nullable View parent,
            @NonNull String name,
            @NonNull Context context,
            @NonNull AttributeSet attrs) {
        Logger.d("onCreateView111");
        return super.onCreateView(parent, name, context, attrs);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        Logger.d("onCreateView222");
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate");
    }

    @Override
    protected void initView() {
        Logger.d("onCreateView");
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
