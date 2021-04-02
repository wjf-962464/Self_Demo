package com.wjf.self_demo.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author : Wangjf
 * @date : 2021/4/2
 */
public class FishDrawable extends Drawable {
    public final static int HEAD_RADIUS = 50;
    private final int OTHER_ALPHA = 110;
    private final int BODY_ALPHA = 160;
    private final float body_length = 3.2f * HEAD_RADIUS;
    private Path mPath;
    private Paint mPaint;
    private PointF middlePoint;
    private float fishMainAngle = 0;

    public FishDrawable() {
        init();
    }

    private PointF calculatePoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) Math.cos(Math.cos(Math.toRadians(angle)) * length);
        float deltaY = (float) Math.cos(Math.sin(Math.toRadians(angle - 180)) * length);
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

        middlePoint = new PointF(4.18f * HEAD_RADIUS, 4.18f * HEAD_RADIUS);

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float fishAngle = fishMainAngle;

        PointF headPoint = calculatePoint(middlePoint, body_length / 2, fishAngle);
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);
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
        return (int) (8.38f * HEAD_RADIUS);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);
    }
}
