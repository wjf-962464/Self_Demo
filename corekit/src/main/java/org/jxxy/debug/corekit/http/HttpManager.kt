package org.jxxy.debug.corekit.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** @author WJF
 */
class HttpManager private constructor(builder: Builder) {
    private val retrofit: Retrofit
    private val gson: Gson
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
        lateinit var instance: HttpManager
            private set

        fun init(builder: Builder) {
            instance = HttpManager(builder)
        }
    }

    init {
        // 全局gson初始化
        val gsonBuilder = GsonBuilder()
        builder.getTypeAdapterFactories().forEach {
            gsonBuilder.registerTypeAdapterFactory(it)
        }
        gson = gsonBuilder.create()
        // 基于okHttp的一些封装
        // 基于retrofit的一些封装
        retrofit = Retrofit.Builder()
            .baseUrl(builder.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    class Builder {
        var baseUrl: String = ""
        var timeout: Long = 1
        private val typeAdapterFactories: MutableList<TypeAdapterFactory> = mutableListOf()

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun timeout(timeout: Long): Builder {
            this.timeout = timeout
            return this
        }

        fun registerTypeAdapterFactory(adapterFactory: TypeAdapterFactory): Builder {
            typeAdapterFactories.add(adapterFactory)
            return this
        }

        fun getTypeAdapterFactories(): List<TypeAdapterFactory> {
            return typeAdapterFactories
        }
    }
}
