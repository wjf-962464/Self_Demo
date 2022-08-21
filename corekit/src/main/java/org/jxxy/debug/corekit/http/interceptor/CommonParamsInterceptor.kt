package org.jxxy.debug.corekit.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 拼接公共参数拦截器
 */
class CommonParamsInterceptor(private val commonParamsFunc: () -> MutableMap<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val build = originalRequest.url.newBuilder()
        val commonParams = commonParamsFunc()
        commonParams.forEach {
            build.addQueryParameter(it.key, it.value)
        }
        val modifiedUrl = build.build()
        val request = originalRequest.newBuilder().url(modifiedUrl).build()
        return chain.proceed(request)
    }
}
