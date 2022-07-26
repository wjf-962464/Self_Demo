package org.jxxy.debug.corekit.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class CommonMap<V : ViewBinding, T : MultipleType> {
    lateinit var view: V
        private set

    fun onCreateViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        view = bindLayout(layoutInflater, parent)
        return view.root
    }

    protected abstract fun bindLayout(layoutInflater: LayoutInflater, parent: ViewGroup): V
    abstract fun bindViewHolder(entity: T, view: V, position: Int, context: Context)
    fun bindViewHolder(entity: T, view: V, position: Int, payload: Any, context: Context) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
