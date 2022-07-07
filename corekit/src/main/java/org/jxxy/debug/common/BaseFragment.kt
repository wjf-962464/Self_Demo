package org.jxxy.debug.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.jxxy.debug.component.corekit.BuildConfig
import org.jxxy.debug.component.corekit.R
import org.jxxy.debug.util.nullOrNot
import org.jxxy.debug.widget.CommonToolbar
import org.jxxy.debug.widget.statusBar.StatusBarUtil

/**
 * @author : WJF
 * @date : 2021/1/19
 */
abstract class BaseFragment<T : ViewBinding> : Fragment() {
    protected lateinit var find: T

    /** 封装toolbar  */
    protected var commonToolbar: CommonToolbar? = null
    private var isLoaded = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindLayout()
        initColorStatusBar(find.root)
        return find.root
    }

    private fun initColorStatusBar(view: View) {
        commonToolbar = view.findViewById(R.id.commonToolbar)
        commonToolbar?.nullOrNot(
            ifNull = {
                // 没有标题栏，默认判定为透明且黑色主题
                darkTheme(true)
            }
        ) {
            val color = it.backgroundNormalColor
            if (color == 0) {
                // 透明，默认同样透明主题
                StatusBarUtil.setTranslucentStatus(activity, it.isDarkTheme)
            } else {
                StatusBarUtil.setStatusBarColor(activity, color, it.isDarkTheme)
            }
        }
    }

    /** 黑色主题透明状态栏  */
    protected fun darkTheme(dark: Boolean) {
        StatusBarUtil.setTranslucentStatus(activity, dark)
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
            subscribeUi()
            isLoaded = true
            Log.i(BuildConfig.TAG, "---" + this.javaClass.canonicalName + "加载")
        }
    }

    /** 绑定viewBinding  */
    protected abstract fun bindLayout()

    /** 初始化控件  */
    protected abstract fun initView()

    /** 订阅ViewModel  */
    protected abstract fun subscribeUi()
}
