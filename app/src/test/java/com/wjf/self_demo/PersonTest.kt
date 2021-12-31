package com.wjf.self_demo

fun main() {
    try {

/*        for (i in 0..5) {
            var flag = false

            Timer().schedule(1000L) {
                if (flag) {
                    println("任务${i + 1} 已经收到回调")
                } else {
                    println("任务${i + 1} 超时")
                }
            }
            val random = Random().nextInt(2)
            println("任务${i + 1} $random")
            val time = if (random > 0) 1200L else 500L
            Timer().schedule(time){
                flag=true
                println("任务${i + 1} on final")
            }
        }*/
/*        for (i in 60 downTo 0 step 1) {
            println("$i s后重新获取")
        }
        val empty: String? = null
        println(empty.toString().isNullOrEmpty())
        val string = "173 74134"
        // ^\\d{3} \\d{4} \\d{4}$
        val re = Regex("(\\d{3})(\\d{1,4})(\\d{1,4})")
        println(re.matchEntire(string)?.groupValues ?: "")
        val result = string.replace(re, "$1 $2 $3")
        println(result)
        println(result.length)
        println('a'.plus(" "))
        val s = string.toCharArray().toMutableList()

        if (s.size >= 4 && !s[3].isWhitespace()) {
            s.add(3, ' ')
        }
        if (s.size >= 9 && !s[8].isWhitespace()) {
            s.add(8, ' ')
        }
        val a = String(s.toCharArray())
        println("$a>>>${a.length}")*/
//        val str = "{\"code\":\"111\",\"name\":\"wjf\"}"
//        val result = Gson().fromJson(str, Student::class.java)
//        println(result)
//        GsonBuilder().registerTypeAdapter()
/*        val count = 2
        for (i in 0 until count) {
            print(i)
        }*/
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

object Util {

    fun output1(title: String?, msg: String? = null) {
        println("title:$title;msg:$msg")
    }
}
