package com.wjf.self_demo.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.DialogMyBinding;
import com.wjf.self_library.common.BaseDialog;

/**
 * @author WJF
 */
public class MyDialog extends BaseDialog<DialogMyBinding> {


    public MyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView(DialogMyBinding view) {
        view.tvAddress.setText(getData("address"));
        view.tvHint.setText(getData("hint"));

    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_my;
    }
}
