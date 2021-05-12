package com.wjf.self_demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.wjf.self_demo.R;

public class DemoActivity extends Activity {
    public static final String UPPER_NUM = "upper";
    private CalThread calThread;

    class CalThread extends Thread {
        private Handler mHandler =
                new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0x123) {
                            int upper = msg.getData().getInt(UPPER_NUM);
                            for (int i = 1; i <= upper; i++) {
                                if (i % 2 == 0) {
                                    System.out.println(i);
                                }
                            }
                        }
                    }
                };

        @Override
        public void run() {
            Looper.prepare();

            Looper.loop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calThread = new CalThread();
        calThread.start();

        Message msg = new Message();
        msg.what = 0x123;
        Bundle bundle = new Bundle();
        bundle.putInt(UPPER_NUM, 20);
        msg.setData(bundle);
        calThread.mHandler.sendMessage(msg);
    }
}
