package com.wjf.self_library.util.common;

import android.content.Context;

import androidx.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public final class ResourceUtils {

    /**
     * @param context
     * @param assetsPath     文件在assets的路径，assets根目录就传""
     * @param assetsFileName 文件名称
     * @return true:存在；false：不存在
     */
    public static boolean hasFileInAssets(
            Context context, final String assetsPath, final String assetsFileName) {
        try {
            String[] assets = context.getAssets().list(assetsPath);
            if (assets != null && assets.length > 0) {
                for (String asset : assets) {
                    if (assetsFileName.equals(asset)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return false;
    }

    /**
     * Copy the file from assets.
     *
     * @param assetsFilePath The path of file in assets.
     * @param destFilePath The path of destination file.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean copyFileFromAssets(
            Context context, final String assetsFilePath, final String destFilePath) {
        boolean res = true;
        try {
            String[] assets = context.getAssets().list(assetsFilePath);
            if (assets.length > 0) {
                for (String asset : assets) {
                    res &=
                            copyFileFromAssets(
                                    context,
                                    assetsFilePath + "/" + asset,
                                    destFilePath + "/" + asset);
                }
            } else {
                res =
                        FileIoUtils.writeFileFromIS(
                                destFilePath, context.getAssets().open(assetsFilePath), false);
            }
        } catch (IOException e) {
            LogUtils.error(e);
            res = false;
        }
        return res;
    }

    /**
     * Return the content of assets.
     *
     * @param assetsFilePath The path of file in assets.
     * @return the content of assets
     */
    public static String readAssets2String(Context context, final String assetsFilePath) {
        return readAssets2String(context, assetsFilePath, null);
    }

    /**
     * Return the content of assets.
     *
     * @param assetsFilePath The path of file in assets.
     * @param charsetName The name of charset.
     * @return the content of assets
     */
    public static String readAssets2String(
            Context context, final String assetsFilePath, final String charsetName) {
        InputStream is;
        try {
            is = context.getAssets().open(assetsFilePath);
        } catch (IOException e) {
            LogUtils.error(e);
            return null;
        }
        byte[] bytes = FileIoUtils.is2Bytes(is);
        if (bytes == null) {
            return null;
        }
        if (StringUtils.isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                LogUtils.error(e);
                return "";
            }
        }
    }

    /**
     * Return the content of file in assets.
     *
     * @param assetsPath The path of file in assets.
     * @return the content of file in assets
     */
    public static List<String> readAssets2List(Context context, final String assetsPath) {
        return readAssets2List(context, assetsPath, null);
    }

    /**
     * Return the content of file in assets.
     *
     * @param assetsPath The path of file in assets.
     * @param charsetName The name of charset.
     * @return the content of file in assets
     */
    public static List<String> readAssets2List(
            Context context, final String assetsPath, final String charsetName) {
        try {
            return is2List(context.getResources().getAssets().open(assetsPath), charsetName);
        } catch (IOException e) {
            LogUtils.error(e);
            return null;
        }
    }

    /**
     * Copy the file from raw.
     *
     * @param resId The resource id.
     * @param destFilePath The path of destination file.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean copyFileFromRaw(
            Context context, @RawRes final int resId, final String destFilePath) {
        return FileIoUtils.writeFileFromIS(
                destFilePath, context.getResources().openRawResource(resId), false);
    }

    /**
     * Return the content of resource in raw.
     *
     * @param resId The resource id.
     * @return the content of resource in raw
     */
    public static String readRaw2String(Context context, @RawRes final int resId) {
        return readRaw2String(context, resId, null);
    }

    /**
     * Return the content of resource in raw.
     *
     * @param resId The resource id.
     * @param charsetName The name of charset.
     * @return the content of resource in raw
     */
    public static String readRaw2String(
            Context context, @RawRes final int resId, final String charsetName) {
        InputStream is = context.getResources().openRawResource(resId);
        byte[] bytes = FileIoUtils.is2Bytes(is);
        if (bytes == null) {
            return null;
        }
        if (StringUtils.isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                LogUtils.error(e);
                return "";
            }
        }
    }

    /**
     * Return the content of resource in raw.
     *
     * @param resId The resource id.
     * @return the content of file in assets
     */
    public static List<String> readRaw2List(Context context, @RawRes final int resId) {
        return readRaw2List(context, resId, null);
    }

    /**
     * Return the content of resource in raw.
     *
     * @param resId The resource id.
     * @param charsetName The name of charset.
     * @return the content of file in assets
     */
    public static List<String> readRaw2List(
            Context context, @RawRes final int resId, final String charsetName) {
        return is2List(context.getResources().openRawResource(resId), charsetName);
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static List<String> is2List(final InputStream is, final String charsetName) {
        BufferedReader reader = null;
        try {
            List<String> list = new ArrayList<>();
            if (StringUtils.isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(is));
            } else {
                reader = new BufferedReader(new InputStreamReader(is, charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            LogUtils.error(e);
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                LogUtils.error(e);
            }
        }
    }
}
