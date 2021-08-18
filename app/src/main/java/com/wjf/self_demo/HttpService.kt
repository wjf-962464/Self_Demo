package com.wjf.self_demo

import com.wjf.self_demo.entity.UserEntity
import com.wjf.self_library.http.HttpResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import rx.Observable

interface HttpService {
    //    String baseUrl = "http://192.168.1.106:8080/";
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=uft-8")
    @POST("demo/user/login")
    @FormUrlEncoded
    fun login(
        @Field("userPhone") userPhone: String?,
        @Field("userPassword") userPassword: String?
    ): Observable<HttpResult<UserEntity>>

    companion object {
        const val baseUrl = "http://39.102.48.223:8080/"
    }
}
