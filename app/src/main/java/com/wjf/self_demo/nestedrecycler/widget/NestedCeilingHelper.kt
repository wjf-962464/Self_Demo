package com.wjf.self_demo.nestedrecycler.widget

import android.view.View
import com.wjf.self_demo.R

object NestedCeilingHelper {
    /** Log开关  */
     const val DEBUG = true

    // 使用自带的overScroll来传递fling事件
    const val USE_OVER_SCROLL = true

    /**
     * 将标记为
     *
     * @param view
     */
    fun setNestedChildContainerTag(view: View) {
        view.setTag(R.id.nested_child_item_container, java.lang.Boolean.TRUE)
    }

    /**
     * 判断是否是包裹类
     *
     * @param view
     * @return
     */
    fun isNestedChildContainerTag(view: View): Boolean {
        return view.getTag(R.id.nested_child_item_container) == java.lang.Boolean.TRUE ||
            view is NestedChildItemContainer
    }

    /**
     * 重写高度
     *
     * @param view
     * @param heightSpec
     * @return
     */
    fun wrapContainerMeasureHeight(view: View, heightSpec: Int): Int {
        val parent = view.parent as? NestedParentRecyclerView ?: return heightSpec
        require(View.MeasureSpec.getMode(heightSpec) == View.MeasureSpec.UNSPECIFIED) { "$view must be exactly height, layoutParam must be MATCH_PARENT" }
        val parentHeight = View.MeasureSpec.getSize(heightSpec)
        val parentTopOffset = parent.topOffset
        val newHeightSpec = if (parentHeight > parentTopOffset) {
            View.MeasureSpec.makeMeasureSpec(
                parentHeight - parentTopOffset,
                View.MeasureSpec.EXACTLY
            )
        } else {
            View.MeasureSpec.makeMeasureSpec(
                parentHeight,
                View.MeasureSpec.EXACTLY
            )
        }
        return newHeightSpec
    }
}
