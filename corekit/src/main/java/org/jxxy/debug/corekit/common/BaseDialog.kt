package org.jxxy.debug.corekit.common

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.orhanobut.logger.Logger
import org.jxxy.debug.corekit.R

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class BaseDialog<T : ViewBinding> :
    AppCompatDialogFragment() {

    private var gravity = Gravity.BOTTOM

    @StyleRes
    private var windowAnimations = R.style.BottomDialog_Animation
    private var width = WindowManager.LayoutParams.MATCH_PARENT
    private var height = WindowManager.LayoutParams.MATCH_PARENT
    private var ifCancelOnTouch = false
    private var enableBack = false
    private var alpha: Float = 0.4f
    val map: MutableMap<String, String?> = HashMap()

    protected lateinit var view: T
        private set

    fun gravity(gravity: Int): BaseDialog<*> {
        this.gravity = gravity
        return this
    }

    fun windowAnimations(@StyleRes windowAnimations: Int): BaseDialog<T> {
        this.windowAnimations = windowAnimations
        return this
    }

    fun width(width: Int): BaseDialog<T> {
        this.width = width
        return this
    }

    fun height(height: Int): BaseDialog<T> {
        this.height = height
        return this
    }

    fun cancelOnTouch(ifCanceled: Boolean): BaseDialog<T> {
        ifCancelOnTouch = ifCanceled
        return this
    }

    fun enableBack(enable: Boolean): BaseDialog<T> {
        enableBack = enable
        return this
    }

    fun alpha(alpha: Float): BaseDialog<T> {
        this.alpha = alpha
        return this
    }

    fun addData(key: String, value: String?): BaseDialog<T> {
        map[key] = value
        return this
    }

    fun getData(key: String?): String? {
        return if (map.containsKey(key)) {
            map[key]
        } else ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AppCompatDialog(context, R.style.BaseDialogTheme)
        Logger.d("dialog onCreate")
        setDialogStyle(dialog)
        view = bindLayout(LayoutInflater.from(context))
        dialog.setContentView(view.root)
        initView()
        return dialog
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
        super.show(manager, tag)
        Logger.d("dialog show")
    }

    private fun setDialogStyle(dialog: Dialog) {
        // 获取窗口
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.apply {
            decorView.setPadding(0, 0, 0, 0)
            setDimAmount(alpha)

            attributes = attributes?.also {
                it.windowAnimations = windowAnimations
                it.gravity = gravity
                it.width = width
                it.height = height
            }
        }
        dialog.setCanceledOnTouchOutside(ifCancelOnTouch)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (enableBack) {
                false
            } else {
                keyCode == KeyEvent.KEYCODE_BACK // 默认返回 false，这里false不能屏蔽返回键，改成true就可以了
            }
        }
    }
}
