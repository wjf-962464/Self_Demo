package org.jxxy.debug.corekit.widget

import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import org.jxxy.debug.corekit.common.BaseDialog
import org.jxxy.debug.corekit.databinding.DialogNormalBinding
import org.jxxy.debug.corekit.util.singleClick

class NormalDialog : BaseDialog<DialogNormalBinding>() {
    companion object {
        private const val TAG = "NormalDialog"
    }

    override fun bindLayout(inflater: LayoutInflater): DialogNormalBinding {
        return DialogNormalBinding.inflate(inflater)
    }

    var listener: NormalDialogListener? = null
    var positiveText: String? = null
    var negativeText: String? = null
    var title: String? = null
    var message: String? = null

    override fun initView() {
        view.dialogTitle.text = title
        view.dialogMessage.text = message
        view.positiveBtn.text = positiveText
        view.positiveBtn.singleClick {
            listener?.onPositiveClick()
        }
        view.negativeBtn.text = negativeText
        view.negativeBtn.singleClick {
            listener?.onNegativeClick()
        }
    }

    interface NormalDialogListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }
}
