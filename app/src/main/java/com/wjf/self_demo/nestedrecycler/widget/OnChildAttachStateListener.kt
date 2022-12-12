package com.wjf.self_demo.nestedrecycler.widget

interface OnChildAttachStateListener {
    /**
     * 子布局吸附到顶部时回调
     */
    fun onChildAttachedToTop()

    /**
     * 子布局从顶部脱离时回调
     */
    fun onChildDetachedFromTop()
}
