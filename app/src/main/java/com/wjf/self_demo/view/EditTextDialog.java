package com.wjf.self_demo.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.wjf.self_demo.R;
import com.wjf.self_demo.databinding.DialogEditBinding;
import com.wjf.self_library.common.BaseDialog;

/**
 * @author WJF
 */
public class EditTextDialog extends BaseDialog<DialogEditBinding> {


    public EditTextDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView(DialogEditBinding view) {

    }

    public EditTextDialog setSubmitListener(BaseDialog.DialogClickListener<String> listener){
        view.submitBtn.setOnClickListener(v->{
            listener.onClick(view.inputEdit.getText().toString().trim());
            view.inputEdit.setText("");
            dismiss();
        });
        return this;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_edit;
    }

}
