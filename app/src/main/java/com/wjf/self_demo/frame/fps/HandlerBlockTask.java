package com.wjf.self_demo.frame.fps;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

public class HandlerBlockTask {
    private final static String TAG = "budaye";
    public final int BLOCK_TMME = 1000;
    private HandlerThread mBlockThread = new HandlerThread("blockThread");
    private Handler mHandler;

    private Runnable mBlockRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.d(TAG, sb.toString());
        }
    };

    public void startWork() {
        mBlockThread.start();
        mHandler = new Handler(mBlockThread.getLooper());
        Looper.getMainLooper().setMessageLogging(new Printer() {
            private static final String START = ">>>>> Dispatching";
            private static final String END = "<<<<< Finished";

            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    startMonitor();
                }
                if (x.startsWith(END)) {
                    removeMonitor();
                }
            }
        });
    }

    private void startMonitor() {
        mHandler.postDelayed(mBlockRunnable, BLOCK_TMME);
    }

    private void removeMonitor() {
        mHandler.removeCallbacks(mBlockRunnable);
    }
}
