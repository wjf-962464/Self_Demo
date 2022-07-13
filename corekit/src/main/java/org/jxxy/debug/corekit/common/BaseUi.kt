package org.jxxy.debug.corekit.common

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class BaseUi<T : ViewBinding> protected constructor(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    protected lateinit var view: T
        private set

    /**
     * 设置布局
     *
     * @return 布局资源id
     */
    protected abstract fun bindLayout(inflater: LayoutInflater, parent: ViewGroup): T

    /**
     * 设置样式
     *
     * @return 返回样式资源id
     */
    protected abstract fun setStyleable(): IntArray

    /**
     * 获取样式类型
     *
     * @param typedArray 样式数组
     */
    protected abstract fun getTypeArray(typedArray: TypedArray)

    /**
     * 设置View
     */
    protected abstract fun setView()

    private fun initWidget(attrs: AttributeSet?) {
        view = bindLayout(LayoutInflater.from(context), this)
        getTypeArray(context.obtainStyledAttributes(attrs, setStyleable()))
        setView()
    }

    init {
        initWidget(attrs)
    }
}
