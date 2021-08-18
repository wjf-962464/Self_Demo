package com.wjf.self_library.http

import com.orhanobut.logger.Logger
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

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

    fun <Service> service(serviceClass: Class<Service>?): Service {
        return retrofit.create(serviceClass)
    }

    companion object Instance {
        private lateinit var instance: HttpManager

        fun init(builder: Builder) {
            instance = HttpManager(builder)
        }

        fun <Service> service(serviceClass: Class<Service>?): Service =
            instance.service(serviceClass)
    }

    init {
        // 基于okHttp的一些封装
        // 基于retrofit的一些封装
        retrofit = Retrofit.Builder()
            .baseUrl(builder.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    class Builder {
        var baseUrl: String? = null
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

fun <T> Observable<HttpResult<T>>.toSubscribe(block: (HttpResult<T>) -> Unit) {
    val subscriber = BaseSubscriber(block)

    subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber)
}
