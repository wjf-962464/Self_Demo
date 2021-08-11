package com.wjf.self_demo.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.DialogMyBinding;
import com.wjf.self_library.common.BaseDialog;

public class MyDialog extends BaseDialog<DialogMyBinding> {
    private final Builder builder;

    public MyDialog(@NonNull Builder builder) {
        super(builder);
        this.builder = builder;
    }

    @Override
    protected void initView(DialogMyBinding view) {
        view.tvAddress.setText(builder.address);
        view.tvHint.setText(builder.hint);
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_my;
    }

    public static class Builder extends BaseDialog.Builder<MyDialog> {
        private String address = "";
        private String hint = "";

        public Builder(@NonNull Context context) {
            super(context);
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        @Override
        public MyDialog build() {
            return new MyDialog(this);
        }
    }
}
