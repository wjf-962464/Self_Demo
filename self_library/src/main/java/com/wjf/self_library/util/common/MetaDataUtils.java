package com.wjf.self_library.util.common;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import androidx.annotation.NonNull;

public final class MetaDataUtils {

    private MetaDataUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the value of meta-data in application.
     *
     * @param key The key of meta-data.
     * @return the value of meta-data in application
     */
    public static String getMetaDataInApp(Context context, @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.error(e);
        }
        return value;
    }

    /**
     * Return the value of meta-data in activity.
     *
     * @param activity The activity.
     * @param key      The key of meta-data.
     * @return the value of meta-data in activity
     */
    public static String getMetaDataInActivity(
            Context context, @NonNull final Activity activity, @NonNull final String key) {
        return getMetaDataInActivity(context, activity.getClass(), key);
    }

    /**
     * Return the value of meta-data in activity.
     *
     * @param clz The activity class.
     * @param key The key of meta-data.
     * @return the value of meta-data in activity
     */
    public static String getMetaDataInActivity(
            Context context,
            @NonNull final Class<? extends Activity> clz,
            @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, clz);
        try {
            ActivityInfo ai = pm.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.error(e);
        }
        return value;
    }

    /**
     * Return the value of meta-data in service.
     *
     * @param service The service.
     * @param key     The key of meta-data.
     * @return the value of meta-data in service
     */
    public static String getMetaDataInService(
            Context context, @NonNull final Service service, @NonNull final String key) {
        return getMetaDataInService(context, service.getClass(), key);
    }

    /**
     * Return the value of meta-data in service.
     *
     * @param clz The service class.
     * @param key The key of meta-data.
     * @return the value of meta-data in service
     */
    public static String getMetaDataInService(
            Context context,
            @NonNull final Class<? extends Service> clz,
            @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, clz);
        try {
            ServiceInfo info = pm.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            value = String.valueOf(info.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.error(e);
        }
        return value;
    }

    /**
     * Return the value of meta-data in receiver.
     *
     * @param receiver The receiver.
     * @param key      The key of meta-data.
     * @return the value of meta-data in receiver
     */
    public static String getMetaDataInReceiver(
            Context context,
            @NonNull final BroadcastReceiver receiver,
            @NonNull final String key) {
        return getMetaDataInReceiver(context, receiver.getClass(), key);
    }

    /**
     * Return the value of meta-data in receiver.
     *
     * @param clz The receiver class.
     * @param key The key of meta-data.
     * @return the value of meta-data in receiver
     */
    public static String getMetaDataInReceiver(
            Context context,
            @NonNull final Class<? extends BroadcastReceiver> clz,
            @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, clz);
        try {
            ActivityInfo info = pm.getReceiverInfo(componentName, PackageManager.GET_META_DATA);
            value = String.valueOf(info.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.error(e);
        }
        return value;
    }
}
