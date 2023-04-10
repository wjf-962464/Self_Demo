package org.jxxy.debug.corekit.common

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.orhanobut.logger.Logger
import org.jxxy.debug.corekit.R

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class BaseDialog<T : ViewBinding> :
    DialogFragment() {

    var gravity = Gravity.CENTER

    @StyleRes
    var windowAnimations = R.style.CommonDialogAnimation
    var width = WindowManager.LayoutParams.MATCH_PARENT
    var height = WindowManager.LayoutParams.WRAP_CONTENT
    var ifCancelOnTouch = false
    var enableBack = false
    var alpha: Float = 0.25f

    protected lateinit var view: T
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setDialogStyle(dialog)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BaseDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = bindLayout(inflater)
        initView()
        return view.root
    }

    /**
     * 初始化视图
     */
    protected abstract fun initView()

    /**
     * 指定布局文件
     */
    protected abstract fun bindLayout(inflater: LayoutInflater): T

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val fragment = manager.findFragmentByTag(tag)
            if (fragment == null || !fragment.isAdded) {
                // 在每个add事务前增加一个remove事务，防止连续的add
                manager.beginTransaction().remove(this).commitAllowingStateLoss()

                val trans = manager.beginTransaction()
                trans.add(this, tag)
                trans.commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            // 同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace()
            Logger.e(e, "Dialog show error")
        }
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    protected open fun setDialogStyle(dialog: Dialog) {
        with(dialog) {
            window?.apply {
                decorView.setPadding(0, 0, 0, 0)
                setWindowAnimations(windowAnimations)
                setDimAmount(alpha)
                setGravity(gravity)

                setLayout(this@BaseDialog.width, this@BaseDialog.height)
            }
            setCanceledOnTouchOutside(ifCancelOnTouch)
            setOnKeyListener { _, keyCode, _ ->
                if (enableBack) {
                    false
                } else {
                    keyCode == KeyEvent.KEYCODE_BACK // true为屏蔽
                }
            }
        }
    }
}
