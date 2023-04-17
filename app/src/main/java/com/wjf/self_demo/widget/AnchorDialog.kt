package com.wjf.self_demo.widget

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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
import org.jxxy.debug.corekit.util.singleClick
import java.lang.ref.WeakReference

class AnchorDialog(val callBack: () -> Unit) : BaseDialog<DialogAnchorBinding>() {
    init {
        ifCancelOnTouch = true
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private val drawable by lazy {
        val drawable = MaterialShapeDrawable(
            ShapeAppearanceModel
                .Builder()
                .setAllCorners(RoundedCornerTreatment())
                .setAllCornerSizes(9.dp<Float>())
                .setBottomEdge(TriangleBottomEdgeTreatment(18f.dp(), 8f.dp(), 88f.dp(), 9.dp()))
                .build()
        )
        drawable.setTint(ResourceUtil.getColor(R.color.white))
        drawable
    }

    override fun initView() {
        view.root.singleClick {
            callBack.invoke()
        }
        view.container.background = drawable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anchor?.get()?.let {
            val location = IntArray(2)
            it.getLocationOnScreen(location)

            val displayFrame = Rect()
            it.rootView.getWindowVisibleDisplayFrame(displayFrame)
            val alignTop = if (location[1] > (displayFrame.bottom - displayFrame.top) / 2) {
                Log.d("wjftc", "在中间以下")
                true
            } else {
                Log.d("wjftc", "在中间以上")
                true
            }

            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    0,
                    View.MeasureSpec.UNSPECIFIED
                ),
                View.MeasureSpec.makeMeasureSpec(
                    0,
                    View.MeasureSpec.UNSPECIFIED
                )
            )
            val lp = dialog?.window?.attributes
            val height = view.measuredHeight + 8.dp<Int>()
            lp?.height = height
            Log.d("wjftc", "onViewCreated: ${location[0]}")
            lp?.apply {
                gravity = Gravity.START.or(Gravity.TOP)
                x = location[0] + ((it.width - 8.dp<Float>())/2 - 88f.dp<Float>()).toInt()
                y = if (alignTop) {
                    location[1] - height
                } else {
                    location[1] + it.measuredHeight
                }
            }
            dialog?.window?.attributes = lp
        }
    }

    override fun bindLayout(inflater: LayoutInflater): DialogAnchorBinding =
        DialogAnchorBinding.inflate(inflater)

    private var anchor: WeakReference<View>? = null
    fun show(fm: FragmentManager, anchor: View) {
        this.anchor = WeakReference(anchor)
        super.show(fm, "AnchorDialog")
    }
}

class TriangleBottomEdgeTreatment(
    private val width: Float,
    private val height: Float,
    private val offset: Float,
    private val radius: Float
) : EdgeTreatment() {

    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath
    ) {
        val start = length - offset - width + radius
        shapePath.lineTo(start, 0f)
        shapePath.lineTo(start + width / 2, -height * interpolation)
        shapePath.lineTo(start + width, 0f)
        shapePath.lineTo(length, 0f)
    }
}
