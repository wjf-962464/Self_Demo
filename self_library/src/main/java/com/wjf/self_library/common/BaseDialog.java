package com.wjf.self_library.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.wjf.self_library.R;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class BaseDialog extends Dialog {
    protected final Context context;
    private final Builder builder;

    public BaseDialog(@NonNull Builder builder) {
        super(builder.context, R.style.FullScreenDialogStyle);
        this.context = builder.context;
        this.builder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        // 设置对话框周围的颜色透明度
        if (window != null) {
            // 去除黑色阴影
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setGravity(builder.gravity);
            // 设置弹出及回收动画
            window.setWindowAnimations(builder.windowAnimations);
            window.setDimAmount(0.4f);
        }
        this.setCanceledOnTouchOutside(false);
        View view = View.inflate(context, setLayout(), null);
        this.setContentView(view);
        initView(view);
    }

    /**
     * 初始化视图
     *
     * @param view 视图
     */
    protected abstract void initView(View view);

    /**
     * 指定布局文件
     *
     * @return R.layout.xxx
     */
    protected abstract int setLayout();

    @Override
    public void show() {
        super.show();
        // 此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        initDialog();
    }

    protected void initDialog() {
        // 获取窗口
        Window window = getWindow();
        if (window == null) {
            return;
        }
        // 获取配置参数
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 设置为屏幕正中
        layoutParams.gravity = builder.gravity;
        // 宽高包裹布局自身
        layoutParams.width = builder.width;
        layoutParams.height = builder.height;
        // 消除dialog自身隐藏padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(layoutParams);
    }

    public interface DialogConfirmListener<T> {
        /**
         * 对话框确认
         *
         * @param value 新的值
         */
        void confirm(T value);
    }

    public abstract static class Builder {
        public final Context context;
        private int gravity = Gravity.BOTTOM;
        @StyleRes
        private int windowAnimations = R.style.BottomDialog_Animation;
        private int width = WindowManager.LayoutParams.MATCH_PARENT;
        private int height = WindowManager.LayoutParams.WRAP_CONTENT;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder windowAnimations(@StyleRes int windowAnimations) {
            this.windowAnimations = windowAnimations;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * 构造
         *
         * @return BaseDialog
         */
        public abstract BaseDialog build();
    }
}
