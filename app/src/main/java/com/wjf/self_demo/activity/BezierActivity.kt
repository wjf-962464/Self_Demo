package com.wjf.self_demo.activity

import android.os.Build
import android.view.View
import android.widget.Toast
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityBezierBinding
import com.wjf.self_library.common.BaseActivity

class BezierActivity : BaseActivity<ActivityBezierBinding>() {

    override fun setLayout(): Int = R.layout.activity_bezier

    override fun initView() {
/*        val startPoint = PointF(2f, 2f)
        // 购物车重心
        val endPoint = PointF(2f, 1f)
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
        println("最终控制点坐标:$controlPoint")*/
        view.confirmBtn.setOnClickListener(
            View.OnClickListener {
                val sx = view.startPointX.text.toString().ifEmpty { "0" }.toFloat()
                val sy = view.startPointY.text.toString().ifEmpty { "0" }.toFloat()
                val ex = view.endPointX.text.toString().ifEmpty { "0" }.toFloat()
                val ey = view.endPointY.text.toString().ifEmpty { "0" }.toFloat()
                val au = view.angleUp.text.toString().ifEmpty { "0" }.toInt()
                val ad = view.angleDown.text.toString().ifEmpty { "0" }.toInt()
                val lu = view.lineUp.text.toString().ifEmpty { "0" }.toInt()
                val ld = view.lineDown.text.toString().ifEmpty { "0" }.toInt()
                if (sx == 0f || sy == 0f || ex == 0f || ey == 0f || au == 0 || ad == 0 || lu == 0 || ld == 0) {
                    Toast.makeText(this@BezierActivity, "非法输入", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                view.basic.setStartX(sx, sy, ex, ey, au, ad, lu, ld)
            }
        )
        view.anim.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.basic.startAnim()
            } else {
                Toast.makeText(this, "版本过低", Toast.LENGTH_SHORT).show()
            }
        }
        view.startPointX.setText("200")
        view.startPointY.setText("1300")

        view.endPointX.setText("200")
        view.endPointY.setText("300")

        view.angleUp.setText("3")
        view.angleDown.setText("11")
        view.lineUp.setText("7")
        view.lineDown.setText("15")
        view.confirmBtn.callOnClick()
    }

    override fun initData() {
    }
}
