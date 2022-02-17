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
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.wjf.loadLayout.R
import kotlin.math.*

/** Description： @Author：桑小年 @Data：2016/7/20 17:35  */
class BasicPathView2 : LinearLayout {
    private lateinit var pointPaint: Paint
    private lateinit var mPaint: Paint
    private lateinit var linePath: Path
    private lateinit var movePath: Path
    private lateinit var controlPath: Path
    private val startPoint = PointF()
    private val endPoint = PointF()
    private val touchPoint = PointF()
    private val cart = RectF()
    private val product = RectF()
    private val productWidth = 200
    private val productHeight = 200
    private val cartWidth = 100f
    private val cartHeight = 50f
    private val cube = RectF()
    private val cubePoint = PointF()
    private lateinit var imageView: RoundImageView
    private val percent = 2L
    private val layoutParams = LayoutParams(productWidth.toInt(), productHeight.toInt())
    private val params = IntArray(4)

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
        controlPath = Path()
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

        mPaint.color = Color.YELLOW
        controlPath.reset()
        controlPath.moveTo(startPoint.x, startPoint.y)
        controlPath.lineTo(touchPoint.x, touchPoint.y)
        canvas.drawPath(controlPath, mPaint)
    }

    private fun drawPoint(color: Int, pointF: PointF, canvas: Canvas) {
        pointPaint.color = color
        canvas.drawCircle(pointF.x, pointF.y, 6f, pointPaint)
    }

    /**
     * @param startPoint 起始点坐标
     * @param length 要求的点到起始点的直线距离 -- 线长
     * @param angle 与x轴的夹角角度
     * @return
     */
    fun includedAngle(O: PointF, A: PointF, B: PointF): Float {
        // cosAOB=OA*OB向量内积/（|OA|*|OB|模积）
        // OA*OB 向量=(Ax-Ox)(Bx-Ox)+(Ay-Oy)*(By-Oy)
        val vectorProduct = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y)
        val a = getPointBetweenDistance(A, O)

        // OB 的长度
        val b = getPointBetweenDistance(B, O)
        val cosAOB = vectorProduct / (a * b)

        // Math.acos反余弦得到角弧度，Math.toDegrees将弧度转换成角度，此时的角度是不带方向的
        val angleAOB = Math.toDegrees(acos(cosAOB.toDouble())).toFloat()
        return if (angleAOB == 0f) {
            180f
        } else {
            angleAOB
        }
    }

    fun judgeDirection(O: PointF, A: PointF, B: PointF): Int {
        // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值，点b对于AO连线的方向，>0为左
        val direction = if (A.x == B.x || O.x == B.x) {
            0f
        } else {
            (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x)
        }
        return when {
            direction >= 0 -> {
                // 与O点垂直，使其默认向右，180°意为与OA点反向
                // 朝右为正
                1
            }
            direction < 0 -> {
                // 朝左为负
                -1
            }
            else -> {
                1
            }
        }
    }

    /**
     * 根据角度、长度在射线上得到点坐标
     * @param startPoint 起始点坐标
     * @param length 要求的点到起始点的直线距离 -- 线长
     * @param angle 与x轴的夹角角度
     * @return 坐标点
     */
    fun calculatePoint(startPoint: PointF, length: Float, angle: Float): PointF {
        // Math.cos传参是弧度，Math.toRadians用于将角度转换为弧度
        // x差值，a=cosα *c
        val deltaX = (cos(Math.toRadians(angle.toDouble())) * length).toFloat()
        // y差值，b=sinα *c，这里由于坐标系翻转，所以正弦值要偏移π°，sin(x+180)=-sinx
        val deltaY = (sin(Math.toRadians((180 - angle).toDouble())) * length).toFloat()
        return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
    }

    /*    override fun onTouchEvent(event: MotionEvent): Boolean {
            touchPoint.x = event.x
            touchPoint.y = event.y
            postInvalidate()
            return true
        }*/
    private fun setControlPoint() {
        val sideLength = min(productWidth, productHeight)
        // 较短边的一半
        val offset = sideLength / 2f
        val upPoint = PointF(startPoint.x, startPoint.y - offset)
        val ifTop = startPoint.y > endPoint.y
        // 商品与购物车形成的夹角
        val bigAngle = if (ifTop)
        // 转换原点坐标
            includedAngle(upPoint, startPoint, endPoint) else
            includedAngle(startPoint, upPoint, endPoint)
        // 夹角得出比例
        val difAngle = bigAngle / params[1] * params[0]
        // 商品重心与商品上点连线与x轴的夹角
        val xAngle = if (!ifTop) -90f else 90f
        // 与x轴夹角，优化边缘算法
        var direction =
            judgeDirection(startPoint, upPoint, endPoint) * if (ifTop) -1 else 1

        if (sideLength < 200f.dpOfInt) {
            // 排除商详页
            val padding =
                (getPointBetweenDistance(startPoint, endPoint) / 9).dpOfInt
            if (startPoint.x < padding) {
                if ((direction < 0 && !ifTop) || (direction > 0 && ifTop)) {
                    direction *= -1
                }
            } else if (startPoint.x > getWindowWidth() - padding) {
                if ((direction > 0 && !ifTop) || (direction < 0 && ifTop)) {
                    direction *= -1
                }
            }
        }
        val delta = xAngle + difAngle * direction
        val length = getPointBetweenDistance(startPoint, endPoint) / params[3] * params[2]
        val pointF = calculatePoint(startPoint, length, delta)
//        val pointF = if (ifTop) calculatePoint(controlPoint1, length, delta) else calculatePoint(startPoint, length, delta)
        touchPoint.x = pointF.x
        touchPoint.y = pointF.y
    }

    /**
     * 求两点之间的距离
     */
    fun getPointBetweenDistance(A: PointF, B: PointF): Float {
        return sqrt((A.x - B.x).toDouble().pow(2) + (A.y - B.y).toDouble().pow(2)).toFloat()
    }

    fun getWindowWidth(): Int {
        return context.resources.displayMetrics.widthPixels
    }

    val Float.dpOfInt
        get() = dip2px(context, this)

    fun dip2px(context: Context?, dpValue: Float): Int {
        val scale: Float = context?.resources?.displayMetrics?.density ?: 0f
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 求view的重心坐标
     */
    fun getViewGravityPoint(view: View): PointF {
        val result = PointF()
        val offset = IntArray(2)
        view.getLocationInWindow(offset)
        result.x = offset[0] + view.width / 2.0f
        result.y = offset[1] + view.height / 2.0f
        return result
    }

    fun setStartX(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        param1: Int,
        param2: Int,
        param3: Int,
        param4: Int
    ) {
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
        params[0] = param1
        params[1] = param2
        params[2] = param3
        params[3] = param4
        setControlPoint()
        invalidate()
    }
}
