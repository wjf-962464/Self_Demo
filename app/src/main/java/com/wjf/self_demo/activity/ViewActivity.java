package com.wjf.self_demo.activity;

import android.graphics.Bitmap;
import android.view.ViewTreeObserver;

import com.wjf.barcode.encode.EncodingUtils;
import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.ActivityViewBinding;
import com.wjf.self_library.common.BaseActivity;

/** @author Wangjf2-DESKTOP */
public class ViewActivity extends BaseActivity<ActivityViewBinding> {

    @Override
    public int setLayout() {
        return R.layout.activity_view;
    }

    @Override
    protected void initView() {



        EncodingUtils.bindBarCode("sadad",view.barcode);

    }

    @Override
    protected void initData() {}
}
