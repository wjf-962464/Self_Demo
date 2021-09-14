package com.wjf.self_demo.util

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.admin.DevicePolicyManager
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.orhanobut.logger.Logger

class AccessibilityClockService : AccessibilityService() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var onceFlag = false
    private val TIME_DELAY = 1000L
    private val PACKAGE_NAME_QQ = "com.tencent.mobileqq"
    private val offArray = arrayOf("工作台", "打卡", "下班打卡")
    private var offIndex = 0
    private val onArray = arrayOf("工作台", "打卡", "上班打卡")
    private var onIndex = 0
    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    // 初始化
    override fun onServiceConnected() {
        super.onServiceConnected()
        Logger.d("辅助功能onServiceConnected...")
        mService = this
        performBackClick()
        performBackClick()
        performBackClick()
    }

    // 实现辅助功能
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Logger.v("onAccessibilityEvent${event.eventType}")
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
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                eventText =
                    "TYPE_NOTIFICATION_STATE_CHANGED"
                val notification = event.parcelableData as Notification
                val intent = notification.contentIntent
                val title = notification.extras.getString(Notification.EXTRA_TITLE)
                val text = notification.extras.getString(Notification.EXTRA_TEXT)
                val pkn = intent.creatorPackage
                if (pkn != null && text != null) {
                    listenNotification(pkn, text)
                    Logger.i("消息来了$title :$text---${intent.creatorPackage}|${notification.tickerText}")
                }
            }
            else -> {
            }
        }
        if (eventText.isNotEmpty()) {
            Logger.v(" \r\n======\r\n||eventText：$eventText\r\n||eventType：$eventType\r\n=====")
        }
    }

    fun clockProcess() {
        Logger.d("clockProcess")
        AccessibilityHelper.openCLD(this)
    }

    private fun listenNotification(pkn: String, msg: String) {
        when (pkn) {
            PACKAGE_NAME_QQ -> {
                Logger.d("$pkn 的消息：$msg")
                when (msg) {
                    "上班" -> {
                        openClockPage(true)
                    }
                    "下班" -> {
                        openClockPage(false)
                        Logger.d("下班")
                    }
                    "锁屏" -> {
                        devicePolicyManager.lockNow()
                        ScreenLockManager.wakeAndUnlock(this, false)
                    }
                    else -> {
                        Logger.d("$pkn 的消息：$msg")
                    }
                }
            }
        }
    }

    /**
     * 执行打卡流程
     */
    private fun openClockPage(workSwitch: Boolean) {
        ScreenLockManager.wakeAndUnlock(this, true)
        AccessibilityHelper.openCLD(this)
        Thread.sleep(5000L)
        if (workSwitch) {
            onIndex = switchWork(onArray, onIndex)
            onceFlag = onIndex == onArray.size - 1
        } else {
            offIndex = switchWork(offArray, offIndex)
            onceFlag = offIndex == offArray.size - 1
        }
        checkClock(onceFlag, workSwitch)
    }

    private fun checkClock(flag: Boolean, switch: Boolean) {
        if (flag) {
            // TODO 检查操作
            Thread.sleep(5000L)
            onceFlag = false
            if (switch) {
                onIndex = 0
            } else {
                offIndex = 0
            }
            devicePolicyManager.lockNow()
        }
    }

    /**
     *  上下班切换
     */
    private fun switchWork(workArray: Array<String>, startIndex: Int): Int {
        if (onceFlag) {
            return startIndex
        }
        onceFlag = true
        val len = workArray.size
        var endIndex = startIndex
        var tab: String
        for (index in startIndex until len) {
            tab = workArray[index]
            if (!openTab(tab)) {
                endIndex = index
                break
            } else {
                Logger.i("已打开$tab")
                Thread.sleep(TIME_DELAY)
            }
        }
        return endIndex
    }

    /**
     * 打开页面
     */
    private fun openTab(text: String): Boolean {
        val controlTab = findViewByText(text)
        return if (controlTab == null) {
            Logger.d("找不到$text")
            false
        } else {
            Logger.d("找到$text")
            doClickableTab(controlTab)
        }
    }

    /**
     * 执行点击
     */
    private fun doClickableTab(node: AccessibilityNodeInfo): Boolean {
        return if (!node.isClickable) {
            Logger.d("${node}不可点击")
            val nodeParent = node.parent
            if (nodeParent == null) {
                Logger.v("${node}没爹")
                false
            } else {
                Logger.v("${node}有爹")
                doClickableTab(nodeParent)
            }
        } else {
            Logger.d("${node}可点击")
            node.performClick()
            true
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
        var mService: AccessibilityClockService? = null

        /** 辅助功能是否启动  */
        val isStart: Boolean
            get() = mService != null
    }
}
