package com.wjf.self_demo.activity

import android.animation.ObjectAnimator
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.postDelayed
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityViewBinding
import com.wjf.self_demo.widget.AnchorDialog
import com.wjf.self_demo.widget.PoolViewFactory
import com.wjf.self_demo.widget.TriangleBottomEdgeTreatment
import org.jxxy.debug.barcode.encode.EncodingUtils
import org.jxxy.debug.corekit.common.BaseActivity
import org.jxxy.debug.corekit.util.ResourceUtil
import org.jxxy.debug.corekit.util.dp
import org.jxxy.debug.corekit.util.singleClick

/** @author Wangjf2-DESKTOP
 */
class ViewActivity : BaseActivity<ActivityViewBinding>() {
    override fun initView() {
        EncodingUtils.bindBarCode("sadad", view.barcode)
        val dialog = AnchorDialog() {
            val rect = Rect()
            val result = view.icon2.getGlobalVisibleRect(rect)
            Log.d("wjftc", "initView: $result $rect ${rect.height()} ${view.icon2.measuredHeight}")
        }
        val drawable by lazy {
            val drawable = MaterialShapeDrawable(ShapeAppearanceModel.Builder().setAllCorners(RoundedCornerTreatment()).setAllCornerSizes(9.dp<Float>()).setBottomEdge(TriangleBottomEdgeTreatment(18f.dp(), 8f.dp(), 88f.dp(), 9.dp())).build())
            drawable.setTint(ResourceUtil.getColor(R.color.color_yellow))
            drawable
        }
        view.icon2.background = drawable
        view.icon1.singleClick {
            dialog.show(supportFragmentManager, view.icon2)
        }
        view.icon2.singleClick {
            dialog.show(supportFragmentManager, view.icon1)
        }

        view.icon3.singleClick {
            dialog.show(supportFragmentManager, view.icon3)
        }
        view.icon4.singleClick {
            dialog.show(supportFragmentManager, view.icon4)
        }
        val list = mutableListOf("我是跑马灯1", "我不是跑马灯", "你猜我是不是跑马灯")
        var position = 0
        val paint = Paint()
        paint.textSize = 12f
        view.marqueeView.setFactory(object : PoolViewFactory {
            override fun makeView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
                val view = TextView(this@ViewActivity)
                view.setPadding(0, 0, 20.dp(), 0)
                view.text = "跑马灯"
                view.textSize = 12f
                view.setTextColor(ResourceUtil.getColor(R.color.white))
                return view
            }

            override fun setAnimator(objectAnimator: ObjectAnimator, width: Int, parentWidth: Int) {
                objectAnimator.duration = (parentWidth + width) * 5L
            }
        })
//        view.marqueeView.start()
        view.marqueeView.postDelayed(5000L) {
            view.marqueeView.pause()
        }
        view.marqueeView.postDelayed(7000L) {
            view.marqueeView.resume()
        }
    }

    override fun bindLayout(): ActivityViewBinding {
        return ActivityViewBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
    }
}
