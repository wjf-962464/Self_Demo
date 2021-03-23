package com.wjf.self_library.util.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.Context.WIFI_SERVICE;

public final class NetworkUtils {

    /**
     * Open the settings of wireless.
     */
    public static void openWirelessSettings(Context context) {
        context.startActivity(
                new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Return whether network is connected.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return {@code true}: connected<br>
     *     {@code false}: disconnected
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * Return whether network is available.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return {@code true}: available<br>
     *     {@code false}: not available
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isAvailable(Context context) {

        ConnectivityManager manager =
                (ConnectivityManager)
                        context.getApplicationContext()
                                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * Return whether using mobile data.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return {@code true}: yes<br>
     *     {@code false}: no
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileData(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return null != info
                && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * Return whether using 4G.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return {@code true}: yes<br>
     *     {@code false}: no
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean is4G(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null
                && info.isAvailable()
                && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * Return whether wifi is enabled.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />}
     *
     * @return {@code true}: enabled<br>
     *     {@code false}: disabled
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled(Context context) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager =
                (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (manager == null) {
            return false;
        }
        return manager.isWifiEnabled();
    }

    /**
     * Return whether wifi is connected.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return {@code true}: connected<br>
     *     {@code false}: disconnected
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * Return whether wifi is available.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}
     *
     * @return {@code true}: available<br>
     *     {@code false}: unavailable
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static boolean isWifiAvailable(Context context) {
        return getWifiEnabled(context);
    }

    /**
     * Return the name of network operate.
     *
     * @return the name of network operate
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getNetworkOperatorName();
    }

    /**
     * Return type of network.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return type of network
     *     <ul>
     *       <li>{@link NetworkUtils.NetworkType#NETWORK_ETHERNET}
     *       <li>{@link NetworkUtils.NetworkType#NETWORK_WIFI }
     *       <li>{@link NetworkUtils.NetworkType#NETWORK_4G }
     *       <li>{@link NetworkUtils.NetworkType#NETWORK_3G }
     *       <li>{@link NetworkUtils.NetworkType#NETWORK_2G }
     *       <li>{@link NetworkUtils.NetworkType#NETWORK_UNKNOWN }
     *     </ul>
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkType getNetworkType(Context context) {
        if (isEthernet(context)) {
            return NetworkType.NETWORK_ETHERNET;
        }
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetworkType.NETWORK_2G;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.NETWORK_3G;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.NETWORK_4G;

                    case TelephonyManager.NETWORK_TYPE_NR:
                        return NetworkType.NETWORK_5G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return NetworkType.NETWORK_3G;
                        }
                }
            }
        }
        return NetworkType.NETWORK_UNKNOWN;
    }

    /**
     * Return whether using ethernet.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
     * />}
     *
     * @return {@code true}: yes<br>
     *     {@code false}: no
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isEthernet(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (info == null) {
            return false;
        }
        NetworkInfo.State state = info.getState();
        if (null == state) {
            return false;
        }
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        return cm.getActiveNetworkInfo();
    }

    /**
     * Return the ip address.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}
     *
     * @param useIPv4 True to use ipv4, false otherwise.
     * @return the ip address
     */
    @RequiresPermission(INTERNET)
    public static String getIpAddress(final boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement());
                }
            }
            for (InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress;
                        }
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.error(e);
        }
        return "";
    }

    /**
     * Return the ip address of broadcast.
     *
     * @return the ip address of broadcast
     */
    public static String getBroadcastIpAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                List<InterfaceAddress> ias = ni.getInterfaceAddresses();
                for (int i = 0, size = ias.size(); i < size; i++) {
                    InterfaceAddress ia = ias.get(i);
                    InetAddress broadcast = ia.getBroadcast();
                    if (broadcast != null) {
                        return broadcast.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.error(e);
        }
        return "";
    }

    /**
     * Return the domain address.
     *
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}
     *
     * @param domain The name of domain.
     * @return the domain address
     */
    @RequiresPermission(INTERNET)
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            LogUtils.error(e);
            return "";
        }
    }

    public enum NetworkType {
        /** 以太网 */
        NETWORK_ETHERNET,
        /** WIFI */
        NETWORK_WIFI,
        /** 5G */
        NETWORK_5G,
        /** 4G */
        NETWORK_4G,
        /** 3G */
        NETWORK_3G,
        /** 2G */
        NETWORK_2G,
        /** 未知 */
        NETWORK_UNKNOWN;

        @Override
        public String toString() {
            switch (this) {
                case NETWORK_2G:
                    return "2G";
                case NETWORK_3G:
                    return "3G";
                case NETWORK_4G:
                    return "4G";
                case NETWORK_5G:
                    return "5G";
                case NETWORK_WIFI:
                    return "wifi";
                case NETWORK_ETHERNET:
                    return "ethernet";
                default:
                    return "unknown";
            }
        }
    }
}
