package org.jxxy.debug.corekit.util

import android.content.Context
import android.content.pm.PackageManager
import org.jxxy.debug.corekit.common.BaseApplication

/** 跟App相关的辅助类
 * @author WJF
 */
object AppUtils {
    private val packageManager: PackageManager by lazy { BaseApplication.context().packageManager }
    private val packageName: String by lazy { BaseApplication.context().packageName }

    /** 获取应用程序名称  */
    fun getAppName(): String {
        try {
            BaseApplication.context().let {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                return ResourceUtil.getString(packageInfo.applicationInfo.labelRes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context context
     * @return 当前应用的版本名称
     */
    @Synchronized
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context context
     * @return 当前应用的版本名称
     */
    @Synchronized
    fun getVersionCode(context: Context): Int {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context context
     * @return 当前应用的版本名称
     */
    @Synchronized
    fun getPackageName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
