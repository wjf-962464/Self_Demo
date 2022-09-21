package org.jxxy.debug.corekit.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import java.util.concurrent.TimeUnit

/**
 * @param lifecycle 生命周期监听
 * @param interval 轮询间隔，默认为一个时间单位触发一次
 * @param timeUnit 时间单位，通过TimeUnit取单位常量，默认为秒
 * @param block 轮询做的事情
 */
class ScheduleTask(
    private val lifecycle: Lifecycle,
    private val interval: Int = 1,
    private val timeUnit: TimeUnit = TimeUnit.SECONDS,
    private val block: () -> Unit
) :
    DefaultLifecycleObserver {
    private var job: Job? = null

    init {
        lifecycle.addObserver(this)
    }

    fun startTask() {
        start()
    }

    fun pause() {
        if (job != null) {
            job?.cancel()
            job = null
        }
    }

    fun start() {
        if (job != null) {
            pause()
        }
        job = TimeCountDowner.countDown2(lifecycle.coroutineScope, interval, timeUnit) {
            block.invoke()
        }
        job?.start()
    }

    fun startDelay() {
        if (job == null) {
            job = TimeCountDowner.countDownWithDelay(lifecycle.coroutineScope, interval, timeUnit) {
                block.invoke()
            }
            job?.start()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        job?.cancel()
        job = null
    }
}
