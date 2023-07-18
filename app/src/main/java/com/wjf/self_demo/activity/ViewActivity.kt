package com.wjf.self_demo.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.wjf.self_demo.IndexActivity
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
import org.jxxy.debug.corekit.util.startActivity

/** @author Wangjf2-DESKTOP
 */
class ViewActivity : BaseActivity<ActivityViewBinding>() {
    override fun initView() {
        EncodingUtils.bindBarCode("sadad", view.barcode)
        val dialog = AnchorDialog()
        val drawable by lazy {
            val drawable = MaterialShapeDrawable(ShapeAppearanceModel.Builder().setAllCorners(RoundedCornerTreatment()).setAllCornerSizes(9.dp<Float>()).setBottomEdge(TriangleBottomEdgeTreatment(18f.dp(), 8f.dp(), 88f.dp(), 9.dp())).build())
            drawable.setTint(ResourceUtil.getColor(R.color.color_yellow))
            drawable
        }
        view.icon2.background = drawable
        view.icon1.singleClick {
            startActivity<IndexActivity>()
//            dialog.show(supportFragmentManager, view.icon2)
        }
        view.icon2.singleClick {
            startActivity<ViewActivity>()
//            dialog.show(supportFragmentManager, view.icon1)
        }

        view.icon3.singleClick {
            dialog.show(supportFragmentManager, view.icon3)
        }
        view.icon4.singleClick {
            dialog.show(supportFragmentManager, view.icon4)
        }
        val list = mutableListOf("我是跑马灯1", "我不是跑马灯蛤", "你猜我是不是跑马灯hhh")
        var position = 0
        view.marqueeView.setFactory(object : PoolViewFactory {
            override fun makeView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
                val view = TextView(this@ViewActivity)
                view.setPadding(0, 0, 20.dp(), 0)
                view.textSize = 12f
                view.setTextColor(ResourceUtil.getColor(R.color.white))
                return view
            }

            override fun setAnimator(objectAnimator: ObjectAnimator, width: Int, parentWidth: Int) {
                objectAnimator.duration = (parentWidth + width) * 5L
            }

            override fun setView(view: View): Boolean {
                (view as? TextView)?.text = list[position++ % list.size]
                return true
            }
        })
//        view.marqueeView.enableAnimated = false
        var status = true
        view.marqueeView.singleClick {
            if (status) {
                it.pause()
            } else {
                it.resume()
            }
            status = !status
        }
//        view.marqueeView.start()
        /*view.marqueeView.postDelayed(5000L) {
            view.marqueeView.pause()
        }
        view.marqueeView.postDelayed(7000L) {
            view.marqueeView.resume()
        }*/
    }

    override fun bindLayout(): ActivityViewBinding {
        return ActivityViewBinding.inflate(layoutInflater)
    }

    override fun subscribeUi() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("ActivityB - onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        println("ActivityB - onPause")
    }

    override fun onStop() {
        super.onStop()
        println("ActivityB - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("ActivityB - onDestroy")
    }

    override fun onStart() {
        super.onStart()
        println("ActivityB - onStart")
    }

    override fun onRestart() {
        super.onRestart()
        println("ActivityB - onRestart")
    }

    override fun onResume() {
        super.onResume()
        println("ActivityB - onResume")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("ActivityB - onNewIntent")
    }
}
