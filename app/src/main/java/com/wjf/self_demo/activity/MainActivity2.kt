package com.wjf.self_demo.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import haipo.com.bezierintroduce.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.hide()
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.confirmBtn.setOnClickListener(
            View.OnClickListener {
                val sx = binding.startPointX.text.toString().ifEmpty { "0" }.toFloat()
                val sy = binding.startPointY.text.toString().ifEmpty { "0" }.toFloat()
                val ex = binding.endPointX.text.toString().ifEmpty { "0" }.toFloat()
                val ey = binding.endPointY.text.toString().ifEmpty { "0" }.toFloat()
                if (sx == 0f || sy == 0f || ex == 0f || ey == 0f) {
                    Toast.makeText(this@MainActivity2, "非法输入", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                binding.basic.setStartX(sx, sy, ex, ey)
            }
        )
        binding.anim.setOnClickListener {
            binding.basic.startAnim()
        }
        binding.startPointX.setText("300")
        binding.startPointY.setText("200")

        binding.endPointX.setText("300")
        binding.endPointY.setText("800")
        binding.confirmBtn.callOnClick()
    }
}
