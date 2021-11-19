package com.wjf.self_demo

import com.google.gson.Gson
import com.wjf.self_demo.test.gson.Student

fun main() {
    try {
        val str = "{\"code\":\"111\",\"name\":\"wjf\"}"
        val result = Gson().fromJson(str, Student::class.java)
        println(result)
//        GsonBuilder().registerTypeAdapter()
/*        val count = 2
        for (i in 0 until count) {
            print(i)
        }*/
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
