package com.wjf.self_demo.nestedrecycler.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.NestedPublicRecyclerView
import androidx.recyclerview.widget.RecyclerView
import com.wjf.self_demo.nestedrecycler.widget.FindTarget.findChildScrollTarget
import kotlin.math.roundToInt

/** 嵌套滑动的父View  */
class NestedParentRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedPublicRecyclerView(context, attrs, defStyleAttr),
    NestedScrollingParent3,
    NestedScrollingParent2 {
    private lateinit var mParentHelper: NestedScrollingParentHelper
    private lateinit var mFlingHelper: FlingHelper
    private var mContentView: ViewGroup? = null
    private var mTotalDy = 0

    // 记录y轴加速度
    private var mVelocityY = 0
    private var mActivePointerId = 0
    private var mLastY = 0f
    private var mIsStartChildFling = false
    private var mIsChildAttachedToTop = false
    private var mIsChildDetachedFromTop = true
    private val mOnChildAttachStateListeners = ArrayList<OnChildAttachStateListener>()
    private val mTempConsumed = IntArray(2)
    private val mNestedScrollingV2ConsumedCompat = IntArray(2)

    /**
     * 距离嵌套滑动距离顶部应该流出的空间
     *
     */
    var topOffset = 0
        set(value) {
            field = value
            mContentView?.requestLayout()
        }
    private var mIsChildNestedScrolling = false
    private val mTempLocation = IntArray(2)
    private fun setup() {
        mParentHelper = NestedScrollingParentHelper(this)
        mFlingHelper = FlingHelper(context)
        overScrollMode = OVER_SCROLL_ALWAYS
    }

    fun addOnChildAttachStateListener(listener: OnChildAttachStateListener) {
        mOnChildAttachStateListeners.add(listener)
    }

    private fun isTargetContainer(child: View): Boolean {
        return NestedCeilingHelper.isNestedChildContainerTag(child)
    }

    override fun onChildAttachedToWindow(child: View) {
        if (isTargetContainer(child)) {
            mContentView = child as? ViewGroup
            var lp = child.layoutParams
            if (lp == null) {
                lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            child.layoutParams = lp
            if (NestedCeilingHelper.DEBUG) {
                log("onChildAttachedToWindow....")
            }
        }
    }

    override fun onChildDetachedFromWindow(child: View) {
        if (child === mContentView) {
            mContentView = null
            log("onChildDetachedFromWindow....")
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val isTouchInContent = (
            mContentView != null &&
                // 判断是否在区域内，如不在子View，则直接由自身处理
                e.y > mContentView!!.top &&
                e.y < mContentView!!.bottom
            )
        val targetView: View? = if (isTouchInContent) findChildScrollTarget(mContentView) else null
        var isTouchInChildArea = false
        if (targetView != null) {
            targetView.getLocationOnScreen(mTempLocation)
            val left = mTempLocation[0]
            val top = mTempLocation[1]
            val right = left + targetView.width
            val bottom = top + targetView.height
            val x = e.rawX
            val y = e.rawY
            isTouchInChildArea = x >= left && x <= right && y >= top && y <= bottom
        }
        // 此控件滑动到底部或者触摸区域在子嵌套布局不拦截事件
        if (isTouchInChildArea) {
            if (scrollState == SCROLL_STATE_SETTLING) {
                // 上划fling过程中，停止，否则会抖动
                stopScroll()
            }
            return false
        }
        return super.onInterceptTouchEvent(e)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastY = e.y
                mActivePointerId = e.getPointerId(0)
                mVelocityY = 0
                stopScroll()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = e.actionIndex
                mLastY = e.getY(index)
                mActivePointerId = e.getPointerId(index)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = e.actionIndex
                val pointerId = e.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastY = e.getY(newPointerIndex)
                    mActivePointerId = e.getPointerId(newPointerIndex)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = e.findPointerIndex(mActivePointerId)
                if (activePointerIndex == -1) {
                    log("Invalid pointerId=$mActivePointerId in onTouchEvent")
                } else {
                    val y = e.getY(activePointerIndex)
                    if (isScrollEnd) {
                        // 如果此控件已经滑动到底部，需要让子嵌套布局滑动剩余的距离
                        // 或者子嵌套布局向下还未到顶部，也需要让子嵌套布局先滑动一段距离
                        val child = findChildScrollTarget(mContentView)
                        if (child != null) {
                            val deltaY = (mLastY - y).toInt()
                            mTempConsumed[1] = 0
                            child.doScrollConsumed(0, deltaY, mTempConsumed)
                            val consumedY = mTempConsumed[1]
                            if (consumedY != 0 && NestedCeilingHelper.DEBUG) {
                                log("onTouch scroll consumed: $consumedY")
                            }
                        }
                    }
                    mLastY = y
                }
            }
        }
        return super.onTouchEvent(e)
    }

    private val isScrollEnd: Boolean
        get() = !canScrollVertically(1)

    private fun isChildScrollTop(child: RecyclerView): Boolean {
        return !child.canScrollVertically(-1)
    }

    override fun onScrolled(dx: Int, dy: Int) {
        if (mIsStartChildFling) {
            mTotalDy = 0
            mIsStartChildFling = false
        }
        mTotalDy += dy
        val attached = dy > 0 && isScrollEnd
        if (attached && mIsChildDetachedFromTop) {
            mIsChildAttachedToTop = true
            mIsChildDetachedFromTop = false
            for (listener in mOnChildAttachStateListeners) {
                listener.onChildAttachedToTop()
            }
        }
        val detached = dy < 0 && !isScrollEnd
        if (detached && mIsChildAttachedToTop) {
            val child: RecyclerView? = findChildScrollTarget(mContentView)
            if (child == null || isChildScrollTop(child)) {
                mIsChildDetachedFromTop = true
                mIsChildAttachedToTop = false
                for (listener in mOnChildAttachStateListeners) {
                    listener.onChildDetachedFromTop()
                }
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            if (NestedCeilingHelper.USE_OVER_SCROLL) {
                dispatchChildState(SCROLL_STATE_IDLE)
            } else {
                dispatchChildFling()
            }
        } else {
            dispatchChildState(state)
        }
    }

    override fun onFlingEnd(velocityX: Int, velocityY: Int) {
        super.onFlingEnd(velocityX, velocityY)
        if (velocityY > 0 && NestedCeilingHelper.USE_OVER_SCROLL) {
            // 通过OverScroll传递滚动状态
            val child: RecyclerView? = findChildScrollTarget(mContentView)
            if (child != null) {
                if (NestedCeilingHelper.DEBUG) {
                    log("onFlingEnd fling child velocityY: $velocityY")
                }
                child.fling(0, velocityY)
            }
        }
    }

    private fun dispatchChildState(state: Int) {
        if (mIsChildNestedScrolling) {
            return
        }
        val child = findChildScrollTarget(mContentView)
        if (child != null && !child.isFling) {
            child.updateScrollState(state)
        }
    }

    private fun dispatchChildFling() {
        var isChildFling = false
        if (mVelocityY != 0 && isScrollEnd) {
            val splineFlingDistance = mFlingHelper.getSplineFlingDistance(mVelocityY)
            if (splineFlingDistance > mTotalDy) {
                childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - mTotalDy))
                isChildFling = true
            }
        }
        mTotalDy = 0
        mVelocityY = 0
        if (!isChildFling) {
            dispatchChildState(SCROLL_STATE_IDLE)
        }
    }

    private fun childFling(velocityY: Int) {
        val child: RecyclerView? = findChildScrollTarget(mContentView)
        child?.fling(0, velocityY)
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val fling = super.fling(velocityX, velocityY)
        if (!fling || velocityY <= 0) {
            mVelocityY = 0
        } else {
            mIsStartChildFling = true
            mVelocityY = velocityY
        }
        return fling
    }

    override fun onStartNestedScroll(
        child: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        val isStart = (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
        if (NestedCeilingHelper.DEBUG) {
            log("onStartNestedScroll type: $type, scrollState: $scrollState")
        }
        if (isStart && type == ViewCompat.TYPE_TOUCH && scrollState == SCROLL_STATE_SETTLING) {
            // 子view引起嵌套滑动是可能在fling，stop it
            stopScroll()
        }
        if (isStart) {
            mIsChildNestedScrolling = true
        }
        return isStart
    }

    override fun onNestedScrollAccepted(
        child: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type)
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type)
    }

    override fun onStartNestedScroll(
        child: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedScrollAccepted(
        child: View,
        target: View,
        nestedScrollAxes: Int
    ) {
        onNestedScrollAccepted(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        val isParentScroll = dispatchNestedPreScroll(dx, dy, consumed, null, type)
        // 在父嵌套布局没有滑动时，处理此控件是否需要滑动
        if (isParentScroll) {
            return
        }
        // 向上滑动且此控件没有滑动到底部时，需要让此控件继续滑动以保证滑动连贯一致性
        val needKeepScroll = dy > 0 // && !isScrollEnd();
        if (needKeepScroll) {
            mTempConsumed[1] = 0
            doScrollConsumed(0, dy, mTempConsumed)
            consumed[1] = mTempConsumed[1]
            updateScrollState(
                if (type == ViewCompat.TYPE_TOUCH) SCROLL_STATE_DRAGGING else SCROLL_STATE_SETTLING
            )
            if (NestedCeilingHelper.DEBUG) {
                val offset = computeVerticalScrollOffset()
                val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
                log(
                    "onNestedPreScroll dy:" +
                        dy +
                        ", consumedY: " +
                        consumed[1] +
                        ", type:" +
                        type +
                        ", isScrollEnd: " +
                        isScrollEnd +
                        ", offset:" +
                        offset +
                        ", range:" +
                        range
                )
            }
        } else {
            if (NestedCeilingHelper.DEBUG) {
                val offset = computeVerticalScrollOffset()
                val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
                log(
                    "onNestedPreScroll not dy:" +
                        dy +
                        ", type:" +
                        type +
                        ", isScrollEnd: " +
                        isScrollEnd +
                        ", offset:" +
                        offset +
                        ", range:" +
                        range
                )
            }
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            ViewCompat.TYPE_TOUCH
        )
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            mNestedScrollingV2ConsumedCompat
        )
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        onNestedScrollInternal(target, dyUnconsumed, type, consumed)
    }

    /**
     * dyUnconsumed != 0时，嵌套的子view，表示嵌套的子view不能滑动，也就是到顶了，大于0表示下滑，小于0表示上划
     *
     * @param target
     * @param dyUnconsumed
     * @param type
     * @param consumed
     */
    private fun onNestedScrollInternal(
        target: View,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyUnconsumed == 0) {
            return
        }
        mTempConsumed[1] = 0
        doScrollConsumed(0, dyUnconsumed, mTempConsumed)
        val consumedY = mTempConsumed[1]
        consumed[1] += consumedY
        val myUnconsumedY = dyUnconsumed - consumedY
        dispatchNestedScroll(0, consumedY, 0, myUnconsumedY, null, type, consumed)
        if (NestedCeilingHelper.DEBUG) {
            log(
                "onNestedScrollInternal dyUnconsumed:" +
                    dyUnconsumed +
                    ", consumedY:" +
                    consumedY +
                    ", myUnconsumedY:" +
                    myUnconsumedY +
                    ", type:" +
                    type
            )
        }
        if (dyUnconsumed < 0 && type == ViewCompat.TYPE_TOUCH) {
            updateScrollState(SCROLL_STATE_DRAGGING)
        }

        // dyUnconsumed 大于0是下滑，小于0是上划
        if (dyUnconsumed < 0 && type == ViewCompat.TYPE_NON_TOUCH && target is NestedChildRecyclerView) {
            if (target != findChildScrollTarget(mContentView)) {
                log("onNestedScrollInternal nestedView is changed, return")
                return
            }
            val overScroller = target.flingOverScroll ?: return
            val absVelocity = overScroller.currVelocity
            // nestedView.stopScroll();
            // 停止，但不更新状态，因为fling时在onStateChanged中要更新子view的状态
            target.stopScrollWithoutState()
            val myVelocity = absVelocity * -1
            fling(0, myVelocity.roundToInt())
            if (NestedCeilingHelper.DEBUG) {
                log(
                    "onNestedScrollInternal start fling from child, absVelocity:" +
                        absVelocity +
                        ", myVelocity:" +
                        myVelocity
                )
            }
        }
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        if (NestedCeilingHelper.DEBUG) {
            log("onStopNestedScroll type: $type, scrollState: $scrollState")
        }
        if (type == ViewCompat.TYPE_TOUCH && scrollState == SCROLL_STATE_SETTLING) {
            // 停止是可能正在fling，需要手动停止
            log("onStopNestedScroll stop it")
            stopScroll()
        } else if (flingOverScroll != null && flingOverScroll!!.isFinished &&
            target is NestedChildRecyclerView &&
            target.scrollState == SCROLL_STATE_IDLE
        ) {
            // 嵌套滑动停止时要将状态至为idle状态
            // ((NestedChildRecyclerView) target).getScrollState() ==
            updateScrollState(SCROLL_STATE_IDLE)
        }
        mParentHelper.onStopNestedScroll(target, type)
        stopNestedScroll(type)
        mIsChildNestedScrolling = false
    }

    // NestedScrollingParent
    override fun onStopNestedScroll(target: View) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        log("onNestedFling velocityY: $velocityY, consumed: $consumed")
        if (!consumed) {
            dispatchNestedFling(0f, velocityY, true)
            fling(0, velocityY.toInt())
            return true
        }
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun getNestedScrollAxes(): Int {
        return mParentHelper.nestedScrollAxes
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {
        const val TAG = "NestedParentRecycler"
    }

    init {
        setup()
    }
}
