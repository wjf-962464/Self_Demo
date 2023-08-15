package org.jxxy.debug.corekit.recyclerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.jxxy.debug.corekit.R
import org.jxxy.debug.corekit.util.ResourceUtil
import org.jxxy.debug.corekit.util.dp

class SlideBarItemDecoration : RecyclerView.ItemDecoration() {
    // 滑动条整体宽度
    private val scrollWidth = 30f.dp<Int>()

    // 滑动条选中部分宽度
    private val indicatorWidth = 17f.dp<Int>()

    // 滑动条的高度
    private val barHeight = 4f.dp<Int>()

    // recyclerview ITEM到滑动条的距离
    private val paddingBottom = 9f.dp<Int>()

    // 滑动条圆角
    private val indicatorRadius = 100f.dp<Float>()
    private val paint = Paint()

    init {
        paint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if ((parent.adapter?.itemCount ?: 0) <= 3) {
            return
        }
        // 居中
        val barX = (parent.width / 2 - scrollWidth / 2).toFloat()
        val barY = (parent.height - barHeight).toFloat()
        // 画底部背景
        paint.color = ResourceUtil.getColor(R.color.grey)
        c.drawRoundRect(barX, barY, barX + scrollWidth.toFloat(), barY + barHeight, indicatorRadius, indicatorRadius, paint)
        // 计算滑动比例
        val extent = parent.computeHorizontalScrollExtent()
        val range = parent.computeHorizontalScrollRange()
        val offset = parent.computeHorizontalScrollOffset()
        val maxEndX = (range - extent).toFloat()
        if (maxEndX > 0) {
            val proportion = offset / maxEndX

            val scrollableDistance = scrollWidth - indicatorWidth

            val offsetX = scrollableDistance * proportion
            paint.color = ResourceUtil.getColor(R.color.colorPrimary)
            // 画滑动选中条
            c.drawRoundRect(barX + offsetX, barY, barX + indicatorWidth.toFloat() + offsetX, barY + barHeight, indicatorRadius, indicatorRadius, paint)
        } else {
            paint.color = ResourceUtil.getColor(R.color.colorPrimary)
            c.drawRoundRect(barX, barY, barX + scrollWidth.toFloat(), barY + barHeight, indicatorRadius, indicatorRadius, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if ((parent.adapter?.itemCount ?: 0) <= 3) {
            outRect.bottom = 0
        } else {
            outRect.bottom = barHeight + paddingBottom
        }
    }
}
