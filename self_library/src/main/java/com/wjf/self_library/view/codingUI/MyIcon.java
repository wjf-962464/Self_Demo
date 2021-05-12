package com.wjf.self_library.view.codingUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.wjf.self_library.R;
import com.wjf.self_library.common.BaseUi;
import com.wjf.self_library.databinding.UiIconBinding;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public class MyIcon extends BaseUi<UiIconBinding> {

    private String text;
    private int iconSrc;
    private float iconSize, textSize, iconTextMargin;
    private int normalColor, selectedColor;

    public MyIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int setLayout() {
        return R.layout.ui_icon;
    }

    @Override
    protected int[] setStyleable() {
        return R.styleable.Icon;
    }

    @Override
    protected void getTypeArray(TypedArray typedArray) {
        text = typedArray.getString(R.styleable.Icon_icon_text);
        iconSrc = typedArray.getResourceId(R.styleable.Icon_icon_src, R.drawable.ic_launcher);
        normalColor = typedArray.getColor(R.styleable.Icon_color_normal, Color.BLACK);
        selectedColor = typedArray.getColor(R.styleable.Icon_color_selected, normalColor);
        // 默认20dp，所有默认值都对于480dpi，即像素密度比为1:3
        iconSize = typedArray.getDimension(R.styleable.Icon_icon_size, 60);
        // 默认13sp
        textSize = typedArray.getDimension(R.styleable.Icon_text_size, 39);
        // 默认1dp
        iconTextMargin = typedArray.getDimension(R.styleable.Icon_icon_text_margin, 3);
    }

    public void setText(String text) {
        this.text = text;
        view.iconText.setText(this.text);
    }

    @Override
    protected void setView() {
        changeToNormal();
        view.iconImg.getLayoutParams().height = (int) iconSize;
        view.iconImg.getLayoutParams().width = (int) iconSize;
        view.iconImg.setImageResource(iconSrc);

        view.iconText.setText(text);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, (int) iconTextMargin, 0, 0);
        view.iconImg.setLayoutParams(lp);
        view.iconText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void changeToSelected() {
        view.iconImg.setColorFilter(selectedColor);
        view.iconText.setTextColor(selectedColor);
    }

    public void changeToNormal() {
        view.iconImg.setColorFilter(normalColor);
        view.iconText.setTextColor(normalColor);
    }
}
