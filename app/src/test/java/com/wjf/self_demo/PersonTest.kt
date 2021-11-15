package com.wjf.self_demo

fun main() {
    try {
        val count = 2
        for (i in 0 until count) {
            print(i)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
