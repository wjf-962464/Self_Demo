package com.wjf.self_library.view.codingUI

import android.content.Context
import com.wjf.self_library.R
import com.wjf.self_library.common.BaseDialog
import com.wjf.self_library.common.click
import com.wjf.self_library.databinding.DialogCommonBinding

class NormalDialog(context: Context) : BaseDialog<DialogCommonBinding>(context) {

    override fun setLayout(): Int {
        return R.layout.dialog_common
    }

    fun setPositiveButton(
        text: String,
        listener: (NormalDialog) -> Unit
    ): NormalDialog {
        view.positiveBtn.text = text
        view.positiveBtn.singleClick {
            listener(this@NormalDialog)
        }
        return this
    }

    fun setNegativeButton(
        text: String,
        listener: (NormalDialog) -> Unit
    ): NormalDialog {
        view.negativeBtn.text = text
        view.negativeBtn.singleClick {
            listener(this@NormalDialog)
        }
        return this
    }

    override fun initView(view: DialogCommonBinding) {
        view.dialogTitle.text = getData(TITLE)
        view.dialogMessage.text = getData(MESSAGE)
    }

    companion object {
        const val TITLE = "title"
        const val MESSAGE = "message"
    }
}
