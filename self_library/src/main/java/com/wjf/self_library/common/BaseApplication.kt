package com.wjf.self_library.common

import android.app.Application
import android.content.Context
import com.orhanobut.logger.Logger
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
        Logger.init(BuildConfig.TAG)
        context = WeakReference(applicationContext)
    }
}
