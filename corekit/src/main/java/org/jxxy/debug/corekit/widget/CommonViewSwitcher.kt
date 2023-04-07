package org.jxxy.debug.corekit.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ViewSwitcher
import androidx.lifecycle.LifecycleCoroutineScope
import org.jxxy.debug.corekit.util.gone
import java.lang.ref.WeakReference

abstract class CommonViewSwitcher<T, V : View> @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) :
    ViewSwitcher(context, attrs), ViewSwitcher.ViewFactory {
    protected var data: MutableList<T> = mutableListOf()
    protected var scope: WeakReference<LifecycleCoroutineScope>? = null
    protected var currentIndex = 0
    protected var supportLooper: Boolean = false
    abstract fun createAnimation()
    override fun reset() {
        super.reset()
        currentIndex = -1
        data.clear()
    }

    open fun setData(list: List<T>?, scope: LifecycleCoroutineScope) {
        reset()
        this.scope = WeakReference(scope)
    }

    open fun getCurrentItemData(): T? {
        return data.getOrNull(currentIndex)
    }

    abstract fun bindView(entity: T, view: V)

    open fun nextView(entity: T) {
        (nextView as? V)?.let {
            bindView(entity, it)
        }
        showNext()
    }

    /**
     * 如果需要支持无限循环的话，当item为null时，currentIndex切为-1，再调用本方法
     */
    open fun showNextView() {
        currentIndex++
        val item = getCurrentItemData()
        if (item != null) {
            nextView(item)
        } else {
            if (supportLooper) {
                // 支持无限循环，则进行二次检测
                currentIndex = 0
                val item2 = getCurrentItemData()
                if (item2 != null) {
                    nextView(item2)
                } else {
                    gone()
                }
            } else {
                gone()
            }
        }
    }
}
