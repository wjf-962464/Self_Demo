package com.wjf.self_demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Wangjf
 * @date : 2021/4/1
 */
public class FlowListView extends ViewGroup {
    private final int mVerticalSpace = 20;
    private final int mHorizontalSpace = 20;
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
        int parentNeedWidth = paddingLeft + paddingRight,
                parentNeedHeight = paddingTop + paddingBottom;

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
            curT += lineHeights.get(i);
            curT = i == len1 - 1 ? curT : curT + mVerticalSpace;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("debug", "onDraw");
    }

    /**
     * 处理由本层级的dispatchtouchevent方法派发来的事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                Logger.d("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Logger.d("ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP: // 初始化两个标记参数
                Logger.d("ACTION_UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float eX = ev.getX();
        float eY = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN: // 监听down事件 判断是不是落在child2里边
                int[] result = getWitchChild(eX, eY);
                if (result[1] == -1) {
                    Logger.d("位于第" + (result[0] + 1) + "行空白部分");
                } else {

                    Logger.d(
                            "dispatchTouchEvent>>>ACTION_DOWN"
                                    + "\nx："
                                    + eX
                                    + "\ny："
                                    + eY
                                    + "\nchild：第"
                                    + (result[0] + 1)
                                    + "行，第"
                                    + (result[1] + 1)
                                    + "个>>>"
                                    + handleClick(eX, eY, ev));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Logger.d("dispatchTouchEvent>>>ACTION_MOVE");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int[] getWitchChild(float eX, float eY) {
        int[] result = new int[] {-1, -1};
        float height = getPaddingTop();
        for (int i = 0, len = lineHeights.size(); i < len; i++) {
            height = lineHeights.get(i) + height + mVerticalSpace;
            if (eY < height) {
                result[0] = i;
                break;
            }
        }
        if (result[0] == -1) {
            return result;
        }
        List<View> lineViews = allLines.get(result[0]);
        for (int i = 0, len = lineViews.size(); i < len; i++) {
            View childView = lineViews.get(i);
            if (eX > childView.getLeft()
                    && eX < childView.getRight()
                    && eY > childView.getTop()
                    && eY < childView.getBottom()) {
                result[1] = i;
                break;
            }
        }

        return result;
    }

    private CharSequence handleClick(float eX, float eY, MotionEvent ev) {
        int[] result = getWitchChild(eX, eY);
        View childView = allLines.get(result[0]).get(result[1]);

        childView.dispatchTouchEvent(ev);
        return ((TextView) childView).getText();
    }
}
