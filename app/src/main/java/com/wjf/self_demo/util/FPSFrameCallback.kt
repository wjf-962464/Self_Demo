package com.wjf.self_demo.util

import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import com.orhanobut.logger.Logger
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class FPSFrameCallback(val handler: Handler) : Choreographer.FrameCallback {
    var lastFrameTime: Long = 0L
    var currentFrameTime: Long = 0L
    var stackInfo: String? = ""

    companion object {
        const val deviceRefreshRate: Float = 16.6f
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (lastFrameTime == 0L) {
            lastFrameTime = frameTimeNanos
            Choreographer.getInstance().postFrameCallback(this)
            return
        }

        currentFrameTime = frameTimeNanos
        val value = (currentFrameTime - lastFrameTime) / 1000000L
        val droppedCount = droppedCount(lastFrameTime, currentFrameTime, deviceRefreshRate)
        if (droppedCount > 10) {

            Logger.w("发生跳帧$droppedCount ${value}ms $stackInfo")
        }
        lastFrameTime = currentFrameTime

        Choreographer.getInstance().postFrameCallback(this)
        stackInfo = Thread.getAllStackTraces()[Looper.getMainLooper().thread].contentToString()
    }

    private fun droppedCount(start: Long, end: Long, rate: Float): Int {
        val diffMs = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS)
        val dev = rate.roundToLong()
        if (diffMs > dev) {
            return (diffMs / dev).toInt()
        }
        return 0
    }
}
