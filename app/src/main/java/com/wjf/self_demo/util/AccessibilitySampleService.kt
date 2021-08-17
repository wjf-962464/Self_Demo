package com.wjf.self_demo.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.TargetApi
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    fun getAllIds(node: AccessibilityNodeInfo) {
        HAND_FREE = true
        Logger.d("id：${node.viewIdResourceName}")
        val count = node.childCount
        if (count <= 0) {
            return
        }
        for (i in 0 until count) {
            val childNode = node.getChild(i)
            getAllIds(childNode)
        }
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun findViewByID(id: String?): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            Log.d("zzm", "findViewByID: " + nodeInfoList.size)
            for (nodeInfo in nodeInfoList) {
                Log.d("zzm", "findViewByID: $nodeInfo")
                if (nodeInfo != null) {
                    return nodeInfo
                }
            }
        }
        return null
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun clickTextViewByID(id: String?): Boolean {
        val accessibilityNodeInfo = rootInActiveWindow ?: return false
        val nodeInfoList =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo)
                    Logger.i("点击${nodeInfo.contentDescription}")
                    return true
                }
            }
        }
        return false
    }

    private fun clickTextViewByText(text: String?) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList =
            accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo)
                    Logger.i("点击${nodeInfo.text}")
                    break
                }
            }
        }
    }

    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    private fun findViewByText(text: String): AccessibilityNodeInfo? {
        return findViewByText(text, false)
    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    private fun findViewByText(text: String, clickable: Boolean): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable == clickable)) {
                    return nodeInfo
                }
            }
        }
        return null
    }

    /**
     * 模拟返回操作
     */
    private fun performBackClick() {
        try {
            Thread.sleep(600)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    private fun performViewClick(nodeInfo: AccessibilityNodeInfo?) {
        if (nodeInfo == null) {
            return
        }
        var node = nodeInfo
        while (node != null) {
            if (nodeInfo.isClickable) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                break
            }
            node = node.parent
        }
    }

    private fun doFigureClick(x: Float, y: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Logger.e("dispatchGesture")
            val path = Path()
            path.moveTo(179F, 1721F)
            val action = GestureDescription.StrokeDescription(path, 100, 50)
            val result = dispatchGesture(
                GestureDescription.Builder().addStroke(action).build(),
                object : AccessibilityService.GestureResultCallback() {
                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        super.onCancelled(gestureDescription)
                        Logger.i("dispatchGesture onCancelled")
                    }

                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        Logger.i("dispatchGesture onCompleted")
                    }
                },
                mHandler
            )
            Logger.i("dispatchGesture result:$result")
        } else {
            Logger.e("dispatchGesture版本不够")
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
