package org.jxxy.debug.corekit

import org.jxxy.debug.corekit.mmkv.PersistenceUtil

object AddressManager {
    private const val ADDRESS_KEY = "addressInfo"

    fun updateCity(cityCode: String?) {
        PersistenceUtil.putValue(ADDRESS_KEY, cityCode)
    }

    fun getCityId(): String? {
        val cityCode: String? = PersistenceUtil.getValue(ADDRESS_KEY)
        return if (cityCode.isNullOrEmpty())
            null
        else
            cityCode
    }
}
