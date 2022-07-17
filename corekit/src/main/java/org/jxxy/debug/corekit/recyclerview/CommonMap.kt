package org.jxxy.debug.corekit.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface CommonMap<T : MultipleType, V : RecyclerView.ViewHolder> {
    fun createViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): V
    fun bindViewHolder(entity: T, holder: V)
}
