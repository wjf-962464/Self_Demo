package org.jxxy.debug.corekit.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.jxxy.debug.corekit.R

private val centerCrop: CenterCrop by lazy { CenterCrop() }
private val errorPic = ResourceUtil.getDrawable(R.drawable.img_loading_error)
private val roundedCornersMap: MutableMap<Int, RequestOptions> = mutableMapOf()
private fun roundedCorner(radius: Int): RequestOptions {
    return roundedCornersMap.getOrPut(radius) {
        RequestOptions().transform(centerCrop, RoundedCorners(radius))
    }
}

/**
 * @param url 图片链接
 * @param isCircle 是否为圆
 */
fun ImageView.load(url: String?, isCircle: Boolean = false) {
    val a = Glide.with(this)
        .load(url)
        .centerCrop()
    if (isCircle) {
        a.circleCrop().error(errorPic).into(this)
    } else {
        a.into(this)
    }
}

/**
 * @param drawableResId Drawable路径id
 * @param isCircle 是否为圆
 */
fun ImageView.load(@DrawableRes drawableResId: Int, isCircle: Boolean = false) {
    val a = Glide.with(this)
        .load(ResourceUtil.getDrawable(drawableResId))
        .centerCrop()
    if (isCircle) {
        a.circleCrop().error(errorPic).into(this)
    } else {
        a.into(this)
    }
}

/**
 * @param url 图片链接
 * @param radius 圆角弧度
 */
fun ImageView.load(url: String?, radius: Int) {
    Glide.with(this)
        .load(url)
        .apply(roundedCorner(radius))
        .error(errorPic)
        .into(this)
}

/**
 * @param drawableResId Drawable路径id
 * @param radius 圆角弧度
 */
fun ImageView.load(@DrawableRes drawableResId: Int, radius: Int) {
    Glide.with(this)
        .load(ResourceUtil.getDrawable(drawableResId))
        .apply(roundedCorner(radius))
        .error(errorPic)
        .into(this)
}
