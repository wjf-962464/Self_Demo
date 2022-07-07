package org.jxxy.debug.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import org.jxxy.debug.common.BaseApplication

object NetworkUtil {
    enum class NetworkStatus {
        NETWORK_NONE, // 没有连接网络
        NETWORK_MOBILE, // 移动网络
        NETWORK_WIFI // 无线网络
    }

    private val connectivityManager: ConnectivityManager? by lazy {
        BaseApplication.context()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    /**
     * @Title: getNetWorkState
     *
     * @Description: 获取当前网络状态
     *
     * @param context
     * @return int
     */
    fun getNetWorkState(): NetworkStatus {
        // 得到连接管理器对象
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val networkCapabilities = it.getNetworkCapabilities(it.activeNetwork)
                if (networkCapabilities == null) {
                    return NetworkStatus.NETWORK_NONE
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return NetworkStatus.NETWORK_MOBILE
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return NetworkStatus.NETWORK_WIFI
                }
            } else {
                val activeNetworkInfo = it.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                        return NetworkStatus.NETWORK_WIFI
                    } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                        return NetworkStatus.NETWORK_MOBILE
                    }
                } else {
                    return NetworkStatus.NETWORK_NONE
                }
            }
        }
        return NetworkStatus.NETWORK_NONE
    }
}
