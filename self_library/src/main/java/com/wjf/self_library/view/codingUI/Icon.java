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

    private Paint mPaint;

    private Rect mTextBound;
    private TextPaint textPaint;
    private IconPosition distribution = null;
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
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_SP,
                                13,
                                getResources().getDisplayMetrics()));

        text = typedArray.getString(R.styleable.Icon_icon_text);
        iconSrc =
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(
                                getResources(),
                                typedArray.getResourceId(
                                        R.styleable.Icon_icon_src, R.drawable.ic_launcher)),
                        (int) iconSize,
                        (int) iconSize,
                        false);


        normalColor = typedArray.getColor(R.styleable.Icon_color_normal, Color.BLACK);
        selectedColor = typedArray.getColor(R.styleable.Icon_color_selected, normalColor);

        iconTextMargin =
                typedArray.getDimension(
                        R.styleable.Icon_icon_text_margin,
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                3,
                                getResources().getDisplayMetrics()));
        iconPosition = typedArray.getInt(R.styleable.Icon_icon_position, 0);
        try {
            distribution = IconPosition.create(iconPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (distribution == null) {
            distribution = IconPosition.ICON_TOP;
        }
        typedArray.recycle();
        Log.d(
                "WJF_DEBUG",
                "iconPosition:"
                        + distribution.toString()
                        + "\ntextSize:"
                        + textSize
                        + "\niconSize:"
                        + iconSize
                        + "\niconTextMargin:"
                        + iconTextMargin);

        rect = new Rect();

        mPaint = new Paint();
        textPaint = new TextPaint(mPaint);
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

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

        int needWith, needHeight;

        // 根据摆放方位，计算对应的长宽
        switch (distribution) {
            case ICON_TOP:
            case ICON_BOTTOM:
                needWith =
                        Math.max(iconSrc.getWidth(), mTextBound.width())
                                + getPaddingLeft()
                                + getPaddingRight();
                needHeight =
                        iconSrc.getHeight()
                                + mTextBound.height()
                                + getPaddingTop()
                                + getPaddingBottom();
                break;
            case ICON_LEFT:
            case ICON_RIGHT:
                needWith =
                        iconSrc.getWidth()
                                + mTextBound.width()
                                + +getPaddingLeft()
                                + getPaddingRight();
                needHeight =
                        Math.max(iconSrc.getHeight(), mTextBound.height())
                                + getPaddingTop()
                                + getPaddingBottom();
                break;
            default:
                needWith = needHeight = 0;
                break;
        }

        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int realWidth = widMode == MeasureSpec.EXACTLY ? selfWidth : needWith;
        int realHeight = heightMode == MeasureSpec.EXACTLY ? selfHeight : needHeight;
        setMeasuredDimension(realWidth, realHeight);
        if (mTextBound.width() > realWidth) {
            text =
                    TextUtils.ellipsize(
                                    text,
                                    textPaint,
                                    (float) realWidth - getPaddingLeft() - getPaddingRight(),
                                    TextUtils.TruncateAt.END)
                            .toString();
            mPaint.getTextBounds(text, 0, text.length(), mTextBound);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(normalColor);
        int mWidth = getWidth();
        int mHeight = getHeight();

        mPaint.setColor(normalColor);

        switch (distribution) {
            case ICON_TOP:
                rect.left = (int) (mWidth * 1.0f / 2 - iconSize / 2);
                rect.right = (int) (rect.left + iconSize);
                rect.top = getPaddingTop();
                rect.bottom = (int) (rect.top + iconSize);
                canvas.drawText(
                        text,
                        mWidth * 1.0f / 2 - mTextBound.width() * 1.0f / 2,
                        mHeight - getPaddingBottom()-mTextBound.height()*1.0f/2,
                        mPaint);

                break;
            case ICON_BOTTOM:
                rect.left = (int) (mWidth * 1.0f / 2 - iconSize / 2);
                rect.right = (int) (rect.left + iconSize);
                rect.top = getPaddingTop() + mTextBound.height();
                rect.bottom = (int) (rect.top + iconSize);
                canvas.drawText(
                        text,
                        mWidth * 1.0f / 2 - mTextBound.width() * 1.0f / 2,
                        getPaddingTop()+mTextBound.height(),
                        mPaint);
                break;
            case ICON_LEFT:
                rect.top = (int) (mHeight * 1.0f / 2 - iconSize / 2);
                rect.bottom = (int) (rect.top + iconSize);
                rect.left = getPaddingLeft();
                rect.right = (int) (rect.left + iconSize);
                canvas.drawText(text, mWidth - getPaddingRight()-mTextBound.width(), mHeight * 1.0f / 2+mTextBound.height()*1.0f/2, mPaint);
                break;
            case ICON_RIGHT:
                rect.top = (int) (mHeight * 1.0f / 2 - iconSize / 2);
                rect.bottom = (int) (rect.top + iconSize);
                rect.right = mWidth-getPaddingRight();
                rect.left = (int) (rect.right - iconSize);
                canvas.drawText(text, getPaddingLeft(), mHeight * 1.0f / 2+mTextBound.height()*1.0f/2, mPaint);

                break;
            default:
                break;
        }
        canvas.drawBitmap(iconSrc, null, rect, mPaint);
    }

    enum IconPosition {
        /** 上图下文 */
        ICON_TOP(0),
        ICON_RIGHT(1),
        ICON_BOTTOM(2),
        ICON_LEFT(3);
        private final int position;

        IconPosition(int i) {
            this.position = i;
        }

        public static IconPosition create(int position) throws Exception {
            for (IconPosition type : IconPosition.values()) {
                if (type.position() == position) {
                    return type;
                }
            }
            throw new Exception("IconPosition 无法转换为对应的枚举类");
        }

        public int position() {
            return position;
        }
    }
}
