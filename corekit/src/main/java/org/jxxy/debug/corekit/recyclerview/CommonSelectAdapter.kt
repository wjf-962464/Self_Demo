package org.jxxy.debug.corekit.recyclerview

import androidx.recyclerview.widget.RecyclerView

abstract class CommonSelectAdapter :
    MultipleTypeAdapter(),
    ItemSelectListener {
    companion object {
        const val PAYLOAD_STATE_CHANGE = 99
    }

    protected var curPosition: Int = -1

    override fun onItemClick(position: Int): Boolean {
        if (curPosition == position) {
            return false
        }
        notifyOnClick(position)
        actionOnClick(position)
        return true
    }

    open fun notifyOnClick(position: Int) {
        curPosition = position
        notifyItemRangeChanged(0, itemCount, PAYLOAD_STATE_CHANGE)
    }

    abstract fun actionOnClick(position: Int)

    override fun bindHolder(
        holder: ViewHolderTag<MultipleType>,
        entity: MultipleType,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (payloads[payloads.lastIndex]) {
            PAYLOAD_STATE_CHANGE -> {
                (holder as? BaseSelectBehavior)?.dispatchState(getSelectState(position))
            }
            else -> super.bindHolder(holder, entity, position, payloads)
        }
    }

    override fun holderBindExtras(holder: RecyclerView.ViewHolder, position: Int) {
        super.holderBindExtras(holder, position)
        when (holder) {
            is BaseSelectBehavior -> {
                holder.dispatchState(getSelectState(position))
            }
        }
    }

    abstract fun getSelectState(position: Int): Int
}
