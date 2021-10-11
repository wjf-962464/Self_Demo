package com.wjf.loadLayout.bezier

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.wjf.loadLayout.R

/** Description： @Author：桑小年 @Data：2016/7/20 17:35  */
class BasicPathView2 : LinearLayout {
    private lateinit var pointPaint: Paint
    private lateinit var mPaint: Paint
    private lateinit var linePath: Path
    private lateinit var movePath: Path
    private val startPoint = PointF()
    private val endPoint = PointF()
    private val touchPoint = PointF()
    private val cart = RectF()
    private val product = RectF()
    private val productWidth = 200f
    private val productHeight = 200f
    private val cartWidth = 100f
    private val cartHeight = 50f
    private val cube = RectF()
    private val cubePoint = PointF()
    private lateinit var imageView: RoundImageView
    private val percent = 3L
    private val layoutParams = LayoutParams(productWidth.toInt(), productHeight.toInt())

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        setWillNotDraw(false)
        mPaint = Paint()
        mPaint.isAntiAlias = true
        linePath = Path()
        movePath = Path()
        pointPaint = Paint()
        pointPaint.isAntiAlias = true
        pointPaint.isDither = true
        pointPaint.style = Paint.Style.FILL
        pointPaint.strokeWidth = 5f
        createNewImage()
    }

    fun createNewImage() {
        imageView = RoundImageView(context)

        imageView.setImageDrawable(resources.getDrawable(R.drawable.shape_red))

        imageView.layoutParams = layoutParams
        imageView.x = startPoint.x - productWidth / 2
        imageView.y = startPoint.y - productHeight / 2
        addView(imageView)
    }

    @SuppressLint("ObjectAnimatorBinding")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun startAnim() {
        val animationSet = AnimatorSet()
        val pathAnim = ObjectAnimator.ofFloat(imageView, "x", "y", movePath)
        pathAnim.duration = 800 * percent
//        pathAnim.startDelay = 100 * percent
        pathAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                removeViewAt(0)
                createNewImage()
            }
        })

        val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0.2f)
        scaleX.duration = 800 * percent
//        scaleX.startDelay = 100 * percent

        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.2f)
        scaleY.duration = 800 * percent
//        scaleY.startDelay = 100 * percent

        val alpha = ObjectAnimator.ofInt(imageView, "imageAlpha", 255, 0)
        alpha.duration = 50 * percent
        alpha.startDelay = 750 * percent

        val shape =
            ObjectAnimator.ofInt(
                imageView,
                "borderRadius",
                0,
                imageView.width / 2
            )
/*        shape.addUpdateListener {
            imageView.background = gradientDrawable
        }*/
        shape.duration = 100

        /*val hypotenuse =
            Math.hypot(imageView.width.toDouble(), imageView.height.toDouble())
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(
                imageView, imageView.width / 2, imageView.height / 2,
                hypotenuse.toFloat(), 6f
            )
        circularReveal.duration = 1000*/

        animationSet.playTogether(shape, pathAnim, scaleX, scaleY, alpha)
        animationSet.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.strokeWidth = 10f
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.FILL
        canvas.drawRect(product, mPaint)
        canvas.drawRect(cart, mPaint)
        mPaint.color = Color.GRAY
        canvas.drawRect(cube, mPaint)
        drawPoint(Color.YELLOW, startPoint, canvas)
        drawPoint(Color.BLACK, endPoint, canvas)
        drawPoint(Color.RED, touchPoint, canvas)
        drawPoint(Color.CYAN, cubePoint, canvas)
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        linePath.reset()
        linePath.moveTo(startPoint.x, startPoint.y)
        linePath.quadTo(touchPoint.x, touchPoint.y, endPoint.x, endPoint.y)
        canvas.drawPath(linePath, mPaint)
        movePath.reset()
        movePath.moveTo(imageView.x, imageView.y)
        movePath.quadTo(
            touchPoint.x - productWidth / 2,
            touchPoint.y - productHeight / 2,
            endPoint.x - productWidth / 2,
            endPoint.y - productHeight / 2
        )
    }

    private fun drawPoint(color: Int, pointF: PointF, canvas: Canvas) {
        pointPaint.color = color
        canvas.drawCircle(pointF.x, pointF.y, 6f, pointPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchPoint.x = event.x
        touchPoint.y = event.y
        postInvalidate()
        return true
    }

    fun setStartX(startX: Float, startY: Float, endX: Float, endY: Float) {
        startPoint.x = startX
        startPoint.y = startY
        endPoint.x = endX
        endPoint.y = endY
        cubePoint.x = startPoint.x
        cubePoint.y = startPoint.y
        product.top = startPoint.y - productHeight / 2
        product.left = startPoint.x - productWidth / 2
        product.bottom = startPoint.y + productHeight / 2
        product.right = startPoint.x + productWidth / 2
        cart.top = endPoint.y - cartHeight / 2
        cart.left = endPoint.x - cartWidth / 2
        cart.bottom = endPoint.y + cartHeight / 2
        cart.right = endPoint.x + cartWidth / 2
        imageView.x = startX - productWidth / 2
        imageView.y = startY - productHeight / 2
        invalidate()
    }
}
