package com.wjf.self_library.view.codingUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
    private Drawable normal_src, selected_src;
    private float icon_size, text_size, icon_text_margin;
    private int text_color_normal, text_color_select;

    public MyIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected int setLayout() {
        return R.layout.ui_icon;
    }

    @Override
    protected int[] setStyleable() {
        return R.styleable.MyIcon;
    }

    @Override
    protected void getTypeArray(TypedArray typedArray) {
        text = typedArray.getString(R.styleable.MyIcon_icon_text);
        normal_src = typedArray.getDrawable(R.styleable.MyIcon_icon_normal);
        selected_src = typedArray.getDrawable(R.styleable.MyIcon_icon_selected);
        icon_size = typedArray.getDimension(R.styleable.MyIcon_icon_size, 60);//默认20dp，所有默认值都对于480dpi，即像素密度比为1:3
        text_size = typedArray.getDimension(R.styleable.MyIcon_text_size, 39);//默认13sp
        icon_text_margin = typedArray.getDimension(R.styleable.MyIcon_icon_text_margin, 3);//默认1dp
        text_color_normal = typedArray.getColor(R.styleable.MyIcon_text_color, getResources().getColor(R.color.text_black));
        text_color_select = typedArray.getColor(R.styleable.MyIcon_text_color_selected, getResources().getColor(R.color.white));
    }

    public void setText(String text) {
        this.text = text;
        view.iconText.setText(this.text);
    }


    @Override
    protected void setView() {
        changeToNormal();
        view.iconImg.getLayoutParams().height = (int) icon_size;
        view.iconImg.getLayoutParams().width = (int) icon_size;
        view.iconText.setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, (int) icon_text_margin, 0, 0);
        view.iconText.setLayoutParams(lp);
        view.iconText.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
    }

    public void changeToSelected() {
        if (selected_src == null) {
            return;
        }
        view.iconImg.setImageDrawable(selected_src);
        view.iconText.setTextColor(text_color_select);
    }

    public void changeToNormal() {
        view.iconImg.setImageDrawable(normal_src);
        view.iconText.setTextColor(text_color_normal);
    }
}
