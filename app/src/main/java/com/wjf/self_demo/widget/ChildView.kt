package com.wjf.self_demo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class ChildView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr),
    View.OnClickListener {
    init {
        setOnClickListener(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ChildView dispatchTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("wjftc", "ChildView dispatchTouchEvent ACTION_MOVE")
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ChildView dispatchTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ChildView dispatchTouchEvent ACTION_CANCEL")
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
//        Log.d("wjftc","ChildView onTouchEvent:$result")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ChildView onTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("wjftc", "ParentLayout onTouchEvent ACTION_MOVE")
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ChildView onTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ChildView onTouchEvent ACTION_CANCEL")
            }
        }
        return result
    }

    override fun onClick(v: View?) {
        Log.w("wjftc", "ChildView onClick")
    }
}
