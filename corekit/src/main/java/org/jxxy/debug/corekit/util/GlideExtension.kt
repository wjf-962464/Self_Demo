package org.jxxy.debug.corekit.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

private val glideTransform: RequestOptions by lazy {
    RequestOptions.bitmapTransform(CenterCrop())
}

/**
 * @param url 图片链接
 * @param isCircle 是否为圆
 */
fun ImageView.load(url: String?, isCircle: Boolean = false) {
    Glide.with(this)
        .load(url)
        .apply(if (isCircle) glideTransform.transform(CircleCrop()) else glideTransform)
        .into(this)
}

/**
 * @param drawableResId Drawable路径id
 * @param isCircle 是否为圆
 */
fun ImageView.load(@DrawableRes drawableResId: Int, isCircle: Boolean = false) {
    Glide.with(this)
        .load(ResourceUtil.getDrawable(drawableResId))
        .apply(if (isCircle) glideTransform.transform(CircleCrop()) else glideTransform)
        .into(this)
}
/**
 * @param url 图片链接
 * @param radius 圆角弧度
 */
fun ImageView.load(url: String?, radius: Int) {
    Glide.with(this)
        .load(url)
        .apply(glideTransform.transform(RoundedCorners(radius)))
        .into(this)
}
/**
 * @param drawableResId Drawable路径id
 * @param radius 圆角弧度
 */
fun ImageView.load(@DrawableRes drawableResId: Int, radius: Int) {
    Glide.with(this)
        .load(ResourceUtil.getDrawable(drawableResId))
        .apply(glideTransform.transform(RoundedCorners(radius)))
        .into(this)
}
