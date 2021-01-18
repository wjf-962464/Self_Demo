package com.wjf.self_library;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


import com.wjf.self_library.util.ActivityCollector;
import com.wjf.self_library.util.StatusBar.StatusBarUtil;
import com.wjf.self_library.view.codingUI.CommonToolbar;
import com.wjf.self_library.view.swipebacklayout.SwipeBackLayout;
import com.wjf.self_library.view.swipebacklayout.app.SwipeBackActivity;


/**
 * @author Wangjf2-DESKTOP
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends SwipeBackActivity {
    
    protected CommonToolbar commonToolbar;
    protected T view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        view = DataBindingUtil.setContentView(this, setLayout());
        getSupportActionBar().hide();//隐藏actionBar
        initSwipeActivity();//初始化右滑返回
        initColorStatusBar();
        initView();
        initData();

    }


    private void initSwipeActivity() {
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    protected void initColorStatusBar() {
        commonToolbar = findViewById(R.id.commonToolbar);
        if (commonToolbar != null) {
            if (commonToolbar.getBackgroundColor() == 0)//透明，默认同样透明主题
            {
                StatusBarUtil.setTranslucentStatus(this, commonToolbar.isDarkTheme());
            } else {
                StatusBarUtil.setStatusBarColor(this, commonToolbar.getBackgroundColor(), commonToolbar.isDarkTheme());
            }
        } else {
            StatusBarUtil.setTranslucentStatus(this, false);
        }
    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    public abstract int setLayout();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
