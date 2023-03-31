package com.wjf.self_demo.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.orhanobut.logger.Logger
import com.wjf.self_demo.entity.AccessibilityTab

class AccessibilityHelper {
    companion object {
        const val PACKAGE_WEWORK = "com.tencent.wework"
        const val PACKAGE_NAME_QQ = "com.tencent.mobileqq"
        const val PACKAGE_NAME_MY_DEBUG = "com.wjf.self_demo.debug"
        const val PACKAGE_NAME_MIUI = "com.miui.home"
        const val PACKAGE_NAME_SYSTEMUI = "com.android.systemui"

        fun closeApp(context: Context, pkn: String) {
            try {
                val am =
                    context.getSystemService(AccessibilityService.ACTIVITY_SERVICE) as ActivityManager
                val method =
                    Class.forName(pkn).getMethod(
                        "forceStopPackage", String::class.java
                    )
                method.isAccessible = true
                method.invoke(am, PACKAGE_NAME_MY_DEBUG)
            } catch (e: Exception) {
                Logger.e("closeApp error", e)
            }
        }

        @SuppressLint("QueryPermissionsNeeded")
        fun openApp(context: Context, pkn: String) {
            val packageManager = context.packageManager
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = packageManager.getPackageInfo(pkn, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                Logger.e("packageManager.getPackageInfo error", e)
            }
            if (packageInfo == null) {
                return
            }

            val resolveIntent = Intent(Intent.ACTION_MAIN, null)
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            resolveIntent.setPackage(packageInfo.packageName)
            // 通过getPackageManager()的queryIntentActivities方法遍历
            val resolveInfoList = packageManager.queryIntentActivities(resolveIntent, 0)
            val resolveInfo = resolveInfoList.iterator().next()
            if (resolveInfo != null) {
                val className = resolveInfo.activityInfo.name
                val packageName = resolveInfo.activityInfo.packageName
                Logger.v("className$className")
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val cn = ComponentName(packageName, className)
                intent.component = cn
//            context.startActivity(intent);
                //            context.startActivity(intent);
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                try {
//                    pendingIntent.send()
                    context.startActivity(intent)
                    Logger.v("已打开$pkn")
                } catch (e: CanceledException) {
                    Logger.e(" pendingIntent.send() error", e)
                }
            }
        }
    }
}

internal fun AccessibilityService.findViewByID(id: String, pkn: String): AccessibilityNodeInfo? {
    return findViewByID("$pkn:id/$id")
}

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
internal fun AccessibilityService.findViewByID(id: String?): AccessibilityNodeInfo? {
    val accessibilityNodeInfo = rootInActiveWindow ?: return null
    val nodeInfoList =
        accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id.orEmpty())
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

internal fun AccessibilityService.getAllIds(node: AccessibilityNodeInfo) {
    AccessibilitySampleService.HAND_FREE = true
    Logger.v("id：${node.viewIdResourceName}；text：${node.text}；toString：$node")
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
internal fun AccessibilityService.clickTextViewByID(id: String?): Boolean {
    val accessibilityNodeInfo = rootInActiveWindow ?: return false
    val nodeInfoList =
        accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id.orEmpty())
    if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
        for (nodeInfo in nodeInfoList) {
            if (nodeInfo != null) {
                performViewClick(nodeInfo)
                Logger.v("点击${nodeInfo.contentDescription}")
                return true
            }
        }
    }
    return false
}

internal fun AccessibilityService.clickTextViewByText(text: String?) {
    val accessibilityNodeInfo = rootInActiveWindow ?: return
    val nodeInfoList =
        accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
    if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
        for (nodeInfo in nodeInfoList) {
            if (nodeInfo != null) {
                performViewClick(nodeInfo)
                Logger.v("点击${nodeInfo.text}")
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
internal fun AccessibilityService.findViewByText(text: String): AccessibilityNodeInfo? {
    return findViewByText(text, false)
}

/**
 * 查找对应文本的View
 *
 * @param text      text
 * @param clickable 该View是否可以点击
 * @return View
 */
internal fun AccessibilityService.findViewByText(
    text: String,
    clickable: Boolean
): AccessibilityNodeInfo? {
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
 * 查找对应文本的View
 *
 * @param text      text
 * @param clickable 该View是否可以点击
 * @return View
 */
internal fun AccessibilityService.findViewByTab(
    tab: AccessibilityTab
): AccessibilityNodeInfo? {
    val accessibilityNodeInfo = rootInActiveWindow ?: return null
    val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(tab.text)
    if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
        for (nodeInfo in nodeInfoList) {
            if (nodeInfo != null && nodeInfo.text == tab.text) {
                return nodeInfo
            }
        }
    }
    return null
}

/**
 * 模拟返回操作
 */
internal fun AccessibilityService.performBackClick() {
    try {
        Thread.sleep(600)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    performGlobalAction(GLOBAL_ACTION_BACK)
}

internal fun AccessibilityNodeInfo.performClick() {
    performAction(AccessibilityNodeInfo.ACTION_CLICK)
}

internal fun AccessibilityNodeInfo.performSelect() {
    performAction(AccessibilityNodeInfo.ACTION_SELECT)
}

/**
 * 模拟点击事件
 *
 * @param nodeInfo nodeInfo
 */
internal fun AccessibilityService.performViewClick(nodeInfo: AccessibilityNodeInfo?) {
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

internal fun AccessibilityService.doFigureClick(x: Float, y: Float, handler: Handler) {
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
                    Logger.v("dispatchGesture onCancelled")
                }

                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    Logger.v("dispatchGesture onCompleted")
                }
            },
            handler
        )
        Logger.v("dispatchGesture result:$result")
    } else {
        Logger.e("dispatchGesture版本不够")
    }
}
