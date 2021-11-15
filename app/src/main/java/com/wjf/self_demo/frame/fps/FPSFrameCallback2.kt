package com.wjf.self_demo.frame.fps

import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import com.orhanobut.logger.Logger

class FPSFrameCallback2(var handler: Handler) : Choreographer.FrameCallback {
    var lastFrameTime: Long = 0L
    var currentFrameTime: Long = 0L
    var mFrameCount: Int = 0
    var stackTrace: String = ""

    companion object {
        const val deviceRefreshRate: Float = 16.6f
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (lastFrameTime == 0L) {
            lastFrameTime = frameTimeNanos
        }

        // 计算上一次出帧率，到本帧绘制的时间差（纳秒->微秒）
        val diff: Float = (frameTimeNanos - lastFrameTime) / 1000000.0f
        // 每秒输出一次帧率
        if (diff > 1000) {
            val fps = mFrameCount * 1000.0f / diff
            mFrameCount = 0
            lastFrameTime = 0
            Logger.i("doFrame: $fps")
            println("doFrame: $fps")
            if (fps < 50) {
                handler.post {

                    Logger.w(stackTrace + "===\n" + Looper.getMainLooper().thread.stackTrace.contentToString())
                }
            }
        } else {
            ++mFrameCount
            stackTrace = Looper.getMainLooper().thread.stackTrace.contentToString()
        }

        Choreographer.getInstance().postFrameCallback(this)
    }
}
