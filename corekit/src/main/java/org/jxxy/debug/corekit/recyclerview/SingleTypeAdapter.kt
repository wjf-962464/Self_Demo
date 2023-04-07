package org.jxxy.debug.corekit.recyclerview

import androidx.recyclerview.widget.RecyclerView

abstract class SingleTypeAdapter<T> protected constructor() :
    BaseTypeAdapter<T>() {
    companion object {
        private const val SINGLE_TYPE = 1
    }

    override fun bindHolder(holder: ViewHolderTag<T>, entity: T, position: Int) {
        holder.setHolder(entity)
    }

    override fun bindHolder(
        holder: ViewHolderTag<T>,
        entity: T,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.setHolder(entity, payloads[0])
    }

    override fun getItemViewType(position: Int): Int {
        return SINGLE_TYPE
    }

    override fun holderBindExtras(holder: RecyclerView.ViewHolder, position: Int) {
    }
}
