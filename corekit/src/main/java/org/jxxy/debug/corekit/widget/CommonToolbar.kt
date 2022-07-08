package org.jxxy.debug.corekit.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import org.jxxy.debug.corekit.R
import org.jxxy.debug.corekit.common.BaseUi
import org.jxxy.debug.corekit.databinding.UiCommonToolbarBinding
import org.jxxy.debug.corekit.util.*

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
class CommonToolbar(context: Context, attrs: AttributeSet?) : BaseUi<UiCommonToolbarBinding>(
    context, attrs
) {
    private var titleText: String? = null
    private var backText: String? = null
    var isDarkTheme = false
        private set
    private var ifBack = false

    @ColorInt
    var backgroundNormalColor = 0
        private set

    @ColorInt
    var backgroundChangeColor = 0
        private set

    var toolbarBackCallback: ToolbarBackCallback? = null

    override fun bindLayout(inflater: LayoutInflater, parent: ViewGroup) =
        UiCommonToolbarBinding.inflate(inflater, parent, true)

    override fun setStyleable(): IntArray {
        return R.styleable.CommonToolbar
    }

    override fun getTypeArray(typedArray: TypedArray) {
        titleText = typedArray.getString(R.styleable.CommonToolbar_titleText)
        backText = typedArray.getString(R.styleable.CommonToolbar_backText)
        isDarkTheme = typedArray.getBoolean(R.styleable.CommonToolbar_dark_theme, true)
        ifBack = typedArray.getBoolean(R.styleable.CommonToolbar_if_back, true)
        backgroundNormalColor =
            typedArray.getColor(R.styleable.CommonToolbar_background_color, Color.TRANSPARENT)
        backgroundChangeColor = typedArray.getColor(
            R.styleable.CommonToolbar_background_change_color, Color.TRANSPARENT
        )
    }

    fun setTitleText(titleText: String?) {
        this.titleText = titleText
        if (this.titleText.isNullOrEmpty()) {
            view.toolbarTitle.hide()
        } else {
            view.toolbarTitle.show()
            view.toolbarTitle.text = titleText
        }
    }

    override fun setView() {
        val themeColor =
            if (isDarkTheme) ResourceUtil.getColor(R.color.black)
            else ResourceUtil.getColor(R.color.white)

        if (!ifBack) {
            view.toolbarBackIcon.gone()
            view.toolbarBack.gone()
        } else {
            if (backText.isNullOrEmpty()) {
                view.toolbarBack.gone()
            } else {
                view.toolbarBack.show()
                view.toolbarBack.text = backText
                view.toolbarBack.setTextColor(themeColor)
            }

            view.toolbarBackIcon.show()
            view.toolbarBackIcon.singleClick(increase = true) {
                toolbarBackCallback?.run {
                    backActivity(context)
                    return@singleClick
                }
                (context as? Activity)?.finish()
            }
            view.toolbarBackIcon.setTextColor(themeColor)
        }

        setTitleText(titleText)
        view.toolbarTitle.setTextColor(themeColor)

        setBackgroundColor(backgroundNormalColor)
    }

    interface ToolbarBackCallback {
        /**
         * 返回事件回调
         *
         * @param context 上下文对象
         */
        fun backActivity(context: Context?)
    }
}
