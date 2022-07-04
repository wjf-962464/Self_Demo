package org.jxxy.debug.widget

import android.content.Context
import android.view.LayoutInflater
import org.jxxy.debug.common.BaseDialog
import org.jxxy.debug.databinding.DialogCommonBinding
import org.jxxy.debug.util.singleClick

class NormalDialog(context: Context) : BaseDialog<DialogCommonBinding>(context) {

    override fun bindLayout(inflater: LayoutInflater) {
        view = DialogCommonBinding.inflate(inflater)
    }

    fun setPositiveButton(
        text: String,
        listener: (NormalDialog) -> Unit
    ): NormalDialog {
        view.positiveBtn.text = text
        view.positiveBtn.singleClick {
            listener.invoke(this@NormalDialog)
        }
        return this
    }

    fun setNegativeButton(
        text: String,
        listener: (NormalDialog) -> Unit
    ): NormalDialog {
        view.negativeBtn.text = text
        view.negativeBtn.singleClick {
            listener.invoke(this@NormalDialog)
        }
        return this
    }

    override fun initView() {
        view.dialogTitle.text = getData(TITLE)
        view.dialogMessage.text = getData(MESSAGE)
    }

    companion object {
        const val TITLE = "title"
        const val MESSAGE = "message"
    }
}
