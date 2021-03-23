package com.wjf.self_library.view.codingUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;

import com.wjf.self_library.R;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public class ColorImage extends androidx.appcompat.widget.AppCompatImageView {

    public ColorImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        getTypeArray(context.obtainStyledAttributes(attrs, R.styleable.ColorImage));
    }

    private void getTypeArray(TypedArray typedArray) {
        int color = typedArray.getColor(R.styleable.ColorImage_img_color, Color.BLACK);
        setColorFilter(color);
    }

    public void changeColor(@ColorRes int color) {
        setColorFilter(color);
    }
}
