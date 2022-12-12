package com.wjf.self_demo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun Log(msg: String) {
    println("$msg ...${Thread.currentThread().name}")
}

fun String.pt() {
    println("$this ...${Thread.currentThread().name}")
}

fun sleepJob(length: Int = 5, time: Long = 500L) {
    var nextPrintTime = System.currentTimeMillis() + time
    var i = 0
    while (i < length) {
        // 一个执行计算的循环，只是为了占用 CPU
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm sleeping ${i++} ...${Thread.currentThread().name}")
            // 每秒打印消息两次
            nextPrintTime += time
        }
    }
}

fun main() {
    f()
}

fun f() {
    runBlocking(SupervisorJob()) {
        coroutineScope {
            launch(
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    Log("捕获到了异常~")
                }
            ) {
                Log("1")
                launch {
                    Log("2")
                    throw ArithmeticException("抛出异常了~")
                }
            }
        }

        launch {
            Log("路人甲")
            delay(1000)
            Log("来了个路人乙")
        }
    }
}

fun e() {
    GlobalScope.launch(
        Dispatchers.Default + CoroutineExceptionHandler { _, e ->
//                e.printStackTrace()
            "zenm 捕捉不到呢".pt()
        }
    ) {
        var block: (() -> Unit)? = null
        launch(
            Dispatchers.Default + CoroutineExceptionHandler { _, e ->
//                e.printStackTrace()
                println("exception $e")
            }
        ) {
            withContext(this@launch.coroutineContext) {
//                try {
                suspendCancellableCoroutine<Unit> { continuation ->
                    block = {
//                            if (continuation.isActive) {
                        /*if (continuation.isCompleted) {
                            "已经结束".pt()
                        } else {

                        }*/
                        "resumeWith".pt()
                        continuation.resumeWith(Result.success(Unit))
                    }
                }
//                } finally {
                block = null
//                }
            }
        }

        launch(
            CoroutineExceptionHandler { _, e ->
//                e.printStackTrace()
                "出错 了".pt()
            }
        ) {
            delay(1000L)
//            throw IllegalStateException("抛出异常")
            block?.invoke()
//            block?.invoke()
            "能往下走".pt()
        }
    }
}

fun d() {
    runBlocking {
        /*launch(Dispatchers.Default) {
            for (i in 5 downTo 1) {
                delay(100)
                "on each $i".pt()
            }
        }*/
        flow {
            for (i in 5 downTo 1) {
                "flow in".pt()
                emit(i)
                delay(100)
            }
        }.onEach {
            "on each $it".pt()
        }
            .launchIn(this + Dispatchers.Default)
        "start".pt()
        sleepJob(time = 100L)
        "end".pt()
    }
}

/**
 * async的总线式同步效果
 */
fun c() {
    runBlocking {
        val a = async(Dispatchers.Default) {
            "第一个async块1".pt()
            delay(400)
            "第一个async块2".pt()
            1
        }
        val b = async(Dispatchers.Default) {
            "第二个async块".pt()
            delay(500)
            2
        }
        "没有await总线".pt()
//        "结果：${a.await() + b.await()}".pt()
    }
}

/**
 * suspend挂起在切换协程时的执行顺序，切换时会当目标协程空闲时间进行执行
 */
fun b() {
    runBlocking(
        CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            println("exception $e")
        }
    ) {
        launch(Dispatchers.Default) {
            delay(500)
            launch(this@runBlocking.coroutineContext) {
                println("我来了1 ...${Thread.currentThread().name}")
            }
            println("我来了2 ...${Thread.currentThread().name}")
        }
        var nextPrintTime = System.currentTimeMillis()
        var i = 0
        while (i < 5) {
            // 一个执行计算的循环，只是为了占用 CPU
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...${Thread.currentThread().name}")
                // 每秒打印消息两次
                nextPrintTime += 500L
            }
        }
    }
}

/**
 * 协程cancel是否可以正常关闭Job，isActive的判断起到了重要作用
 * 同Thread.interrupt和isInterrupted判断
 * 在suspend状态能被直接取消
 */
fun a() {
    try {
        runBlocking(
            CoroutineExceptionHandler { _, e ->
                e.printStackTrace()
                println("exception $e")
            } + Dispatchers.Default
        ) {
            val job = launch {
                var nextPrintTime = System.currentTimeMillis()
                var i = 0
                // isActive是协程扩展属性，cancel()后变为false
                // && isActive能取消，不加则取消失败
                while (i < 5) {
                    // 一个执行计算的循环，只是为了占用 CPU
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        // 每秒打印消息两次
                        nextPrintTime += 500L
                    }
                }
            }
            delay(100)
            job.cancel()
            println("3")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        println("exception $e")
    }
}
