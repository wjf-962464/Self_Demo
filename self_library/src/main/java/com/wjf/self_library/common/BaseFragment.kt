package com.wjf.self_library.common;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.wjf.self_library.BuildConfig;
import com.wjf.self_library.R;
import com.wjf.self_library.util.StatusBar.StatusBarUtil;
import com.wjf.self_library.view.codingUI.CommonToolbar;

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    protected T view;
    /** 封装toolbar */
    protected CommonToolbar commonToolbar;

    private boolean isLoaded = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(setLayout(), container, false);
        this.view = DataBindingUtil.bind(root);
        initColorStatusBar(root);
        return root;
    }

    private void initColorStatusBar(View view) {
        commonToolbar = view.findViewById(R.id.commonToolbar);
        if (commonToolbar != null) {
            int color = commonToolbar.getBackgroundColor();
            if (color == 0) {
                // 透明，默认同样透明主题
                StatusBarUtil.setTranslucentStatus(getActivity(), commonToolbar.isDarkTheme());
            } else {
                StatusBarUtil.setStatusBarColor(getActivity(), color, commonToolbar.isDarkTheme());
            }
        }
    }

    /** 黑色主题透明状态栏 */
    protected void darkTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(getActivity(), true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i(BuildConfig.TAG, "---" + this.getClass().getCanonicalName() + "隐藏");
        }
        lazyInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyInit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
    }

    private void lazyInit() {
        if (!isLoaded && !isHidden()) {
            initView();
            initData();
            subscribeUi();
            isLoaded = true;
            Log.i(BuildConfig.TAG, "---" + this.getClass().getCanonicalName() + "加载");
        }
    }

    /** 订阅ViewModel */
    protected void subscribeUi() {}

    /**
     * 设置布局
     *
     * @return 布局id
     */
    public abstract int setLayout();

    /** 初始化控件 */
    protected abstract void initView();

    /** 初始化数据 */
    protected abstract void initData();

    protected void toastShort(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
