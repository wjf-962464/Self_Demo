package com.wjf.self_library.util.common;

import android.util.Log;

import androidx.annotation.Nullable;

public abstract class TimerTask extends ThreadUtils.BaseTask<String> {
    private static final String TAG = "TEST";
    private final int timeMaxLimit;
    private final String url;
    private int time;

    public TimerTask(int timeMaxLimit, String url) {
        this.timeMaxLimit = timeMaxLimit;
        this.url = url;
        time = 0;
    }

    /**
     * 运行超时
     */
    public abstract void runTimeOut();

    @Nullable
    @Override
    public String doInBackground() throws Exception {
        if (time == timeMaxLimit) {
            runTimeOut();
            cancel();
        }
        return String.valueOf(++time);
    }

    @Override
    public void onSuccess(@Nullable String result) {
        Log.d(TAG, "task... " + url + "...time：" + result);
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "task... " + url + "...onCancel：");
    }

    @Override
    public void onFail(Throwable t) {
        Log.d(TAG, "onFail");
    }
}
