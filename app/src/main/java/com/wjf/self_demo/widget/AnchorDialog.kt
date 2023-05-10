package com.wjf.self_demo.widget

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.shape.*
import com.wjf.self_demo.R
import com.wjf.self_demo.databinding.DialogAnchorBinding
import org.jxxy.debug.corekit.common.BaseDialog
import org.jxxy.debug.corekit.util.ResourceUtil
import org.jxxy.debug.corekit.util.dp
import java.lang.ref.WeakReference

class AnchorDialog : BaseDialog<DialogAnchorBinding>() {
    init {
        ifCancelOnTouch = true
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private val drawableRadius: Float = 9.dp()
    private val triangleWidth: Float = 18.dp()
    private val triangleHeight: Float = 8.dp()
    private val triangleOffset: Float = 88.dp()
    private val drawable by lazy {
        val drawable = MaterialShapeDrawable(ShapeAppearanceModel.Builder().setAllCorners(RoundedCornerTreatment()).setAllCornerSizes(9.dp<Float>()).setBottomEdge(TriangleBottomEdgeTreatment(triangleWidth, triangleHeight, triangleOffset, drawableRadius)).build())
        drawable.setTint(ResourceUtil.getColor(R.color.white))
        drawable
    }

    override fun getView(inflater: LayoutInflater, parent: ViewGroup?) =
        DialogAnchorBinding.inflate(inflater, parent, false)

    override fun initView(view: DialogAnchorBinding) {
        view.container.background = drawable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anchor?.get()?.let {
            // 获取锚点view在屏幕中的x,y位置，即左上角定点位置[left,top]
            val location = IntArray(2)
            it.getLocationOnScreen(location)

            // 对弹窗主体进行测量，因为自身是wrap，里面的高度会变化，需要自行测量。当然，也可以在view.post中获取
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            val lp = dialog?.window?.attributes
            // 需要加上小三角形的高度形成自身真正的高度
            val clipHeight = view.measuredHeight + triangleHeight.toInt()
            lp?.apply {
                height = clipHeight
                // gravity默认为start|top，如果更改，x|y的定位方式会变化，比如设置bottom，那y就相当于margin bottom
                gravity = Gravity.START.or(Gravity.TOP)
                // -triangleOffset先将箭头的左端与锚点view左端对齐，+it.width/2将箭头左端与中心点对齐
                // -triangleWidth/2将箭头中心与中心点对齐
                x = location[0] + ((it.width - triangleWidth) / 2f - triangleOffset).toInt()

                // 这里的计算可以自定义，在上方只需减去自身的高度
                // 在下方的话，那就加上锚点view的高度
                y = location[1] - clipHeight
            }
            dialog?.window?.attributes = lp
        }
    }

    private var anchor: WeakReference<View>? = null
    fun show(fm: FragmentManager, anchor: View) {
        this.anchor = WeakReference(anchor)
        super.show(fm)
    }
}

class TriangleBottomEdgeTreatment(private val width: Float, private val height: Float, private val offset: Float, private val radius: Float) : EdgeTreatment() {

    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        // 减多了，因为是从圆角的弧结束开始的，需要加回去
        val start = length - offset - width + radius
        shapePath.lineTo(start, 0f)
        shapePath.lineTo(start + width / 2, -height)
        shapePath.lineTo(start + width, 0f)
        shapePath.lineTo(length, 0f)
    }
}
