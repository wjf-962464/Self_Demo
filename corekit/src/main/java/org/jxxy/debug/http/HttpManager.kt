package org.jxxy.debug.http

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** @author WJF
 */
class HttpManager private constructor(builder: Builder) {
    private val retrofit: Retrofit
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(builder.timeout, TimeUnit.SECONDS)
            .readTimeout(builder.timeout, TimeUnit.SECONDS)
            .writeTimeout(builder.timeout, TimeUnit.SECONDS)
            .addInterceptor(LogInterceptor())
            .build()
    }

    fun <Service> service(serviceClass: Class<Service>): Service {
        return retrofit.create(serviceClass)
    }

    companion object Instance {
        private lateinit var instance: HttpManager

        fun init(builder: Builder) {
            instance = HttpManager(builder)
        }

        fun <Service> service(serviceClass: Class<Service>): Service =
            instance.service(serviceClass)
    }

    init {
        // 基于okHttp的一些封装
        // 基于retrofit的一些封装
        retrofit = Retrofit.Builder()
            .baseUrl(builder.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    class Builder {
        var baseUrl: String = ""
        var timeout: Long = 5

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
