package com.wjf.self_library.view.codingUI;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;


import androidx.core.content.ContextCompat;

import com.wjf.self_library.R;
import com.wjf.self_library.common.BaseUi;
import com.wjf.self_library.databinding.UiCommonToolbarBinding;


/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public class CommonToolbar extends BaseUi<UiCommonToolbarBinding> {

    private String titleText, backText;
    private boolean darkTheme, ifBack;
    private int backgroundColor, backgroundChangeColor;

    public CommonToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int setLayout() {
        return R.layout.ui_common_toolbar;
    }

    @Override
    protected int[] setStyleable() {
        return R.styleable.CommonToolbar;
    }

    @Override
    protected void getTypeArray(TypedArray typedArray) {
        titleText = typedArray.getString(R.styleable.CommonToolbar_titleText);
        backText = typedArray.getString(R.styleable.CommonToolbar_backText);
        darkTheme = typedArray.getBoolean(R.styleable.CommonToolbar_dark_theme, true);
        ifBack = typedArray.getBoolean(R.styleable.CommonToolbar_if_back, true);
        backgroundColor = typedArray.getColor(R.styleable.CommonToolbar_background_color, Color.TRANSPARENT);
        backgroundChangeColor = typedArray.getColor(R.styleable.CommonToolbar_background_change_color, Color.TRANSPARENT);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getBackgroundChangeColor() {
        return backgroundChangeColor;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        if (titleText != null) {
            view.toolbarTitle.setVisibility(VISIBLE);
            view.toolbarTitle.setText(titleText);
        } else {
            view.toolbarTitle.setVisibility(INVISIBLE);
        }
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    @Override
    protected void setView() {
        int themeColor = darkTheme ? ContextCompat.getColor(context, R.color.black) : ContextCompat.getColor(context, R.color.white);

        if (ifBack) {
            view.backLine.setVisibility(VISIBLE);
            if (backText != null) {
                view.toolbarBack.setText(backText);
                view.toolbarBack.setVisibility(VISIBLE);
                view.toolbarBack.setTextColor(themeColor);
            }
            view.toolbarBackIcon.setVisibility(VISIBLE);
            view.toolbarBackIcon.changeColor(themeColor);
            view.backLine.setOnClickListener(v -> {
                if (toolbarBackCallback != null) {
                    toolbarBackCallback.backActivity(context);
                }
                ((Activity) context).finish();
            });
        }

        if (titleText != null) {
            view.toolbarTitle.setText(titleText);
            view.toolbarTitle.setVisibility(VISIBLE);
            view.toolbarTitle.setTextColor(themeColor);
        }

        setBackgroundColor(backgroundColor);
    }


    private ToolbarBackCallback toolbarBackCallback;


    public void setToolbarBackCallback(ToolbarBackCallback toolbarBackCallback) {
        this.toolbarBackCallback = toolbarBackCallback;
    }


    public interface ToolbarBackCallback {
        /**
         * 返回事件回调
         *
         * @param context 上下文对象
         */
        void backActivity(Context context);
    }
}
