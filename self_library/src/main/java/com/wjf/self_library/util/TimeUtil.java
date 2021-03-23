package com.wjf.self_library.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    /**
     * 获取参数时间戳距当前的时间
     *
     * @param timestamp 时间戳
     * @return int
     */
    public static int compareWithNow(long timestamp, String type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long diffTime = System.currentTimeMillis() - timestamp;
        switch (type) {
            case "day":
                return (int) (diffTime / (24 * 60 * 60 * 1000));
            case "hour":
                return (int) (diffTime / (60 * 60 * 1000));
            case "min":
                return (int) (diffTime / (60 * 1000));
            case "sec":
                return (int) (diffTime / (1000));
            default:
                // Log.e(Constants.INFO_TAG, com.coding.poemway.util.TimeUtil.class.getSimpleName()
                // +
                // "：转换的类型输入有误");
                return -1;
        }
    }

    /**
     * 获得对比当前时间的多少时间前，如抖音 4小时前
     *
     * @param timestamp 时间戳
     * @return 对应字符串
     */
    public static String getDifferentTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long diffTime = System.currentTimeMillis() - timestamp;
        long day = diffTime / (24 * 60 * 60 * 1000);
        long hour = (diffTime / (60 * 60 * 1000) - day * 24);
        long min = ((diffTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (diffTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day > 0) {
            return day + "天前";
        }
        if (hour > 0) {
            return hour + "小时前";
        }
        if (min > 0) {
            return min + "分钟前";
        }
        return s + "秒前";
    }

    /**
     * 获取参数时间戳距今天的天数或日期
     *
     * @param timestamp 时间戳
     * @return 对应字符串
     * @throws ParseException 解析异常
     */
    public static String getDifDay(long timestamp) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = format.parse(getStringTime(timestamp));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long diffTime = date.getTime() - calendar.getTimeInMillis();
        long day = diffTime / (24 * 60 * 60 * 1000);
        if (day == 0) {
            return "今天";
        }
        if (day == -1) {
            return "昨天";
        }
        return getStringMonDayTime(timestamp);
    }

    /**
     * 获取long类型时间戳的 【年-月-日 小时:分钟:秒】string
     *
     * @param timestamp 时间戳
     * @return 对应字符串
     */
    public static String getStringTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(timestamp);
    }

    /**
     * 获取long类型时间戳的 【月份-日期】string
     *
     * @param timestamp 时间戳
     * @return 对应字符串
     */
    public static String getStringYearMonDayTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(timestamp);
    }

    /**
     * 获取long类型时间戳的 【月份-日期】string
     *
     * @param timestamp 时间戳
     * @return 对应字符串
     */
    public static String getStringMonDayTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.CHINA);
        return format.format(timestamp);
    }

    public static Long getLongTimestamp(String time) {
        Long timestamp = null;
        if (time != null) {
            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timestamp = sdf.parse(time).getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timestamp;
    }

    /**
     * 获取long类型时间戳的 【小时:分钟】string
     *
     * @param timestamp 时间戳
     * @return 对应字符串
     */
    public static String getStringHourMinTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return format.format(timestamp);
    }

    /**
     * 将string类时间戳转换为long类型
     *
     * @param timeStr timeStr
     * @return long类型时间
     * @throws ParseException 解析错误
     */
    public static long getStampTime(String timeStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = format.parse(timeStr);
        // 转换为Date类
        return date.getTime();
    }

    public static String getEnglishMonth(int month) {
        switch (month) {
            case 1:
                return "Jan.";
            case 2:
                return "Feb.";
            case 3:
                return "Mar.";
            case 4:
                return "Apr.";
            case 5:
                return "May.";
            case 6:
                return "Jun.";
            case 7:
                return "Jul.";
            case 8:
                return "Aug.";
            case 9:
                return "Sept.";
            case 10:
                return "Oct.";
            case 11:
                return "Nov.";
            case 12:
                return "Dec.";
            default:
                return null;
        }
    }
}
