package com.wjf.self_library.util.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public final class CleanUtils {

    /**
     * Clean the internal cache.
     *
     * <p>directory: /data/data/package/cache
     *
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean cleanInternalCache(Context context) {
        return FileUtils.deleteFilesInDir(context.getCacheDir());
    }

    /**
     * Clean the internal files.
     *
     * <p>directory: /data/data/package/files
     *
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean cleanInternalFiles(Context context) {
        return FileUtils.deleteFilesInDir(context.getFilesDir());
    }

    /**
     * Clean the internal databases.
     *
     * <p>directory: /data/data/package/databases
     *
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean cleanInternalDbs(Context context) {
        return FileUtils.deleteFilesInDir(new File(context.getFilesDir().getParent(), "databases"));
    }

    /**
     * Clean the internal database by name.
     *
     * <p>directory: /data/data/package/databases/dbName
     *
     * @param dbName The name of database.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean cleanInternalDbByName(Context context, final String dbName) {
        return context.deleteDatabase(dbName);
    }

    /**
     * Clean the internal shared preferences.
     *
     * <p>directory: /data/data/package/shared_prefs
     *
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean cleanInternalSp(Context context) {
        return FileUtils.deleteFilesInDir(
                new File(context.getFilesDir().getParent(), "shared_prefs"));
    }

    /**
     * Clean the external cache.
     *
     * <p>directory: /storage/emulated/0/android/data/package/cache
     *
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean cleanExternalCache(Context context) {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && FileUtils.deleteFilesInDir(context.getExternalCacheDir());
    }
}
