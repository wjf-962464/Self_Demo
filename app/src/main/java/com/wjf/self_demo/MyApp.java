package com.wjf.self_demo;

import com.wjf.barcode.BeepManager;
import com.wjf.barcode.CustomException;
import com.wjf.self_library.common.BaseApplication;
import com.wjf.self_library.http.HttpManager;

/** @author asus */
public class MyApp extends BaseApplication {

    private static final String SP_TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();
        HttpManager.Builder builder=new HttpManager.Builder().timeout(5L).baseUrl(HttpService.baseUrl);
        HttpManager.Instance.init(builder);
        BeepManager.init(this, SP_TAG);
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
