package com.wjf.self_demo

import android.app.Activity
import android.os.Bundle
import android.os.HandlerThread
import com.wjf.barcode.BeepManager
import com.wjf.barcode.CustomException
import com.wjf.self_library.common.BaseApplication
import com.wjf.self_library.http.HttpManager
import com.wjf.self_library.http.HttpManager.Instance.init
import java.util.*

/** @author asus
 */
class MyApp : BaseApplication() {
    private val mBlockThread = HandlerThread("blockThread")

    val stack = LinkedList<Activity?>() as Deque<Activity?>
    private val mCallbacks: ActivityLifecycleCallbacks = object :
        ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            stack.push(activity)
        }

        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {
            stack.pop()
        }
    }

    override fun onCreate() {
        super.onCreate()
        val builder = HttpManager.Builder().timeout(5L).baseUrl(HttpService.baseUrl)
        init(builder)
        BeepManager.init(this, SP_TAG)
//        IconFontManager.initAsset("iconfont.ttf")
        //        BlockCanary.install(this, new AppBlockCanaryContext()).start();
//        mBlockThread.start()
//        Choreographer.getInstance()
//            .postFrameCallback(
//                FPSFrameCallback2(Handler(mBlockThread.looper), stack)
//            )
        //        new HandlerBlockTask().startWork();

        registerActivityLifecycleCallbacks(mCallbacks)
        try {
            BeepManager.getInstance()
                .setBeepRes(R.raw.beep)
                .setBeepVolume(0.10f)
                .setVibrateDuration(200L)
                .build()
        } catch (e: CustomException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val SP_TAG = "MyApp"
    }
}
