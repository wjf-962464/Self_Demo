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
        view.confirmBtn.setOnClickListener(
            View.OnClickListener {
                val sx = view.startPointX.text.toString().ifEmpty { "0" }.toFloat()
                val sy = view.startPointY.text.toString().ifEmpty { "0" }.toFloat()
                val ex = view.endPointX.text.toString().ifEmpty { "0" }.toFloat()
                val ey = view.endPointY.text.toString().ifEmpty { "0" }.toFloat()
                if (sx == 0f || sy == 0f || ex == 0f || ey == 0f) {
                    Toast.makeText(this@BezierActivity, "非法输入", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                view.basic.setStartX(sx, sy, ex, ey)
            }
        )
        view.anim.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.basic.startAnim()
            } else {
                Toast.makeText(this, "版本过低", Toast.LENGTH_SHORT).show()
            }
        }
        view.startPointX.setText("800")
        view.startPointY.setText("1300")

        view.endPointX.setText("800")
        view.endPointY.setText("300")
        view.confirmBtn.callOnClick()
    }

    override fun initData() {
    }
}
