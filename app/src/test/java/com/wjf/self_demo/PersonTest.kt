package com.wjf.self_demo

fun main() {
/*    a5(
        {
            println(it)
            println("wjf")
            "周俊峰"
        },
        {
            println("hjy")
            5
        }
    )*/
    val a = "周俊峰"
//    stringExtend(a)
    a.stringExtend2 {
        println("没吃午饭")
        it
    }
}
fun stringExtend(a: String) {
    println("name:$a")
}
fun String.stringExtend2(block: (String) -> String) {
    println("name:${block(this)}")
}
fun a(printMethod: (Int) -> String, b: Int) {
    println(printMethod(b))
}

fun a2(b: Int, printMethod: (Int) -> String) {
    println(printMethod(b))
}

fun a3(b: Int, printMethod: (Int) -> String) {
    printMethod(b)
}

fun a4(printMethod: (Int) -> String, getInt: () -> Int) {
    printMethod(getInt())
}
fun a6(string: String) {
    println("输出结果$string")
}

fun a5(printMethod: (Int) -> String, getInt: () -> Int) {
    a6(printMethod(getInt()))
}

object Util {

    fun output1(title: String?, msg: String? = null) {
        println("title:$title;msg:$msg")
    }
}
