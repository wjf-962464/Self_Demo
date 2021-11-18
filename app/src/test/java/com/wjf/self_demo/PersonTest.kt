package com.wjf.self_demo

import android.net.Uri

fun main() {
    try {
        val result =
            "url=?" + String(
                java.util.Base64.getDecoder().decode("TUlEPVhYWCZTSUQ9WFhYJkNJRD1YWFg=")
            )
        println(result)
        val value = Uri.parse(result).getQueryParameter("MID")
        println(value)

/*        val count = 2
        for (i in 0 until count) {
            print(i)
        }*/
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
