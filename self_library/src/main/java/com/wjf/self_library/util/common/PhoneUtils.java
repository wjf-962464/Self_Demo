package com.wjf.self_library.util.common;

import android.content.Context;
import android.telephony.TelephonyManager;

public final class PhoneUtils {

    /**
     * Return whether the device is phone.
     *
     * @return {@code true}: yes<br>
     *     {@code false}: no
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * Returns the current phone type.
     *
     * @return the current phone type
     *     <ul>
     *       <li>{@link TelephonyManager#PHONE_TYPE_NONE}
     *       <li>{@link TelephonyManager#PHONE_TYPE_GSM }
     *       <li>{@link TelephonyManager#PHONE_TYPE_CDMA}
     *       <li>{@link TelephonyManager#PHONE_TYPE_SIP }
     *     </ul>
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm.getPhoneType();
    }

    /**
     * Return whether sim card state is ready.
     *
     * @return {@code true}: yes<br>
     *     {@code false}: no
     */
    public static boolean isSimCardReady(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * Return the sim operator name.
     *
     * @return the sim operator name
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        return tm.getSimOperatorName();
    }

    /**
     * Return the sim operator using mnc.
     *
     * @return the sim operator
     */
    public static String getSimOperatorByMnc(Context context) {
        TelephonyManager tm = getTelephonyManager(context);
        String operator = tm.getSimOperator();
        if (operator == null) {
            return "";
        }
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
                return "中国移动";
            case "46001":
            case "46006":
            case "46009":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
}
