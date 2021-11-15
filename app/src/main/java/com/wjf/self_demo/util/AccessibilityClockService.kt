package com.wjf.self_demo.util

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.admin.DevicePolicyManager
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.orhanobut.logger.Logger
import com.wjf.self_demo.entity.AccessibilityTab
import com.wjf.self_demo.entity.WxAccessibilityTab

class AccessibilityClockService : AccessibilityService() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var onceFlag = false
    private val TIME_DELAY = 1000L

    private val offArray = listOf<AccessibilityTab>(
        WxAccessibilityTab("f1n", "工作台"),
        WxAccessibilityTab("frq", "打卡"),
        WxAccessibilityTab("b4k", "下班打卡")
    )
    private val onArray = listOf<AccessibilityTab>(
        WxAccessibilityTab("f1n", "工作台"),
        WxAccessibilityTab("frq", "打卡"),
        WxAccessibilityTab("b4k", "上班打卡")
    )
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
                    Logger.v("==============Start====================\n ${noteInfo}\n=============END=====================")
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
                    Logger.i("消息来了$title :$text---${intent.creatorPackage}|${notification.tickerText}")
                    listenNotification(pkn, text)
                }
            }
            else -> {
            }
        }
        if (eventText.isNotEmpty()) {
            Logger.v(" \r\n======\r\n||eventText：$eventText\r\n||eventType：$eventType\r\n=====")
        }
    }

    private fun listenNotification(pkn: String, msg: String) {
        when (pkn) {
            AccessibilityHelper.PACKAGE_NAME_QQ -> {
                Logger.d("$pkn 的消息：$msg")
                when (msg) {
                    "上班" -> {
                        openClockPage(true)
                    }
                    "下班" -> {
                        openClockPage(false)
                        Logger.d("下班")
                    }
                    "更新" -> {
                        ScreenLockManager.wakeAndUnlock(this, true)
                        AccessibilityHelper.openApp(this, AccessibilityHelper.PACKAGE_WEWORK)
                        Thread.sleep(5000L)
                        val node = findViewByID("${AccessibilityHelper.PACKAGE_WEWORK}:id/jbp")
                        if (node == null) {
                            Logger.d("没有找到")
                        }
                        node?.let {
                            Logger.d("text：${node.text}；clickable：${node.isClickable}")
                            node.performClick()
                            Thread.sleep(5000L)
                            findTabAndClick(WxAccessibilityTab("b4k", "更新下班卡"))
                            Thread.sleep(10000L)

//                            devicePolicyManager.lockNow()
                        }
                    }
                    "锁屏" -> {
                        devicePolicyManager.lockNow()
                        ScreenLockManager.wakeAndUnlock(this, false)
                    }
                    "关闭企微" -> {
                        AccessibilityHelper.closeApp(this, AccessibilityHelper.PACKAGE_WEWORK)
                    }
                    "打开企微" -> {
                        val intent =
                            packageManager.getLaunchIntentForPackage(AccessibilityHelper.PACKAGE_WEWORK)
                        startActivity(intent)
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
    open fun openClockPage(
        workSwitch: Boolean,
        wakeSwitch: Boolean = true,
        pageSwitch: Boolean = true
    ) {
/*        if (wakeSwitch) {
            ScreenLockManager.wakeAndUnlock(this, true)
            Thread.sleep(2000L)
        }*/
        if (pageSwitch) {
//            AccessibilityHelper.openApp(this, AccessibilityHelper.PACKAGE_WEWORK)
            Thread.sleep(2000L)
            handler.postDelayed({
                val array: List<AccessibilityTab> = if (workSwitch) {
                    onArray
                } else {
                    offArray
                }
                val startIndex = relocation(array)
                if (startIndex == -1) {
                    getAllIds(rootInActiveWindow)
                    Logger.e("在其他页面")
                    if (findViewByID("btnOk", AccessibilityHelper.PACKAGE_NAME_MIUI) != null) {
                        Logger.w("在桌面")
                        openClockPage(workSwitch, false)
                    }
                    if (findViewByID(
                            "status_bar_container",
                            AccessibilityHelper.PACKAGE_NAME_SYSTEMUI
                        ) != null
                    ) {
                        Logger.w("在锁屏页")
                        openClockPage(workSwitch, true)
                    }
                    return@postDelayed
                }
                val endIndex = switchWork(array, startIndex)
                onceFlag = endIndex == offArray.size - 1
                checkClock(onceFlag)
            }, 2000L)
        }
    }

    private fun relocation(workArray: List<AccessibilityTab>): Int {
        var realIndex = -1
        val len = workArray.size
        var tab: AccessibilityTab
        for (i in 0 until len) {
            tab = workArray[i]
            if (findTabAndClick(tab, false)) {
                realIndex = i
                Logger.i("重定向到$workArray 的${tab.text}")
                continue
            }
        }
        return realIndex
    }

    private fun checkClock(flag: Boolean) {
        if (flag) {
            // TODO 检查操作
            Thread.sleep(5000L)
            onceFlag = false
//            devicePolicyManager.lockNow()
        }
    }

    /**
     *  上下班切换
     */
    private fun switchWork(workArray: List<AccessibilityTab>, startIndex: Int): Int {
        if (onceFlag) {
            return startIndex
        }
        onceFlag = true
        val len = workArray.size
        var endIndex = startIndex
        var tab: AccessibilityTab
        for (index in startIndex until len) {
            tab = workArray[index]
            if (!findTabAndClick(tab)) {
                endIndex = index
                break
            } else {
                Logger.i("已打开${tab.text}")
                Thread.sleep(TIME_DELAY)
            }
        }
        return endIndex
    }

    /**
     * 打开页面
     */
    private fun findTabAndClick(tab: AccessibilityTab, doClick: Boolean = true): Boolean {
        val controlTab = findViewByTab(tab)
        return if (controlTab == null) {
            Logger.d("找不到${tab.text}")
            false
        } else {
            Logger.d("找到${tab.text}")
            if (doClick) {
                return doClickableTab(controlTab)
            }
            return true
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
