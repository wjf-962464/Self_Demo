package com.wjf.self_demo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.orhanobut.logger.Logger

class ClockBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val type = it.getIntExtra("type", -1)
            if (type != 1 && type != 0) {
                Logger.w("未收到有效消息")
                return
            }
            Logger.w("收到消息 $type")
            if (AccessibilityClockService.isStart) {
                val service = AccessibilityClockService.mService
                service?.apply {
                    openClockPage(type == 1)
                }
            } else {
                try {
                    Logger.d("not start")
                    context?.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                } catch (e: Exception) {
                    Logger.e("already start", e)
                    context?.startActivity(Intent(Settings.ACTION_SETTINGS))
                    e.printStackTrace()
                }
            }
        }
    }
}
