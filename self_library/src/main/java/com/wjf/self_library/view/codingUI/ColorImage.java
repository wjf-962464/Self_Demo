package com.wjf.self_library.view.codingUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wjf.self_library.R;

/**
 * @author Wangjf2-DESKTOP
 */
public class ColorImage extends androidx.appcompat.widget.AppCompatImageView {
    private int imgColor;

    public ColorImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        getTypeArray(context.obtainStyledAttributes(attrs, R.styleable.ColorImage));
    }


    private void getTypeArray(TypedArray typedArray) {
        imgColor = typedArray.getColor(R.styleable.ColorImage_img_color,Color.BLACK);
        setColorFilter(imgColor);
    }

    public void setImgColor(int imgColor) {
        this.imgColor = imgColor;
        setColorFilter(imgColor);
    }
}
