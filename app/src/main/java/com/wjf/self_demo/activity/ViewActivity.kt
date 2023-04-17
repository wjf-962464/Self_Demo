package com.wjf.self_demo.activity

import android.graphics.Rect
import android.os.Handler
import android.util.Log
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.ActivityViewBinding
import com.wjf.self_demo.widget.AnchorDialog
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
            val drawable = MaterialShapeDrawable(
                ShapeAppearanceModel
                    .Builder()
                    .setAllCorners(RoundedCornerTreatment())
                    .setAllCornerSizes(9.dp<Float>())
                    .setBottomEdge(TriangleBottomEdgeTreatment(18f.dp(), 8f.dp(), 88f.dp(),9.dp()))
                    .build()
            )
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
    }

    override fun bindLayout(): ActivityViewBinding {
        return ActivityViewBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
    }
}
