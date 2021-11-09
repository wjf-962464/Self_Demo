package com.wjf.self_demo

import com.google.gson.Gson
import org.junit.Assert.*

fun main() {
    try {
        val string =
            "{\"name\":\"da\",\"age\":20,\"dog\":[{\"type\":\"泰迪\",\"size\":10.5},{\"type\":\"金毛\",\"size\":20.5}]}"
        val son = Gson().fromJson(string, Son::class.java)

        print(son.dog[0].uuid)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
