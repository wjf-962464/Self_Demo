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
    private EditDialogListener listener;


    public EditTextDialog(@NonNull Builder builder) {
        super(builder);
        this.listener=builder.listener;
    }

    @Override
    protected void initView(DialogEditBinding view) {
        view.submitBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.submit(view.inputEdit.getText().toString().trim());
            }
            view.inputEdit.setText("");
            dismiss();
        });
    }




    @Override
    protected int setLayout() {
        return R.layout.dialog_edit;
    }

    public interface EditDialogListener {
        void submit(String result);
    }

    public static class Builder extends BaseDialog.Builder<EditTextDialog>{
        private EditDialogListener listener;

        public Builder(@NonNull Context context) {
            super(context);
        }

        public Builder setListener(EditDialogListener listener){
            this.listener=listener;
            return this;
        }

        @Override
        public EditTextDialog build() {
            return new EditTextDialog(this);
        }
    }
}
