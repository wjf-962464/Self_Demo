package com.wjf.self_demo.jetpack

import android.util.Log
import androidx.lifecycle.ViewModel
import com.wjf.self_demo.BuildConfig
import com.wjf.self_demo.HttpService
import com.wjf.self_library.common.toSubscribe
import com.wjf.self_library.http.HttpManager

class HttpViewModel : ViewModel() {
    private val service: HttpService by lazy {
        HttpManager.service(HttpService::class.java)
    }

    fun login(phone: String, pwd: String) {
        service.login(phone, pwd).toSubscribe(true) {
            Log.d(BuildConfig.TAG, "成功$it")
        }
    }
}
