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

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    protected T view;
    private boolean isLoaded = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(setLayout(), container, false);
        this.view = DataBindingUtil.bind(root);
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i("wjf", "---" + this.getClass().getCanonicalName() + "隐藏");
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
            isLoaded = true;
            Log.i("wjf", "---" + this.getClass().getCanonicalName() + "加载");
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

    protected void toastShort(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
