package androidx.recyclerview.widget

import android.content.Context
import androidx.viewbinding.ViewBinding
import org.jxxy.debug.corekit.recyclerview.ViewHolderTag

abstract class SelfViewHolder<V : ViewBinding, T>(val view: V) : RecyclerView.ViewHolder(view.root), ViewHolderTag<T> {
    val context: Context = itemView.context

    /**
     * holder操作，含payload
     * 默认空实现
     *
     * @param entity 数据实体
     */
    override fun setHolder(entity: T, payload: Any) {
    }

    fun bindItemViewType(type: Int) {
        mItemViewType = type
    }
}
