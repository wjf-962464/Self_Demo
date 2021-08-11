package com.wjf.self_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class MyIcon extends View {
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

    public MyIcon(Context context) {
        super(context);
    }

    public MyIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getTypeArray(context.obtainStyledAttributes(attrs, setStyleable()));
    }

    public MyIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置样式
     *
     * @return 返回样式资源id
     */
    protected int[] setStyleable() {
        return com.wjf.self_library.R.styleable.Icon;
    }

    /**
     * 获取样式类型
     *
     * @param typedArray 样式数组
     */
    protected void getTypeArray(TypedArray typedArray) {
        iconSize =
                typedArray.getDimension(
                        com.wjf.self_library.R.styleable.Icon_icon_size,
                        (int)
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        20,
                                        getResources().getDisplayMetrics()));

        textSize =
                typedArray.getDimension(
                        com.wjf.self_library.R.styleable.Icon_text_size,
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_SP,
                                13,
                                getResources().getDisplayMetrics()));

        text = typedArray.getString(com.wjf.self_library.R.styleable.Icon_icon_text);
        iconSrc =
                Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(
                                getResources(),
                                typedArray.getResourceId(
                                        com.wjf.self_library.R.styleable.Icon_icon_src,
                                        com.wjf.self_library.R.drawable.ic_launcher)),
                        (int) iconSize,
                        (int) iconSize,
                        false);

        normalColor =
                typedArray.getColor(
                        com.wjf.self_library.R.styleable.Icon_color_normal, Color.BLACK);
        selectedColor =
                typedArray.getColor(
                        com.wjf.self_library.R.styleable.Icon_color_selected, normalColor);

        iconTextMargin =
                typedArray.getDimension(
                        com.wjf.self_library.R.styleable.Icon_icon_text_margin,
                        TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                3,
                                getResources().getDisplayMetrics()));
        iconPosition = typedArray.getInt(com.wjf.self_library.R.styleable.Icon_icon_position, 0);
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
