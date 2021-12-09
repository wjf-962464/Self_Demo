package com.wjf.self_demo

import android.graphics.PointF
import com.wjf.self_library.view.CoordinateTool

fun main() {
    try {
        val startPoint = PointF(2f, 2f)
        // 购物车重心
        val endPoint = PointF(1f, 5f)
        // 商品上点
        val upPoint = PointF(startPoint.x, startPoint.y - 1)
        // 商品与购物车形成的夹角
        val bigAngle = CoordinateTool.includedAngle(startPoint, upPoint, endPoint)
        println("商品与购物车形成的夹角:$bigAngle")
        // 夹角得出比例
        val difAngle = bigAngle * 3 / 11f
        println("夹角得出比例:$difAngle")
        // x轴正方向上任意一点
        val xPoint = PointF(startPoint.x + 1, startPoint.y)
        // 商品重心与商品上点连线与x轴的夹角
        val xAngle = CoordinateTool.includedAngle(
            startPoint,
            upPoint,
            xPoint
        )
        println("商品重心与商品上点连线与x轴的夹角:$xAngle")
        // 与x轴夹角，优化边缘算法
        val delta = xAngle - difAngle
        // 控制点2 的坐标
        val length = CoordinateTool.getPointBetweenDistance(startPoint, endPoint) * 7 / 15f
        val controlPoint = CoordinateTool.calculatePoint(startPoint, length, delta)
        println("最终控制点坐标:$controlPoint")
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
