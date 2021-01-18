package com.wjf.self_library.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wangjf2-DESKTOP
 */
public class ActivityCollector {
    private ActivityCollector() {}
 
    private static List<Activity> actList = new ArrayList<>();
 
    public static void addActivity(Activity act) {
        actList.add(act);
    }
 
    public static void removeActivity(Activity act) {
        actList.remove(act);
    }
 
    public static void finishAll() {
        for (Activity act : actList) {
            if (!act.isFinishing()) {
                act.finish();
            }
        }
    }
}
