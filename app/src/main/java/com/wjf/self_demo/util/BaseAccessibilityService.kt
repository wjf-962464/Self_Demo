package com.wjf.self_demo.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi

class BaseAccessibilityService : AccessibilityService() {
    private var mAccessibilityManager: AccessibilityManager? = null
    private var mContext: Context? = null
    fun init(context: Context) {
        mContext = context.applicationContext
        mAccessibilityManager =
            this.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    private fun checkAccessibilityEnabled(serviceName: String): Boolean {
        val accessibilityServices =
            mAccessibilityManager!!.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (info in accessibilityServices) {
            if (info.id == serviceName) {
                return true
            }
        }
        return false
    }

    /**
     * 前往开启辅助服务界面
     */
    fun goAccess() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext!!.startActivity(intent)
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    private fun performViewClick(nodeInfo: AccessibilityNodeInfo?) {
        var nodeInfo: AccessibilityNodeInfo? = nodeInfo ?: return
        while (nodeInfo != null) {
            if (nodeInfo.isClickable) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                break
            }
            nodeInfo = nodeInfo.parent
        }
    }

    /**
     * 模拟返回操作
     */
    fun performBackClick() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    /**
     * 模拟下滑操作
     */
    fun performScrollBackward() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
    }

    /**
     * 模拟上滑操作
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun performScrollForward() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    }
    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    @JvmOverloads
    fun findViewByText(
        text: String?,
        clickable: Boolean = false
    ): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList =
            accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null && nodeInfo.isClickable == clickable) {
                    return nodeInfo
                }
            }
        }
        return null
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
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            Log.d("dd", "findViewByID: " + nodeInfoList.size)
            for (nodeInfo in nodeInfoList) {
                Log.d("dd", "findViewByID: $nodeInfo")
                if (nodeInfo != null) {
                    return nodeInfo
                }
            }
        }
        return null
    }

    fun clickTextViewByText(text: String?) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList =
            accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo)
                    break
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun clickTextViewByID(id: String?) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList =
            accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo)
                    break
                }
            }
        }
    }

    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    fun inputText(nodeInfo: AccessibilityNodeInfo, text: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val arguments = Bundle()
            arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                text
            )
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("dd", "onAccessibilityEvent: $event")
    }

    override fun onInterrupt() {}
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("llll", "onServiceConnected: ")
    }

    companion object {
        private var mInstance: BaseAccessibilityService? = null
        val instance: BaseAccessibilityService?
            get() {
                if (mInstance == null) {
                    mInstance = BaseAccessibilityService()
                }
                return mInstance
            }
    }
}
