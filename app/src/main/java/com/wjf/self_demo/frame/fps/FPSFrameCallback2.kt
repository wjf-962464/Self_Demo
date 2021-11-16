package com.wjf.self_demo.frame.fps

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import com.orhanobut.logger.Logger
import java.lang.StringBuilder
import java.util.*

class FPSFrameCallback2(var handler: Handler, val stack: Deque<Activity?>?) :
    Choreographer.FrameCallback {
    var lastFrameTime: Long = 0L
    var currentFrameTime: Long = 0L
    var mFrameCount: Int = 0
    var stackTraceStr: String = ""

    companion object {
        const val NS_UNIT: Float = 1000000.0f
        const val INTERVAL = 1000L
    }

    private val mBlockRunnable = Runnable {
        val sb = StringBuilder()
        val stackTrace = Looper.getMainLooper().thread.stackTrace
        for (s in stackTrace) {
            sb.append(
                """
                    $s
                    
                """.trimIndent()
            )
        }
        stackTraceStr = sb.toString()
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (lastFrameTime == 0L) {
            lastFrameTime = frameTimeNanos
        }

        // 计算上一次出帧率，到本帧绘制的时间差（纳秒->微秒）
        val diff: Float = (frameTimeNanos - lastFrameTime) / NS_UNIT
        // 每秒输出一次帧率
        if (diff > INTERVAL) {
            val fps = mFrameCount * 1000.0f / diff
            mFrameCount = 0
            lastFrameTime = 0

            stack?.let {
                if (it.size > 0) {
                    Logger.i("doFrame: $fps on ${it.first?.localClassName ?: ""}")
                } else {
                    Logger.d("doFrame: $fps")
                }
            }
            println("doFrame: $fps")
            if (fps < 50) {
//                Logger.d(stackTraceStr)
            }
        } else {
            ++mFrameCount
        }
//        handler.postDelayed(mBlockRunnable, 1000L)
        Choreographer.getInstance().postFrameCallback(this)
    }
}
