package com.wjf.self_library.view.swipebacklayout.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.wjf.self_library.R;
import com.wjf.self_library.view.swipebacklayout.SwipeBackLayout;
import com.wjf.self_library.view.swipebacklayout.SwipeBackUtils;

/**
 * @author Yrom
 */
public class SwipeBackActivityHelper {
    private final Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout =
                (SwipeBackLayout)
                        LayoutInflater.from(mActivity).inflate(R.layout.swipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(
                new SwipeBackLayout.SwipeListener() {
                    @Override
                    public void onScrollStateChange(int state, float scrollPercent) {
                    }

                    @Override
                    public void onEdgeTouch(int edgeFlag) {
                        SwipeBackUtils.convertActivityToTranslucent(mActivity);
                    }

                    @Override
                    public void onScrollOverThreshold() {
                    }
                });
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
