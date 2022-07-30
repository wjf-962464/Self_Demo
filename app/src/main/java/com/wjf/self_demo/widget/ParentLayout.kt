package com.wjf.self_demo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.orhanobut.logger.Logger

class ParentLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {
    init {
        setOnClickListener(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
//        Logger.d("ParentLayout onTouchEvent:$result")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Logger.d("ParentLayout ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
//                Logger.d("ParentLayout ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Logger.d("ParentLayout ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Logger.d("ParentLayout ACTION_CANCEL")
            }
        }
        return result
    }

    override fun onClick(v: View?) {
        Logger.d("ParentLayout onClick")
    }
}
