package com.wjf.self_demo

import com.google.gson.Gson

fun main() {
    val a1 = ToIntJava()
    val a2 = ToIntJava2()
    a1.method1()
    a2.method1()
}

class ToIntJava2 {
    fun method1() {
        val a = "123".toInt()
        val c = 12.0f.toInt()
        val b = Integer.valueOf("123")
        val s = "s"
        val ob = Gson().fromJson(s, Person::class.java)
    }
}
