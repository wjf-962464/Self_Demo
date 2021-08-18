package com.wjf.self_library.common

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.wjf.self_library.BuildConfig
import java.lang.ref.WeakReference

/** @author WJF
 */
open class BaseApplication : Application() {
    companion object {
        lateinit var context: WeakReference<Context>
    }

    override fun onCreate() {
        super.onCreate()
        initLog()
        context = WeakReference(applicationContext)
    }

    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 是否显示线程信息，默认为ture
            .methodCount(0)         // 显示的方法行数，默认为2
//                    .methodOffset(1)        // 隐藏内部方法调用到偏移量，默认为5
//                    .logStrategy() // 更改要打印的日志策略。
            .tag(BuildConfig.TAG)   // 每个日志的全局标记。默认PRETTY_LOGGER
            .build();
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}
