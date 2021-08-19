package com.wjf.self_library.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wjf.self_library.R
import java.util.*

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class BaseDialog<T : ViewDataBinding>(context: Context) :
    Dialog(context, R.style.FullScreenDialogStyle) {

    private var gravity = Gravity.BOTTOM

    @StyleRes
    private var windowAnimations = R.style.BottomDialog_Animation
    private var width = WindowManager.LayoutParams.WRAP_CONTENT
    private var height = WindowManager.LayoutParams.WRAP_CONTENT
    private var ifCancelOnTouch = false
    private val map: MutableMap<String, String?> = HashMap()

    protected lateinit var view: T

    init {
        view = DataBindingUtil.inflate<T>(LayoutInflater.from(context), setLayout(), null, false)
    }

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

    fun addData(key: String, value: String?): BaseDialog<T> {
        map[key] = value
        return this
    }

    fun getData(key: String?): String? {
        return if (map.containsKey(key)) {
            map[key]
        } else ""
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val window = window
        // 设置对话框周围的颜色透明度
        if (window != null) {
            // 去除黑色阴影
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setGravity(gravity)
            // 设置弹出及回收动画
            window.setWindowAnimations(windowAnimations)
            window.setDimAmount(0.4f)
        }
        setCanceledOnTouchOutside(ifCancelOnTouch)
        setContentView(view.root)
        initView(view)
    }

    /**
     * 初始化视图
     *
     * @param view 视图
     */
    protected abstract fun initView(view: T)

    /**
     * 指定布局文件
     *
     * @return R.layout.xxx
     */
    protected abstract fun setLayout(): Int
    override fun show() {
        super.show()
        // 此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        initDialog()
    }

    private fun initDialog() {
        // 获取窗口
        val window = window ?: return
        // 获取配置参数
        val layoutParams = window.attributes
        // 设置为屏幕正中
        layoutParams.gravity = gravity
        // 宽高包裹布局自身
        layoutParams.width = width
        layoutParams.height = height
        // 消除dialog自身隐藏padding
        window.decorView.setPadding(0, 0, 0, 0)
        window.attributes = layoutParams
    }

    interface DialogClickListener<T> {
        /**
         * 点击事情
         *
         * @param value 新的值
         */
        fun onClick(value: T)
    }
}
