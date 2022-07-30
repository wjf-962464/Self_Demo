package org.jxxy.debug.corekit.recyclerview

import androidx.viewbinding.ViewBinding

abstract class MultipleViewHolder<V : ViewBinding, T : MultipleType>(view: V) :
    SingleViewHolder<V, T>(view)
