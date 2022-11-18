package com.wjf.self_demo.view

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.wjf.self_demo.databinding.DialogEditBinding
import org.jxxy.debug.corekit.common.BaseDialog
import org.jxxy.debug.corekit.util.singleClick

/** @author WJF
 */
class EditTextDialog : BaseDialog<DialogEditBinding>() {
    init {
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        ifCancelOnTouch = true
        gravity = Gravity.BOTTOM
    }

    var listener: ((String) -> Unit)? = null
    fun setSubmitListener(block: (String) -> Unit): EditTextDialog {
        this.listener = block
        return this
    }

    override fun initView() {
        view.submitBtn.singleClick { v: View? ->
            listener?.invoke(view.inputEdit.text.toString().trim { it <= ' ' })
            view.inputEdit.setText("")
            dismiss()
        }
    }

    override fun bindLayout(inflater: LayoutInflater): DialogEditBinding {
        return DialogEditBinding.inflate(inflater)
    }
}
