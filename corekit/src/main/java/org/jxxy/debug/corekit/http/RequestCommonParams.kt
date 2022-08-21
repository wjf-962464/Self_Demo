package org.jxxy.debug.corekit.http

import java.util.*

class RequestCommonParams {
    /**
     * 对参数进行排序
     *
     * @param map
     * @return
     */
    fun getParamBySort(map: Map<String, String>): StringBuilder {
        val list: List<Map.Entry<String, String>> = ArrayList(map.entries)
        Collections.sort(list) { (key), (key1) ->
            key.compareTo(
                key1
            )
        }
        val sb = StringBuilder()
        for (i in list.indices) {
            val (key, value) = list[i]
            sb.append(key).append(value)
        }
        return sb
    }

    companion object {
        var sign: String? = null
        var timestamp: Long = 0
        private var versionName: String? = null
        private var deviceId: String? = null
        private var distinctId: String? = null

        // 订单需求--新增公参
        val commonParameters: MutableMap<String, String>
            get() {
                val map: MutableMap<String, String> = HashMap()
/*                val access_token: String = ParamsFormatter.getAccessToken()
                val platform = "Android"
                val channel: String =
                    PackageUtil.getMetaData(YhStoreApplication.getInstance(), "channel")
                if (TextUtils.isEmpty(versionName)) {
                    versionName = PackageUtil.getVersionName(YhStoreApplication.getInstance())
                }
                if (TextUtils.isEmpty(distinctId)) {
                    distinctId = TrackerProxy.getAnonymousId()
                }
                if (!TextUtils.isEmpty(access_token)) {
                    map["access_token"] = access_token
                }
                map["channel"] = AppBuildConfig.CHANNEL_NAME
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = DeviceUtil.getDeviceId(YhStoreApplication.getInstance())
                }
                map["deviceid"] = deviceId
                map["platform"] = platform
                if (!TextUtils.isEmpty(distinctId)) {
                    map["distinctId"] = distinctId
                }
                map["timestamp"] =
                    java.lang.String.valueOf(TimeSyncUtil.getDefault().getTimeStamp())
                map["v"] = versionName

                // 订单需求--新增公参
                map["channelMain"] = "official"
                map["channelSub"] = ""
                map["brand"] = Build.BRAND
                map["model"] = Build.MODEL
                map["os"] = "android"
                map["osVersion"] = "android" + Build.VERSION.SDK_INT
                map["networkType"] =
                    NetWorkUtil.getNetworkTypeNoProvider(YhStoreApplication.getInstance())
                map["screen"] = "" + UiUtil.getWindowWidth(YhStoreApplication.getInstance())
                    .toString() + "*" + UiUtil.getWindowHeight(YhStoreApplication.getInstance())
                map["productLine"] = "YhStore"*/
                return map
            }
    }
}
