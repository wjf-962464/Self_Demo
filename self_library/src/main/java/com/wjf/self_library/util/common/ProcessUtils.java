package com.wjf.self_library.util.common;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.KILL_BACKGROUND_PROCESSES;

public final class ProcessUtils {

    /**
     * Return the foreground process name.
     *
     * <p>Target APIs greater than 21 must hold {@code <uses-permission
     * android:name="android.permission.PACKAGE_USAGE_STATS" />}
     *
     * @return the foreground process name
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String getForegroundProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> pInfo = am.getRunningAppProcesses();
        if (pInfo != null && !pInfo.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo aInfo : pInfo) {
                if (aInfo.importance
                        == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return aInfo.processName;
                }
            }
        }
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list =
                pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {
            return "";
        }
        try { // Access to usage information.
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager aom = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            //noinspection ConstantConditions
            if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName)
                    != AppOpsManager.MODE_ALLOWED) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName)
                    != AppOpsManager.MODE_ALLOWED) {
                return "";
            }
            UsageStatsManager usageStatsManager =
                    (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> usageStatsList = null;
            if (usageStatsManager != null) {
                long endTime = System.currentTimeMillis();
                long beginTime = endTime - 86400000 * 7;
                usageStatsList =
                        usageStatsManager.queryUsageStats(
                                UsageStatsManager.INTERVAL_BEST, beginTime, endTime);
            }
            if (usageStatsList == null || usageStatsList.isEmpty()) {
                return "";
            }
            UsageStats recentStats = null;
            for (UsageStats usageStats : usageStatsList) {
                if (recentStats == null
                        || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                    recentStats = usageStats;
                }
            }
            return recentStats == null ? null : recentStats.getPackageName();
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.error(e);
        }
        return "";
    }

    /**
     * Return all background processes.
     *
     * <p>Must hold {@code <uses-permission
     * android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}
     *
     * @return all background processes
     */
    @RequiresPermission(KILL_BACKGROUND_PROCESSES)
    public static Set<String> getAllBackgroundProcesses(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //noinspection ConstantConditions
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        if (info != null) {
            for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                Collections.addAll(set, aInfo.pkgList);
            }
        }
        return set;
    }

    /**
     * Return whether app running in the main process.
     *
     * @return {@code true}: yes<br>
     * {@code false}: no
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getCurrentProcessName());
    }

    /**
     * Return the name of current process.
     *
     * <p>It's faster than ActivityManager.
     *
     * @return the name of current process
     */
    public static String getCurrentProcessName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }

        File file = new File("/proc/" + android.os.Process.myPid() + "/cmdline");
        try (BufferedReader mBufferedReader = new BufferedReader(new FileReader(file))) {
            return mBufferedReader.readLine().trim();
        } catch (Exception e) {
            LogUtils.error(e);
            return null;
        }
    }
}
