package org.jxxy.debug.corekit.recyclerview

import androidx.recyclerview.widget.RecyclerView

abstract class MultipleTypeAdapter protected constructor() :
    BaseTypeAdapter<MultipleType>() {
    companion object {
        private const val NONE_TYPE = -1
    }

    override fun bindHolder(
        holder: ViewHolderTag<MultipleType>,
        entity: MultipleType,
        position: Int
    ) {
        holder.setHolder(entity)
    }

    override fun bindHolder(
        holder: ViewHolderTag<MultipleType>,
        entity: MultipleType,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.setHolder(entity, payloads[0])
    }

    override fun getItemViewType(position: Int): Int {
        return data.getOrNull(position)?.viewType() ?: NONE_TYPE
    }

    override fun holderBindExtras(holder: RecyclerView.ViewHolder, position: Int) {
    }
}
