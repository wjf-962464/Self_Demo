package com.wjf.self_demo.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.DialogEditBinding;
import com.wjf.self_library.common.BaseDialog;

/** @author WJF */
public class EditTextDialog extends BaseDialog<DialogEditBinding> {

    public EditTextDialog(@NonNull Context context) {
        super(context);
    }

    public EditTextDialog setSubmitListener(BaseDialog.DialogClickListener<String> listener) {
        view.submitBtn.setOnClickListener(
                v -> {
                    listener.onClick(getView().inputEdit.getText().toString().trim());
                    getView().inputEdit.setText("");
                    EditTextDialog.this.dismiss();
                });
        return this;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_edit;
    }

    @Override
    protected void initView(@NonNull DialogEditBinding view) {}
}
