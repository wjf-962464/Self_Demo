package com.wjf.self_demo.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : Wangjf
 * @date : 2021/4/2
 */
public class FishDrawable extends Drawable {
    /** 单位长度R */
    public static final int R = 50;
    /** 其他部分透明度 */
    private final int OTHER_ALPHA = 110;
    /** 身体部分透明度 */
    private final int BODY_ALPHA = 160;
    /** 鱼身长度 */
    private final float BODY_LENGTH = 3.2f * R;
    /** 寻找鱼鳍起点的线长 */
    private final float FIND_FINS_LENGTH = 0.9f * R;
    /** 鱼鳍长度 */
    private final float FINS_LENGTH = 1.3f * R;
    /** 尾部大圆半径 */
    private final float BIG_CIRCLE_RADIUS = 0.7f * R;
    /** 尾部中圆半径 */
    private final float MIDDLE_CIRCLE_RADIUS = BIG_CIRCLE_RADIUS * 0.6f;
    /** 尾部小圆半径 */
    private final float SMALL_CIRCLE_RADIUS = MIDDLE_CIRCLE_RADIUS * 0.4f;
    /** 寻找尾部中圆圆心的线长 */
    private final float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS + MIDDLE_CIRCLE_RADIUS;
    /** 寻找尾部小圆圆心的线长 */
    private final float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    /** 寻找大三角形底边中心店的线长 */
    private final float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f;

    private Path mPath;
    private Paint mPaint;
    /** 中心点坐标 */
    private PointF middlePoint;
    /** 朝向角度 */
    private final float fishMainAngle = 90;

    private float currentValue = 0;

    public FishDrawable() {
        init();
    }

    private PointF calculatePoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) (Math.cos(Math.toRadians(angle)) * length);
        float deltaY = (float) (Math.sin(Math.toRadians(angle - 180)) * length);
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();

        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);

        middlePoint = new PointF(4.18f * R, 4.18f * R);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(1000L);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(
                animation -> {
                    currentValue = (float) animation.getAnimatedValue();
                    invalidateSelf();
                });
        valueAnimator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(currentValue)) * 10);

        // 鱼头圆形
        PointF headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headPoint.x, headPoint.y, R, mPaint);
        // 右鳍
        PointF rightFinPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110);
        makeFins(canvas, rightFinPoint, fishAngle, true);

        PointF leftFinPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110);
        makeFins(canvas, leftFinPoint, fishAngle, false);
        // 身体底部的中心店
        PointF bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180);
        // 画节肢1
        PointF middleCircleCenterPoint =
                makeSegment(
                        canvas,
                        bodyBottomCenterPoint,
                        BIG_CIRCLE_RADIUS,
                        MIDDLE_CIRCLE_RADIUS,
                        FIND_MIDDLE_CIRCLE_LENGTH,
                        fishAngle,
                        true);
        // 画节肢2
        //        PointF middleCircleCenterPoint =
        //                calculatePoint(bodyBottomCenterPoint, FIND_MIDDLE_CIRCLE_LENGTH, fishAngle
        // - 180);
        makeSegment(
                canvas,
                middleCircleCenterPoint,
                MIDDLE_CIRCLE_RADIUS,
                SMALL_CIRCLE_RADIUS,
                FIND_SMALL_CIRCLE_LENGTH,
                fishAngle,
                false);

        // 画尾巴
        makeTriangle(
                canvas,
                middleCircleCenterPoint,
                FIND_TRIANGLE_LENGTH,
                BIG_CIRCLE_RADIUS,
                fishAngle);
        makeTriangle(
                canvas,
                middleCircleCenterPoint,
                FIND_TRIANGLE_LENGTH,
                BIG_CIRCLE_RADIUS - 20,
                fishAngle);
        // 画身体
        makeBody(canvas, headPoint, bodyBottomCenterPoint, fishAngle);
    }

    private PointF makeSegment(
            Canvas canvas,
            PointF bottomCenterPoint,
            float bigRadius,
            float smallRadius,
            float findSmallCircleLength,
            float fishAngle,
            boolean hasBigCircle) {
        int swing = hasBigCircle ? 30 : 50;
        fishAngle = (float) (fishAngle + Math.sin(Math.toRadians(currentValue)) * swing);
        PointF upperCenterPoint =
                calculatePoint(bottomCenterPoint, findSmallCircleLength, fishAngle - 180);
        PointF blPoint = calculatePoint(bottomCenterPoint, bigRadius, fishAngle + 90);
        PointF brPoint = calculatePoint(bottomCenterPoint, bigRadius, fishAngle - 90);

        PointF tlPoint = calculatePoint(upperCenterPoint, smallRadius, fishAngle + 90);
        PointF trPoint = calculatePoint(upperCenterPoint, smallRadius, fishAngle - 90);
        if (hasBigCircle) {
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigRadius, mPaint);
        }
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, smallRadius, mPaint);
        mPath.reset();
        mPath.moveTo(blPoint.x, blPoint.y);
        mPath.lineTo(tlPoint.x, tlPoint.y);
        mPath.lineTo(trPoint.x, trPoint.y);
        mPath.lineTo(brPoint.x, brPoint.y);
        canvas.drawPath(mPath, mPaint);
        return upperCenterPoint;
    }

    private void makeBody(
            Canvas canvas, PointF headPoint, PointF bodyBottomCenterPoint, float fishAngle) {
        PointF tlPoint = calculatePoint(headPoint, R, fishAngle + 90);
        PointF trPoint = calculatePoint(headPoint, R, fishAngle - 90);
        PointF blPoint = calculatePoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle + 90);
        PointF brPoint = calculatePoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle - 90);
        PointF controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130);
        mPath.reset();
        mPath.moveTo(tlPoint.x, tlPoint.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, blPoint.x, blPoint.y);
        mPath.lineTo(brPoint.x, brPoint.y);
        mPath.quadTo(controlRight.x, controlRight.y, trPoint.x, trPoint.y);
        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private void makeTriangle(
            Canvas canvas,
            PointF startPoint,
            float findCenterLength,
            float findEdgeLength,
            float fishAngle) {
        findEdgeLength = (float) (findEdgeLength * Math.sin(Math.toRadians(currentValue)));
        Log.d("WJF_DEBUG", "length>>>" + findEdgeLength);
        PointF centerPoint = calculatePoint(startPoint, findCenterLength, fishAngle - 180);
        PointF leftPoint = calculatePoint(centerPoint, findEdgeLength, fishAngle + 90);
        PointF rightPoint = calculatePoint(centerPoint, findEdgeLength, fishAngle - 90);
        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    private void makeFins(Canvas canvas, PointF startPoint, float fishAngle, boolean isRight) {
        int flag = isRight ? -1 : 1;
        PointF controlPoint =
                calculatePoint(startPoint, 1.8f * FINS_LENGTH, fishAngle + flag * 115);
        PointF endPoint = calculatePoint(startPoint, FINS_LENGTH, fishAngle + flag * 180);

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * R);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * R);
    }
}
