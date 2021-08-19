package com.wjf.self_library.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.wjf.self_library.BuildConfig
import com.wjf.self_library.R
import com.wjf.self_library.util.StatusBar.StatusBarUtil
import com.wjf.self_library.view.codingUI.CommonToolbar

/**
 * @author : WJF
 * @date : 2021/1/19
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    protected lateinit var view: T

    /** 封装toolbar  */
    protected var commonToolbar: CommonToolbar? = null
    private var isLoaded = false
    private lateinit var inflater: LayoutInflater
    private var container: ViewGroup? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = DataBindingUtil.inflate(inflater, setLayout(), container, false)
        initColorStatusBar(view.root)
        return view.root
    }

    private fun initColorStatusBar(view: View) {
        commonToolbar = view.findViewById(R.id.commonToolbar)
        commonToolbar?.let {
            val color = it.backgroundColor
            if (color == 0) {
                // 透明，默认同样透明主题
                StatusBarUtil.setTranslucentStatus(activity, it.isDarkTheme)
            } else {
                StatusBarUtil.setStatusBarColor(activity, color, it.isDarkTheme)
            }
        }
    }

    /** 黑色主题透明状态栏  */
    protected fun darkTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(activity, true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            Log.i(BuildConfig.TAG, "---" + this.javaClass.canonicalName + "隐藏")
        }
        lazyInit()
    }

    override fun onResume() {
        super.onResume()
        lazyInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    private fun lazyInit() {
        if (!isLoaded && !isHidden) {
            initView()
            initData()
            subscribeUi()
            isLoaded = true
            Log.i(BuildConfig.TAG, "---" + this.javaClass.canonicalName + "加载")
        }
    }

    /** 订阅ViewModel  */
    protected open fun subscribeUi() {}

    /**
     * 设置布局
     *
     * @return 布局id
     */
    abstract fun setLayout(): Int

    /** 初始化控件  */
    protected abstract fun initView()

    /** 初始化数据  */
    protected abstract fun initData()
    protected fun toastShort(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
