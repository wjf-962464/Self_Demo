package com.wjf.self_library.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wangjf2-DESKTOP
 */
public class ActivityCollector {
    private static final List<Activity> ACT_LIST = new ArrayList<>();

    private ActivityCollector() {
    }

    public static void addActivity(Activity act) {
        ACT_LIST.add(act);
    }

    public static void removeActivity(Activity act) {
        ACT_LIST.remove(act);
    }

    public static void finishAll() {
        for (Activity act : ACT_LIST) {
            if (!act.isFinishing()) {
                act.finish();
            }
        }
    }
}
