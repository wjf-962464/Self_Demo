package org.jxxy.debug.corekit.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.jxxy.debug.corekit.util.dp

class CommonItemDecoration(
    private val interval: Float,
    @RecyclerView.Orientation private val orientation: Int = RecyclerView.VERTICAL
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        when (orientation) {
            RecyclerView.VERTICAL -> {
                if (position == 0) {
                    outRect.top = 0
                } else {
                    outRect.top = interval.dp()
                }
            }
            RecyclerView.HORIZONTAL -> {
                if (position == 0) {
                    outRect.left = 0
                } else {
                    outRect.left = interval.dp()
                }
            }
            else -> {
                // do nothing
            }
        }
    }
}
