package org.jxxy.debug.corekit.widget

import android.content.Context
import android.view.LayoutInflater
import org.jxxy.debug.corekit.databinding.DialogCommonBinding
import org.jxxy.debug.corekit.util.singleClick

class NormalDialog(context: Context) :
    org.jxxy.debug.corekit.common.BaseDialog<DialogCommonBinding>(context) {

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
