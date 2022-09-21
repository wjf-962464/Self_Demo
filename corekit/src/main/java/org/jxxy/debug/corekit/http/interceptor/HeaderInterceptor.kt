package org.jxxy.debug.corekit.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Header拦截器
 */
class HeaderInterceptor(private val headersFunc: () -> MutableMap<String, String?>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val headers = headersFunc()
        headers.forEach { map ->
            map.value?.let {
                requestBuilder.addHeader(map.key, it)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}
