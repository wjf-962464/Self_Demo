package org.jxxy.debug.common

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.lang.ref.WeakReference
import org.jxxy.debug.BuildConfig

/** @author WJF
 */
open class BaseApplication : Application() {
    companion object {
        private var contextReference: WeakReference<Context>? = null
        fun context(): Context {
            return contextReference?.get()!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        initLog()
        contextReference = WeakReference(applicationContext)
    }

    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // 是否显示线程信息，默认为ture
            .methodCount(0) // 显示的方法行数，默认为2
//                    .methodOffset(1)        // 隐藏内部方法调用到偏移量，默认为5
//                    .logStrategy() // 更改要打印的日志策略。
            .tag(BuildConfig.TAG) // 每个日志的全局标记。默认PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}
