package com.wjf.self_demo.loadlayout;

import com.wjf.loadLayout.callback.ICallback;
import com.wjf.self_demo.R;

/** @author WJF */
public class EmptyCallback extends ICallback {
    public EmptyCallback(ICallback.CallOnListener listener) {
        super(listener);
    }

    @Override
    public int layoutResource() {
        return R.layout.callback_empty;
    }

    @Override
    public void show() {}
}
