package com.wjf.self_demo.nestedrecycler.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.NestedPublicRecyclerView

/** 嵌套滑动子View，一定要继承该View  */
class NestedChildRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedPublicRecyclerView(context, attrs, defStyleAttr) {

    init {
        isNestedScrollingEnabled = true
        overScrollMode = OVER_SCROLL_ALWAYS
    }
}
