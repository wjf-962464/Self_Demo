package org.jxxy.debug.corekit.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val spanCount: Int, private val spacingHorizontal: Float, private val spacingVertical: Float, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // 获取当前 item 的位置
        val column = position % spanCount // 计算当前 item 在一行中的列数
        if (includeEdge) {
            // 如果需要在边缘添加间距，则为每个 item 都添加相同的间距

            outRect.left = (spacingHorizontal - column * spacingHorizontal / spanCount).toInt()
            outRect.right = ((column + 1) * spacingHorizontal / spanCount).toInt()
            if (position < spanCount) {
                outRect.top = spacingVertical.toInt()
            }
            outRect.bottom = spacingVertical.toInt()
        } else {
            // 如果不需要在边缘添加间距，则只在内部 item 之间添加间距
            outRect.left = (column * spacingHorizontal / spanCount).toInt()
            outRect.right = (spacingHorizontal - (column + 1) * spacingHorizontal / spanCount).toInt()
            if (position >= spanCount) {
                outRect.top = spacingVertical.toInt()
            }
        }
    }
}
