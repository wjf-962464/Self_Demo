package org.jxxy.debug.corekit.http

import okhttp3.OkHttpClient
import org.jxxy.debug.corekit.gson.GsonManager
import org.jxxy.debug.corekit.http.interceptor.HeaderInterceptor
import org.jxxy.debug.corekit.http.interceptor.LogInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** @author WJF
 */
class HttpManager private constructor(builder: Builder) {
    private val headers = mutableMapOf<String, String?>()
    private val retrofit: Retrofit
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(builder.timeout, TimeUnit.SECONDS)
            .readTimeout(builder.timeout, TimeUnit.SECONDS)
            .writeTimeout(builder.timeout, TimeUnit.SECONDS)
/*            .addInterceptor(
                CommonParamsInterceptor {
                    RequestCommonParams.commonParameters
                }
            )*/
            .addInterceptor(
                HeaderInterceptor {
                    headers.apply {
                        put("Content-Type", "application/json;charset=UTF-8")
                    }
                }
            )
            .addInterceptor(LogInterceptor())
            .build()
    }

    fun <Service> service(serviceClass: Class<Service>): Service {
        return retrofit.create(serviceClass)
    }

    fun updateHeader() {
        CustomHttpHeaderUtil.header.forEach {
            headers[it.key] = it.value
        }
    }

    companion object Instance {
        lateinit var instance: HttpManager
            private set

        fun init(builder: Builder) {
            instance = HttpManager(builder)
        }
    }

    init {
        updateHeader()

        // 基于okHttp的一些封装
        // 基于retrofit的一些封装
        retrofit = Retrofit.Builder()
            .baseUrl(builder.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonManager.instance.gson))
            .client(okHttpClient)
            .build()
    }

    class Builder {
        var baseUrl: String = ""
        var timeout: Long = 3

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun timeout(timeout: Long): Builder {
            this.timeout = timeout
            return this
        }
    }
}
