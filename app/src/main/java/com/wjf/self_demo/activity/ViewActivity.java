package com.wjf.self_demo.activity;

import androidx.annotation.NonNull;

import com.wjf.self_demo.databinding.ActivityViewBinding;

import org.jxxy.debug.barcode.encode.EncodingUtils;
import org.jxxy.debug.corekit.common.BaseActivity;

/** @author Wangjf2-DESKTOP */
public class ViewActivity extends BaseActivity<ActivityViewBinding> {

    @Override
    protected void initView() {

        EncodingUtils.bindBarCode("sadad", getView().barcode);
    }

    @NonNull
    @Override
    protected ActivityViewBinding bindLayout() {
        return ActivityViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void subscribeUi() {}
}
