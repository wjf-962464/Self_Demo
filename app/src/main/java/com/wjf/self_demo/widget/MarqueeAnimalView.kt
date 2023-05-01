package com.wjf.self_demo.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.util.*

class MarqueeAnimalView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), Animator.AnimatorListener, View.OnAttachStateChangeListener {

    companion object {
        private const val MAX_POOL_SIZE = 20
        private const val PROPERTY_NAME = "translationX"
    }

    init {
        addOnAttachStateChangeListener(this)
    }

    // <editor-fold desc="View回收复用">
    private val layoutInflater = LayoutInflater.from(context)
    private val sPoolSync = Any()
    private val queue = ArrayDeque<View>()
    private val animators = hashMapOf<String, ObjectAnimator>()
    private var factory: PoolViewFactory? = null
    fun setFactory(factory: PoolViewFactory) {
        this.factory = factory
    }

    private fun obtain(): View? {
        synchronized(sPoolSync) {
            if (!isAttachedToWindow) {
                return null
            }
            if (queue.isNotEmpty()) {
                return queue.poll()
            }
        }
        return factory?.makeView(layoutInflater, this@MarqueeAnimalView)?.apply {
            addView(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun recycle(view: View) {
        synchronized(sPoolSync) {
            if (queue.size < MAX_POOL_SIZE) {
                queue.offer(view)
            }
        }
    }

    private fun next(view: View?) {
        view ?: return
        if (factory?.setView(view) == true) {
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        }
        val width = view.measuredWidth
        val parentWidth = measuredWidth
        val targetValue = parentWidth - width
        val animator = ObjectAnimator.ofFloat(view, PROPERTY_NAME, parentWidth.toFloat(), -width.toFloat()).apply {
            // null即为默认线性插值器
            interpolator = null
            addUpdateListener(
                RecyclerAnimatorUpdateListener(targetValue) {
                    next(obtain())
                    removeUpdateListener(it)
                }
            )
            addListener(this@MarqueeAnimalView)
            factory?.setAnimator(this, width, parentWidth)
        }
        animators["${view.hashCode()}-${animator.hashCode()}"] = animator
        animator.start()
    }
    // </editor-fold>

    // <editor-fold desc="使用">
    fun start() {
        if (measuredWidth == 0) {
            this.post {
                // 如果测量还未完成，那就等待post后发起
                next(obtain())
            }
            return
        }
        next(obtain())
    }

    fun stop() {
        val it = animators.values.iterator()
        while (it.hasNext()) {
            val i = it.next()
            it.remove()
            i.cancel()
        }
    }

    fun pause() {
        for (i in animators.values) {
            i.pause()
        }
    }

    fun resume() {
        for (i in animators.values) {
            i.resume()
        }
    }

    override fun onAnimationStart(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        (animation as? ObjectAnimator)?.let { animator ->
            (animator.target as? View)?.let { view ->
                animators.remove("${view.hashCode()}-${animator.hashCode()}")
                recycle(view)
            }
            // target释放
            animator.target = null
        }
    }

    override fun onAnimationCancel(animation: Animator?) {
        (animation as? ObjectAnimator)?.let { animator ->
            (animator.target as? View)?.let { view ->
                view.translationX = measuredWidth.toFloat()
            }
        }
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    // </editor-fold>

    // <editor-fold desc="自动绑定">
    override fun onViewAttachedToWindow(v: View?) {
        if (animators.isNotEmpty()) {
            resume()
        } else {
            start()
        }
    }

    override fun onViewDetachedFromWindow(v: View?) {
        pause()
    }
    // </editor-fold>
}

interface PoolViewFactory {
    fun makeView(layoutInflater: LayoutInflater, parent: ViewGroup): View
    fun setAnimator(objectAnimator: ObjectAnimator, width: Int, parentWidth: Int)

    /**
     * 返回值，代表view是否需要重新测量
     */
    fun setView(view: View): Boolean
}

class RecyclerAnimatorUpdateListener(private val targetValue: Int, private val block: (ValueAnimator.AnimatorUpdateListener) -> Unit) : ValueAnimator.AnimatorUpdateListener {
    // 标志位，用于避免频繁回调
    private var postFlag = true
    override fun onAnimationUpdate(animation: ValueAnimator?) {
        if (postFlag) {
            postFlag = false
            (animation?.animatedValue as? Float)?.let {
                if (it < targetValue) {
                    block.invoke(this)
                    return@let
                }
                postFlag = true
            }
        }
    }
}
