package com.wjf.self_demo.util

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.wjf.barcode.Logger

class AccessibilitySampleService : AccessibilityService() {
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val audioButtonArray = arrayListOf(
        "com.android.incallui:id/audioButton",
        "com.android.incallui:id/audioButtonlayout",
        "com.android.incallui:id/showAudio",
        "com.android.incallui:id/showAudioButton"

    )

    // 初始化
    override fun onServiceConnected() {
        super.onServiceConnected()
        Logger.d("辅助功能onServiceConnected...")
        mService = this
        performBackClick()
        performBackClick()
    }

    // 实现辅助功能
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Logger.d("onAccessibilityEvent${event.eventType}")
        val eventType = event.eventType
        var eventText = ""
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                eventText = "TYPE_VIEW_CLICKED"
                val noteInfo = event.source
                noteInfo?.let {
                    Logger.d("==============Start====================")
                    Logger.d(noteInfo.toString())
                    Logger.d("=============END=====================")
                }
            }
            AccessibilityEvent.TYPE_VIEW_SELECTED -> eventText = "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> eventText = "TYPE_VIEW_TEXT_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                eventText =
                    "TYPE_WINDOW_CONTENT_CHANGED"
                val line = findViewByID("com.android.incallui:id/call_card_state_info")
                val phoneStatusBtn =
                    if (line != null) {
                        line.getChild(0)
                    } else {
                        findViewByID("com.android.incallui:id/single_call_status")
                    }
                listenCallStatus(phoneStatusBtn)
                openVoice()
/*                if (!HAND_FREE) {
                    getAllIds(rootInActiveWindow)
                }*/
            }
        }
        if (eventText.isNotEmpty()) {
            Logger.i(" \r\n======\r\n||eventText：$eventText\r\n||eventType：$eventType\r\n=====")
        }
    }

    private fun listenCallStatus(phoneStatusBtn: AccessibilityNodeInfo?) {
        if (phoneStatusBtn != null && phoneStatusBtn.text != null) {
//            openVoice()
            when (phoneStatusBtn.text) {
                "通话结束" -> {
                    clickTextViewByID("com.android.incallui:id/endButton")
                }
                else -> {
                    Logger.d("通话状态：${phoneStatusBtn.text}")
                }
            }
            if (phoneStatusBtn.text.contains(":")) {
                Logger.e("接通了！！！")
            }
        } else {
            Logger.e("都没有")
        }
    }

    private fun openVoice() {
        if (!HAND_FREE) {
            HAND_FREE = true
            for (id in audioButtonArray) {
                val node = findViewByID(id)
                Logger.e("node ：${node?.isClickable}")
                if (node?.isChecked == false && node.isClickable) {
                    performViewClick(node)
                    node.performAction(AccessibilityNodeInfo.ACTION_SELECT)
                    node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    HAND_FREE = true
                    performViewClick(node)
                    return
                    /*if (node.isChecked) {
                        Logger.e("自动免提")
                        HAND_FREE = true
                        return
                    } else {
                        Logger.e("自动免提失败")
                    }*/
                } else {
                    Logger.e("没有找到$id")
                }
            }
            HAND_FREE = false
        }
    }

    override fun onInterrupt() {
        Logger.d("辅助功能onInterrupt")
        mService = null
    }

    // 公共方法
    override fun onDestroy() {
        super.onDestroy()
        Logger.d("辅助功能onDestroy")
        mService = null
    }

    companion object {
        var mService: AccessibilitySampleService? = null

        /** 辅助功能是否启动  */
        val isStart: Boolean
            get() = mService != null

        var HAND_FREE: Boolean = false
    }
}
