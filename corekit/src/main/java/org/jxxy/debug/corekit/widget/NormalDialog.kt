package org.jxxy.debug.corekit.widget

import android.content.Context
import android.view.LayoutInflater
import org.jxxy.debug.corekit.common.BaseDialog
import org.jxxy.debug.corekit.databinding.DialogCommonBinding
import org.jxxy.debug.corekit.util.singleClick

class NormalDialog(context: Context) : BaseDialog<DialogCommonBinding>(context) {

    override fun bindLayout(inflater: LayoutInflater): DialogCommonBinding {
        return DialogCommonBinding.inflate(inflater)
    }

    var listener: NormalDialogListener? = null
    var positiveText: String? = null
    var negativeText: String? = null

    override fun initView() {
        view.dialogTitle.text = getData(TITLE)
        view.dialogMessage.text = getData(MESSAGE)
        view.positiveBtn.text = positiveText
        view.positiveBtn.singleClick {
            listener?.onPositiveClick()
        }
        view.negativeBtn.text = negativeText
        view.negativeBtn.singleClick {
            listener?.onNegativeClick()
        }
    }

    companion object {
        const val TITLE = "title"
        const val MESSAGE = "message"
    }

    interface NormalDialogListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }
}
