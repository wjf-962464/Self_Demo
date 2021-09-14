package com.wjf.self_demo.util

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.KeyguardManager.KeyguardLock
import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import com.orhanobut.logger.Logger

object ScreenLockManager {
    // 锁屏、唤醒相关
    private var km: KeyguardManager? = null
    private var kl: KeyguardLock? = null
    private var pm: PowerManager? = null
    private var wl: WakeLock? = null

    @SuppressLint("InvalidWakeLockTag", "WakelockTimeout")
    fun wakeAndUnlock(context: Context, b: Boolean) {
        // 获取电源管理器对象
        pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wl = pm?.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
            "bright"
        )
        // 得到键盘锁管理器对象
        km = context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager?
        kl = km?.newKeyguardLock("unLock")
        if (b) {

            // 点亮屏幕
            wl?.acquire()
            // 解锁
            kl?.disableKeyguard()
            Logger.d("解锁")
        } else {
            // 锁屏
            kl?.reenableKeyguard()
            // 释放wakeLock，关灯
            Logger.i("释放wakeLock，关灯")
            if (wl?.isHeld == true) {
                Logger.i("release")
                wl?.release()
            }
        }
    }
}
