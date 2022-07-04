package org.jxxy.debug.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

object TimeCountDowner {
    @OptIn(ExperimentalCoroutinesApi::class)
    @JvmOverloads
    fun countDown(
        scope: CoroutineScope,
        onTick: ((Int) -> Unit)? = null,
        total: Int,
        interval: Int = 1,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        onFinish: (() -> Unit)?
    ): Job {
        return flow {
            for (i in total downTo 1 step interval) {
                emit(i)
                delay(timeUnit.toMillis(interval.toLong()))
            }
        }.flowOn(Dispatchers.Default)
            .onCompletion { onFinish?.invoke() }
            .onEach { onTick?.invoke(it) }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }
}
