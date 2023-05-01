package com.wjf.self_demo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class ChildView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr), View.OnClickListener {
    init {
        isClickable = false
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                Log.d("wjftc", "onViewAttachedToWindow: $parent")
                parent.requestDisallowInterceptTouchEvent(true)
            }

            override fun onViewDetachedFromWindow(v: View?) {
            }
        })
//        setOnClickListener(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(ev)
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ChildView dispatchTouchEvent ACTION_DOWN $result")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("wjftc", "ChildView dispatchTouchEvent ACTION_MOVE $result")
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ChildView dispatchTouchEvent ACTION_UP $result")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ChildView dispatchTouchEvent ACTION_CANCEL $result")
            }
        }
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
//        Log.d("wjftc","ChildView onTouchEvent:$result")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ChildView onTouchEvent ACTION_DOWN $result")
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                Log.i("wjftc", "ChildView onTouchEvent ACTION_MOVE $result")
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ChildView onTouchEvent ACTION_UP $result")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ChildView onTouchEvent ACTION_CANCEL $result")
            }
        }
        return result
    }

    override fun onClick(v: View?) {
        Log.w("wjftc", "ChildView onClick")
    }
}
