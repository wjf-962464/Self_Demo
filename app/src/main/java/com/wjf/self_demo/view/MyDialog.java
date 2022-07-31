package com.wjf.self_demo.view;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.wjf.self_demo.databinding.DialogMyBinding;

import org.jxxy.debug.corekit.common.BaseDialog;

/**
 * @author WJF
 */
public class MyDialog extends BaseDialog<DialogMyBinding> {


    public MyDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void initView() {
        getView().tvAddress.setText(getData("address"));
        getView().tvHint.setText(getData("hint"));
    }

    @NonNull
    @Override
    protected DialogMyBinding bindLayout(@NonNull LayoutInflater inflater) {
        return DialogMyBinding.inflate(inflater);
    }
}
