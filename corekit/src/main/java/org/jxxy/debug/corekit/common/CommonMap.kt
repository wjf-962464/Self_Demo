package org.jxxy.debug.corekit.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface CommonMap<T : ViewHolderType, V : RecyclerView.ViewHolder> {
    fun createViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): V
    fun bindViewHolder(entity: T, holder: V)
}
