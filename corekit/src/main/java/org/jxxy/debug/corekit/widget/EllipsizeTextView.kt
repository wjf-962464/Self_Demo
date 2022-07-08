package org.jxxy.debug.corekit.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import org.jxxy.debug.corekit.R

/**
 * 根据字符数进行尾部处理的TextView
 */
class EllipsizeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(
    context, attrs, defStyleAttr
) {
    companion object {
        // 缩略模式，即...
        private const val MODE_END = 0x01

        // 纯粹模式，直接截断
        private const val MODE_NONE = 0x02
    }

    /**
     * 最大字符数，默认为0，即不处理
     */
    private var ellipsizeMax: Int = 0

    /**
     * 截断模式
     */
    private var ellipsizeMode: Int = 0x01

    init {
        initAttrs(attrs)
    }

    /**
     * 初始化Attrs
     */
    @SuppressLint("CustomViewStyleable")
    private fun initAttrs(attrs: AttributeSet?) {
        val t = context.obtainStyledAttributes(
            attrs,
            R.styleable.EllipsizeTextView
        )
        ellipsizeMax = t.getInt(R.styleable.EllipsizeTextView_ellipsize_max, 0)
        ellipsizeMode = t.getInt(R.styleable.EllipsizeTextView_ellipsize_mode, MODE_END)
        t.recycle()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (ellipsizeMax <= 0 || (text?.length ?: 0) <= ellipsizeMax) {
            super.setText(text, type)
            return
        }
        var newText: String = text.toString()
        when (ellipsizeMode) {
            MODE_END -> {
                // 缩略...
                newText = newText.substring(0, ellipsizeMax) + "..."
            }
            MODE_NONE -> {
                // 截断
                newText = newText.substring(0, ellipsizeMax)
            }
        }
        super.setText(newText, type)
    }
}
