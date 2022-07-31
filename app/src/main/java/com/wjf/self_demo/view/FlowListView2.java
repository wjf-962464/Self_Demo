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
public class FlowListView2 extends ViewGroup {
    private final int mVerticalSpace = 20;
    private final int mHorizontalSpace = 20;
    private List<List<View>> allLines;
    private List<Integer> lineHeights;

    public FlowListView2(Context context) {
        super(context);
    }

    public FlowListView2(Context context, AttributeSet attrs) {
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

        int needWidth = 0, needHeight = 0;

        int lineWidth = 0, lineHeight = 0;
        for (int i = 0, len = getChildCount(); i < len; i++) {
            View childView = getChildAt(i);
            LayoutParams layoutParams = childView.getLayoutParams();

            int childWidthMeasureSpec =
                    getChildMeasureSpec(
                            widthMeasureSpec, paddingLeft + paddingRight, layoutParams.width);
            int childHeightMeasureSpec =
                    getChildMeasureSpec(
                            heightMeasureSpec, paddingTop + paddingBottom, layoutParams.height);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (lineWidth + childWidth + mHorizontalSpace > selfWidth) {
                // 这里作换行
                needWidth = Math.max(needWidth, lineWidth);
                lineHeights.add(lineHeight);
                needHeight += lineHeight + mVerticalSpace;
                lineHeight = 0;
                lineWidth = 0;
            }
            lineHeight = Math.max(lineHeight, childHeight);
            lineWidth += childWidth + mHorizontalSpace;

            if (i == len - 1) {
                // 最后一行
                lineHeights.add(lineHeight);
                needWidth = Math.max(needWidth, lineWidth);
                needHeight += lineHeight;
            }
        }

        int realWidth =
                MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? selfWidth
                        : needWidth;
        int realHeight =
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? selfHeight
                        : needHeight;

        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("debug", "onLayout");
        int width = getWidth(), height = getHeight();
        int curT = getPaddingTop(), curL = getPaddingLeft();
        int line = 0;
        for (int i = 0, len = getChildCount(); i < len; i++) {
            int lineHeight = lineHeights.get(line);
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (curL + childWidth + mHorizontalSpace > width) {
                // 换行
                Log.d("wjf", "换行");
                curT += lineHeight + mVerticalSpace;
                Log.d("wjf", "curT" + curT);
                curL = getPaddingLeft();
                line++;
                lineHeight = lineHeights.get(line);
            }
            int half = childHeight / 2;
            int midHeight = curT + lineHeight / 2;
            int childTop = midHeight - half;
            int childLeft = curL;
            int childRight = childLeft + childWidth;
            int childBottom = midHeight + half;
            childView.layout(childLeft, childTop, childRight, childBottom);

            curL += childWidth + mHorizontalSpace;
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
