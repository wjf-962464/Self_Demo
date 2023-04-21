package com.wjf.self_demo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class ParentLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {
    init {
        setOnClickListener(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ParentLayout dispatchTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("wjftc", "ParentLayout dispatchTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ParentLayout dispatchTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ParentLayout dispatchTouchEvent ACTION_CANCEL")
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
//        Log.d("wjftc","ParentLayout onTouchEvent:$result")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ParentLayout onTouchEvent ACTION_DOWN $result")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("wjftc", "ParentLayout onTouchEvent ACTION_MOVE $result")
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ParentLayout onTouchEvent ACTION_UP $result")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ParentLayout onTouchEvent ACTION_CANCEL $result")
            }
        }
        return result
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.onInterceptTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("wjftc", "ParentLayout onInterceptTouchEvent ACTION_DOWN  $result")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("wjftc", "ParentLayout onInterceptTouchEvent ACTION_MOVE  $result")
            }
            MotionEvent.ACTION_UP -> {
                Log.w("wjftc", "ParentLayout onInterceptTouchEvent ACTION_UP  $result")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.e("wjftc", "ParentLayout onInterceptTouchEvent ACTION_CANCEL  $result")
            }
        }
        return result
    }

    override fun onClick(v: View?) {
        Log.w("wjftc", "ParentLayout onClick")
    }
}
