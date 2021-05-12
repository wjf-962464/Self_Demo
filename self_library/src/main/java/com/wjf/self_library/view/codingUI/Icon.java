package com.wjf.self_library.view.codingUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.wjf.self_library.R;

/**
 * @author : Wangjf
 * @date : 2021/5/11
 */
public class Icon extends View {
    private String text;
    private int iconPosition;
    private Bitmap iconSrc;
    private float iconSize, textSize, iconTextMargin;
    private int normalColor, selectedColor;

    /** 自定义View的宽 */
    private int mWidth;
    /** 自定义View的高 */
    private int mHeight;

    private Paint mPaint;

    private Rect mTextBound;
    /** 需要绘制的整个矩形范围 */
    private Rect rect;

    public Icon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getTypeArray(context.obtainStyledAttributes(attrs, setStyleable()));
    }

    /**
     * 设置样式
     *
     * @return 返回样式资源id
     */
    protected int[] setStyleable() {
        return R.styleable.Icon;
    }

    /**
     * 获取样式类型
     *
     * @param typedArray 样式数组
     */
    protected void getTypeArray(TypedArray typedArray) {
        text = typedArray.getString(R.styleable.Icon_icon_text);
        iconSrc =
                BitmapFactory.decodeResource(
                        getResources(),
                        typedArray.getResourceId(
                                R.styleable.Icon_icon_src, R.drawable.ic_launcher));

        normalColor = typedArray.getColor(R.styleable.Icon_color_normal, Color.BLACK);
        selectedColor = typedArray.getColor(R.styleable.Icon_color_selected, normalColor);

        iconSize =
                typedArray.getDimension(
                        R.styleable.Icon_icon_size,
                        (int)
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        20,
                                        getResources().getDisplayMetrics()));
        textSize =
                typedArray.getDimension(
                        R.styleable.Icon_text_size,
                        (int)
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_SP,
                                        13,
                                        getResources().getDisplayMetrics()));

        iconTextMargin =
                typedArray.getDimension(
                        R.styleable.Icon_icon_text_margin,
                        (int)
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        3,
                                        getResources().getDisplayMetrics()));
        iconPosition = typedArray.getInt(R.styleable.Icon_icon_position, 0);
        typedArray.recycle();

        Log.d(
                "WJF_DEBUG",
                "iconPosition:"
                        + iconPosition
                        + "\ntextSize:"
                        + textSize
                        + "\niconSize:"
                        + iconSize
                        + "\niconTextMargin:"
                        + iconTextMargin);
        rect = new Rect();

        mPaint = new Paint();
        mTextBound = new Rect();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPaint.setTextSize(textSize);
        // 计算描绘字体需要的范围
        mPaint.getTextBounds(text, 0, text.length(), mTextBound);

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else {
            // 自定义View的宽度，由左右填充和图片宽度决定
            int desireByImg = getPaddingLeft() + getPaddingRight() + iconSrc.getWidth();
            // 由左右填充和字体绘制范围的宽度决定
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextBound.width();

            if (specMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.min(desire, specSize);
            }
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            // 设置了明确的值或者是MATCH_PARENT
            mHeight = specSize;
        } else {
            // 由上下填充、图片的高度和字体绘制范围的高度决定
            int desire =
                    getPaddingTop()
                            + getPaddingBottom()
                            + iconSrc.getHeight()
                            + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST) {
                // wrap_content
                mHeight = Math.min(desire, specSize);
            }
        }
        // 为控件指定大小
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(normalColor);

        rect.left = (int) ((getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - iconSize / 2);
        rect.right = (int) (rect.left + iconSize);
        rect.top = getPaddingTop();
        rect.bottom = (int) (rect.top + iconSize);

        mPaint.setColor(normalColor);

        /** 当前设置的宽度小于字体需要的宽度，将字体改为xxx... */
        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg =
                    TextUtils.ellipsize(
                                    text,
                                    paint,
                                    (float) mWidth - getPaddingLeft() - getPaddingRight(),
                                    TextUtils.TruncateAt.END)
                            .toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);

        } else {
            // 正常情况，将字体居中
            canvas.drawText(
                    text,
                    mWidth / 2 - mTextBound.width() * 1.0f / 2,
                    mHeight - getPaddingBottom(),
                    mPaint);
        }

        // 取消使用掉的块
        rect.bottom -= mTextBound.height();
        canvas.drawBitmap(iconSrc, null, rect, mPaint);
        /*        if (mImageScale == IMAGE_SCALE_FITXY) {
            // 绘制image
            canvas.drawBitmap(mImage, null, rect, mPaint);
        } else {
            // 计算居中的矩形范围
            rect.left = mWidth / 2 - mImage.getWidth() / 2;
            rect.right = mWidth / 2 + mImage.getWidth() / 2;
            rect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            rect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, rect, mPaint);
        }*/
    }
}
