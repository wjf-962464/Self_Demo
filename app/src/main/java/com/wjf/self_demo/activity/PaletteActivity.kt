package com.wjf.self_demo.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityPaletteBinding
import org.jxxy.debug.corekit.common.BaseActivity

class PaletteActivity : BaseActivity<ActivityPaletteBinding>() {

    override fun initView() {
        createPaletteAsync(BitmapFactory.decodeResource(resources, R.drawable.img_1))
    }

    @SuppressLint("WrongConstant")
    fun createPaletteAsync(bitmap: Bitmap) {
        val filter = Palette.Filter { k, m ->
            k == -10448656
        }
        val target = androidx.palette.graphics.Target.Builder()
            .setMaximumLightness(0.5f)
            .build()
        Palette.Builder(bitmap)
            .addFilter(filter) // 添加过滤器，只能使用指定的某些颜色
            .addTarget(target)
            .setRegion(0, 0, bitmap.width, bitmap.height) // 设置从某一区域中获取颜色
//            .setRegion(0, 0, bitmap.width, (bitmap.height * 0.1).toInt())//设置从某一区域中获取颜色
            .generate { palette ->
                val defaultColor = ContextCompat.getColor(this, R.color.colorPrimary)

                val colors = intArrayOf(
                    palette?.getLightVibrantColor(defaultColor) ?: defaultColor,
                    resources.getColor(R.color.white)
                )
                val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
                drawable.cornerRadius = 25F
                drawable.gradientType = GradientDrawable.RECTANGLE
                view.colorBar.background = drawable
            }
    }

    override fun bindLayout(): ActivityPaletteBinding {
        return ActivityPaletteBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
    }
}
