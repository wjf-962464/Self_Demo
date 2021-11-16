package com.wjf.self_demo

import kotlin.math.roundToInt

fun main() {
    try {
        print(0.1f.roundToInt())
        print(0.1f.toInt())
        print(0.1f.toUInt())
/*        val count = 2
        for (i in 0 until count) {
            print(i)
        }*/
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
