package com.wjf.self_demo;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.Choreographer;

import com.github.moduth.blockcanary.BlockCanary;
import com.wjf.barcode.BeepManager;
import com.wjf.barcode.CustomException;
import com.wjf.self_demo.frame.fps.AppBlockCanaryContext;
import com.wjf.self_demo.frame.fps.FPSFrameCallback2;
import com.wjf.self_demo.frame.fps.HandlerBlockTask;
import com.wjf.self_library.common.BaseApplication;
import com.wjf.self_library.http.HttpManager;

/** @author asus */
public class MyApp extends BaseApplication {

    private static final String SP_TAG = "MyApp";
    private HandlerThread mBlockThread = new HandlerThread("blockThread");

    @Override
    public void onCreate() {
        super.onCreate();
        HttpManager.Builder builder =
                new HttpManager.Builder().timeout(5L).baseUrl(HttpService.baseUrl);
        HttpManager.Instance.init(builder);
        BeepManager.init(this, SP_TAG);
/*        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        mBlockThread.start();
        Choreographer.getInstance()
                .postFrameCallback(new FPSFrameCallback2(new Handler(mBlockThread.getLooper())));
        new HandlerBlockTask().startWork();*/
        try {
            BeepManager.getInstance()
                    .setBeepRes(R.raw.beep)
                    .setBeepVolume(0.10f)
                    .setVibrateDuration(200L)
                    .build();
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}
