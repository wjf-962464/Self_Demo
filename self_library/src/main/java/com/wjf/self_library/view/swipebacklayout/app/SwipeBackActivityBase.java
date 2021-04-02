package com.wjf.self_library.view.swipebacklayout.app;

import com.wjf.self_library.view.swipebacklayout.SwipeBackLayout;

/**
 * @author Yrom
 */
public interface SwipeBackActivityBase {
    /** @return the SwipeBackLayout associated with this activity. */
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /** Scroll out contentView and finish the activity */
    void scrollToFinishActivity();
}
