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
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.orhanobut.logger.Logger
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
    private val productWidth = 200f
    private val productHeight = 200f
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
    private fun calculatePoint(startPoint: PointF, length: Float, angle: Float): PointF {
        // Math.cos传参是弧度，Math.toRadians用于将角度转换为弧度
        // x差值，a=cosα *c
        val deltaX = (cos(Math.toRadians(angle.toDouble())) * length).toFloat()
        // y差值，b=sinα *c，这里由于坐标系翻转，所以正弦值要偏移π°，sin(x+180)=-sinx
        val deltaY = (sin(Math.toRadians((180 - angle).toDouble())) * length).toFloat()
        return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
    }

    private fun includeAngle(O: PointF, A: PointF, B: PointF): Float {
        // cosAOB=OA*OB向量内积/（|OA|*|OB|模积）
        // OA*OB 向量=(Ax-Ox)(Bx-Ox)+(Ay-Oy)*(By-Oy)
        val AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y)
        val OALength = getPointBetweenDistance(A, O)

        // OB 的长度
        val OBLength = getPointBetweenDistance(B, O)
        val cosAOB = AOB / (OALength * OBLength)

        // Math.acos反余弦得到角弧度，Math.toDegrees将弧度转换成角度，此时的角度是不带方向的
        val angleAOB = Math.toDegrees(acos(cosAOB.toDouble())).toFloat()

        // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值，点b对于AO连线的方向，>0为左
        val direction = if (A.x == B.x || O.x == B.x) {
            0f
        } else {
            (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x)
        }
        val result = when {
            direction == 0f -> {
                /*            if (AOB >= 0) {
                                180f
                            } else {
                                0f
                            }*/
                -180f
            }
            direction > 0 -> {
                -angleAOB
            }
            else -> {
                if (angleAOB == 0f) {
                    return 180f
                }
                angleAOB
            }
        }
        Logger.d(
            "angleAOB:$angleAOB；direction：$direction;result:$result;"
        )
        return result
    }

    private fun getPointBetweenDistance(A: PointF, B: PointF): Float {
        val x2 = (A.x - B.x).toDouble().pow(2)
        val y2 = (A.y - B.y).toDouble().pow(2)
        return sqrt(x2 + y2).toFloat()
    }

    /*    override fun onTouchEvent(event: MotionEvent): Boolean {
            touchPoint.x = event.x
            touchPoint.y = event.y
            postInvalidate()
            return true
        }*/
    private fun setControlPoint() {
        val controlPoint1 = PointF(startPoint.x, startPoint.y - productHeight / 2)
        // 大角一半
//        val angle = includeAngle(startPoint, endPoint, controlPoint1) / 4
        Logger.d("-----angle-----")
        val bigAngle = includeAngle(startPoint, controlPoint1, endPoint)
        val angle = bigAngle / params[1] * params[0]
        val xPoint = PointF(startPoint.x + 1, startPoint.y)
/*        val xAngle = includeAngle(
            startPoint,
            xPoint,
            controlPoint1
        )*/
        Logger.d("-----xAngle-----")
        val xAngle = includeAngle(
            startPoint,
            controlPoint1,
            xPoint
        )
        // 切角
        val delta = (xAngle - angle)
        // 控制点2 的坐标
        Logger.d(
            "现在:$delta；angle：$angle;xagle:$xAngle;大角：$bigAngle"
        )
        val length = getPointBetweenDistance(startPoint, endPoint) / params[3] * params[2]
        val pointF = calculatePoint(startPoint, length, delta)
        touchPoint.x = pointF.x
        touchPoint.y = pointF.y
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
