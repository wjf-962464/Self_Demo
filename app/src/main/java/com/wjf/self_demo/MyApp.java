package com.wjf.self_demo;

import android.app.Application;

import com.wjf.barcode.CustomException;
import com.wjf.self_demo.Zxing.BeepManager;

/** @author asus */
public class MyApp extends Application {
    private static final String SP_TAG = "MyApp";
    @Override
    public void onCreate() {
        super.onCreate();
        BeepManager.init(this,SP_TAG);
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
