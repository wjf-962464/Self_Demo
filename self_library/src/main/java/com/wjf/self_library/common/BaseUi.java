package com.wjf.self_library.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class BaseUi<T extends ViewDataBinding> extends ConstraintLayout {
    protected T view;
    protected Context context;

    protected BaseUi(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = DataBindingUtil.inflate(inflater, setLayout(), this, true);
        getTypeArray(context.obtainStyledAttributes(attrs, setStyleable()));
        setView();
    }

    /**
     * 设置布局
     *
     * @return 布局资源id
     */
    protected abstract int setLayout();

    /**
     * 设置样式
     *
     * @return 返回样式资源id
     */
    protected abstract int[] setStyleable();

    /**
     * 获取样式类型
     *
     * @param typedArray 样式数组
     */
    protected abstract void getTypeArray(TypedArray typedArray);

    /**
     * 设置View
     */
    protected abstract void setView();
}
