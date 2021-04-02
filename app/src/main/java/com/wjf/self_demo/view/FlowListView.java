package com.wjf.self_demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Wangjf
 * @date : 2021/4/1
 */
public class FlowListView extends ViewGroup {
    private int mVerticalSpace = 20, mHorizontalSpace = 20;
    private List<List<View>> allLines;
    private List<Integer> lineHeights;

    public FlowListView(Context context) {
        super(context);
    }

    public FlowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("debug", "构造2");
    }

    private void initMeasureParams() {
        if (allLines == null) {
            allLines = new ArrayList<>();
        } else {
            allLines.clear();
        }
        if (lineHeights == null) {
            lineHeights = new ArrayList<>();
        } else {
            lineHeights.clear();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("debug", "onMeasure");
        initMeasureParams();

        int paddingLeft = getPaddingLeft(),
                paddingRight = getPaddingRight(),
                paddingTop = getPaddingTop(),
                paddingBottom = getPaddingBottom();
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);
        List<View> lineView = new ArrayList<>();
        int lineWidth = 0, lineHeight = 0;
        int parentNeedWidth = paddingLeft + paddingRight, parentNeedHeight = paddingTop + paddingBottom;

        for (int i = 0, len = getChildCount(); i < len; i++) {
            View childView = getChildAt(i);
            LayoutParams childParams = childView.getLayoutParams();

            int childWidthMeasureSpec =
                    getChildMeasureSpec(
                            widthMeasureSpec, paddingLeft + paddingRight, childParams.width);
            int childHeightMeasureSpec =
                    getChildMeasureSpec(
                            heightMeasureSpec, paddingTop + paddingBottom, childParams.height);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (childWidth + lineWidth > selfWidth) {
                allLines.add(lineView);
                lineHeights.add(lineHeight);
                parentNeedWidth = Math.max(parentNeedWidth, lineWidth + mHorizontalSpace);
                parentNeedHeight = parentNeedHeight + lineHeight + mVerticalSpace;
                lineView = new ArrayList<>();

                lineWidth = 0;
                lineHeight = 0;
            }

            lineWidth = lineWidth + childWidth + mHorizontalSpace;
            lineHeight = Math.max(lineHeight, childHeight);
            lineView.add(childView);
            if (i == len - 1) {
                allLines.add(lineView);
                lineHeights.add(lineHeight);
                parentNeedWidth = Math.max(parentNeedWidth, lineWidth + mHorizontalSpace);
                parentNeedHeight = parentNeedHeight + lineHeight;
            }
        }
        int widMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int realNeedWidth = widMode == MeasureSpec.EXACTLY ? selfWidth : parentNeedWidth;
        int realNeedHeight = heightMode == MeasureSpec.EXACTLY ? selfHeight : parentNeedHeight;
        setMeasuredDimension(realNeedWidth, realNeedHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("debug", "onLayout");
        int paddingLeft = getPaddingLeft();
        int curT = getPaddingTop();
        for (int i = 0, len1 = allLines.size(); i < len1; i++) {
            int curL = paddingLeft;
            List<View> lineView = allLines.get(i);
            for (int j = 0, len2 = lineView.size(); j < len2; j++) {
                View childView = lineView.get(j);
                int top = curT;
                int left = curL;
                int right = left + childView.getMeasuredWidth();
                int bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                curL = right + mHorizontalSpace;
            }
            if (i == len1 - 1) {
                curT = curT + lineHeights.get(i);
            } else {
                curT = curT + lineHeights.get(i) + mVerticalSpace;
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("debug", "onDraw");
    }
}
