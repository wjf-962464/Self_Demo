package org.jxxy.debug.corekit.http


/**
 * 自定义网络请求头部
 */
object CustomHttpHeaderUtil {

    val header = mutableMapOf<String, String?>()

    init {
        getHeaderMap()
    }

    private fun getHeaderMap() {
        TokenManager.getToken().let {
            header["satoken"] = it
        }
        AddressManager.getCityId().let {
            header["cityId"] = it
        }
//        header[HEADER_CONST] = addHeaderParams()
    }

    /*fun addHeaderParams(): String {

        var lat = ""
        var lng = ""
        var cityid = ""
        val currentCityBean = LocationHelper.getCurrentSelectCity()
        if (!TextUtils.isEmpty(currentCityBean.id)) {
            cityid = currentCityBean.id ?: ""
        }
        val locationDataBean = currentCityBean.location
        if (!TextUtils.isEmpty(locationDataBean?.lat) && !TextUtils.isEmpty(locationDataBean?.lng)) {
            lat = locationDataBean?.lat ?: ""
            lng = locationDataBean?.lng ?: ""
        }
        val sb = StringBuilder()
        sb.append("lat=")
        sb.append(lat)
        sb.append("&lng=")
        sb.append(lng)
        sb.append("&cityid=")
        sb.append(cityid)
        if (!TextUtils.isEmpty(currentCityBean.district)) {
            sb.append("&area=").append(URLEncoder.encode(currentCityBean.district, "UTF-8"))
        }
        val currentShopMsg = LocationHelper.getCurrentShopMsg()
        if (!TextUtils.isEmpty(currentShopMsg?.sellerid)) {
            sb.append("&sellerid=").append(currentShopMsg?.sellerid)
        }
        if (!TextUtils.isEmpty(currentShopMsg?.shopid)) {
            sb.append("&shopid=").append(currentShopMsg?.shopid)
        }
        return sb.toString()
    }*/

    @JvmStatic
    fun updateHeader() {
        getHeaderMap()
        HttpManager.instance.updateHeader()
    }
}
