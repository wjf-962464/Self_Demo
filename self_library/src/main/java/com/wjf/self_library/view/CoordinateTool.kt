package com.wjf.self_library.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.PointF
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.*

/**
 * 坐标系计算工具类
 */
object CoordinateTool {
    /**
     * 根据角度、长度在射线上得到点坐标
     * @param startPoint 起始点坐标
     * @param length 要求的点到起始点的直线距离 -- 线长
     * @param angle 与x轴的夹角角度
     * @return 坐标点
     */
    fun calculatePoint(startPoint: PointF, length: Float, angle: Float): PointF {
        // Math.cos传参是弧度，Math.toRadians用于将角度转换为弧度
        // x差值，a=cosα *c
        val deltaX = (cos(Math.toRadians(angle.toDouble())) * length).toFloat()
        println("角度值：$angle")
        // y差值，b=sinα *c，这里由于坐标系翻转，所以正弦值要偏移π°，sin(x+180)=-sinx
        val deltaY = (sin(Math.toRadians((180 - angle).toDouble())) * length).toFloat()
        return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
    }

    /**
     * 求夹角角度值
     */
    fun includedAngle(O: PointF, A: PointF, B: PointF): Float {
        // cosAOB=OA*OB向量内积/（|OA|*|OB|模积）
        // OA*OB 向量=(Ax-Ox)(Bx-Ox)+(Ay-Oy)*(By-Oy)
        val vectorProduct = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y)
        val a = getPointBetweenDistance(A, O)

        // OB 的长度
        val b = getPointBetweenDistance(B, O)
        val cosAOB = vectorProduct / (a * b)

        // Math.acos反余弦得到角弧度，Math.toDegrees将弧度转换成角度，此时的角度是不带方向的
        val angleAOB = Math.toDegrees(acos(cosAOB.toDouble())).toFloat()
        println("angleAOB：$angleAOB")
        // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值，点b对于AO连线的方向，>0为左
        val direction = if (A.x == B.x || O.x == B.x) {
            0f
        } else {
            (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x)
        }
        println("direction：$direction")
        return when {
            direction == 0f -> {
                -180f
            }
            direction > 0 -> {
                // 与O点垂直，使其默认向右，B在O下方为-180°意为与AO点反向
                // 朝右为负
                println("在右边")
                -angleAOB
            }
            direction < 0 -> {
                // 朝左为负
                println("在左边")
                angleAOB
            }
            else -> {
                println("方向角度计算异常$angleAOB")
                angleAOB
            }
        }
    }

    /**
     * 求两点之间的距离
     */
    fun getPointBetweenDistance(A: PointF, B: PointF): Float {
        return sqrt((A.x - B.x).toDouble().pow(2) + (A.y - B.y).toDouble().pow(2)).toFloat()
    }

    /**
     * 求view的重心坐标
     */
    fun getViewGravityPoint(view: View): PointF {
        val result = PointF()
        val offset = IntArray(2)
        view.getLocationInWindow(offset)
        result.x = offset[0] + view.width / 2.0f
        result.y = offset[1] + view.height / 2.0f
        return result
    }

    fun getScaleAnimators(
        target: View,
        startPercent: Float,
        endPercent: Float,
        animators: MutableList<Animator>,
        duration: Long,
        delay: Long = 0
    ) {
        val scaleX = ObjectAnimator.ofFloat(target, "scaleX", startPercent, endPercent)
        scaleX.duration = duration
        scaleX.startDelay = delay
        scaleX.interpolator = LinearInterpolator()
        animators.add(scaleX)
        val scaleY = ObjectAnimator.ofFloat(target, "scaleY", startPercent, endPercent)
        scaleY.duration = duration
        scaleY.startDelay = delay
        scaleY.interpolator = LinearInterpolator()
        animators.add(scaleY)
    }
}
