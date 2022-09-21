package org.jxxy.debug.corekit.http

import org.jxxy.debug.corekit.mmkv.PersistenceUtil

object AddressManager {
    private const val ADDRESS_KEY = "addressInfo"
    private var addressTemp: String? = null
    fun updateCity(cityCode: String?) {
        addressTemp = cityCode
        PersistenceUtil.putValue(ADDRESS_KEY, cityCode)
        CustomHttpHeaderUtil.updateHeader()
    }

    fun getCityId(): String? {
        val cityCode: String? = PersistenceUtil.getValue(ADDRESS_KEY)
        return when {
            addressTemp.isNullOrEmpty().not() -> addressTemp
            cityCode.isNullOrEmpty().not() -> cityCode
            else -> null
        }
    }
}
