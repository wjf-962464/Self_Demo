package org.jxxy.debug.corekit.util

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    private val SDF_THREAD_LOCAL: ThreadLocal<MutableMap<String, SimpleDateFormat>> =
        object : ThreadLocal<MutableMap<String, SimpleDateFormat>>() {
            override fun initialValue(): MutableMap<String, SimpleDateFormat> {
                return HashMap(16)
            }
        }
    private val defaultFormat: SimpleDateFormat
        get() = getSafeDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    fun getSafeDateFormat(pattern: String): SimpleDateFormat {
        val sdfMap: MutableMap<String, SimpleDateFormat>? = SDF_THREAD_LOCAL.get()
        var simpleDateFormat = sdfMap?.get(pattern)
        if (simpleDateFormat == null) {
            simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            sdfMap?.put(pattern, simpleDateFormat)
        }
        return simpleDateFormat
    }
    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis The milliseconds.
     * @param format The format.
     * @return the formatted time string
     */
    /**
     * Milliseconds to the formatted time string.
     *
     *
     * The pattern is `yyyy-MM-dd HH:mm:ss`.
     *
     * @param millis The milliseconds.
     * @return the formatted time string
     */
    @JvmOverloads
    fun millis2String(millis: Long, format: DateFormat = defaultFormat): String {
        return format.format(Date(millis))
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis  The milliseconds.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the formatted time string
     */
    fun millis2String(millis: Long, pattern: String): String {
        return millis2String(millis, getSafeDateFormat(pattern))
    }
}
