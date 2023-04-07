package org.jxxy.debug.corekit.recyclerview

import androidx.viewbinding.ViewBinding

abstract class MultipleViewHolder2<V : ViewBinding, T : MultipleType>(val view: V) :
    MultipleViewHolder<T>(view.root)
