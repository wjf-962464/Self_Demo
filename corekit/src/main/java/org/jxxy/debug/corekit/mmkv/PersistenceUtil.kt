package org.jxxy.debug.corekit.mmkv

import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV
import org.jxxy.debug.corekit.common.BaseApplication
import org.jxxy.debug.corekit.gson.GsonManager

object PersistenceUtil {
    const val DEFAULT_STRING = ""
    const val DEFAULT_INT = -1
    const val DEFAULT_BOOLEAN = false
    const val DEFAULT_FLOAT = -1f
    const val DEFAULT_LONG = -1L
    val mkv: MMKV by lazy {
        try {
            MMKV.defaultMMKV()
        } catch (e: Exception) {
            MMKV.initialize(BaseApplication.context())
            MMKV.defaultMMKV()
        }
    }

    inline fun <reified T> putValue(key: String?, value: T?) {
        if (key.isNullOrEmpty() || value == null) return
        val editor: SharedPreferences.Editor = mkv.edit()
        try {
            when (T::class) {
                Boolean::class -> editor.putBoolean(key, value as Boolean)
                Int::class -> editor.putInt(key, value as Int)
                String::class -> editor.putString(key, value as String)
                Float::class -> editor.putFloat(key, value as Float)
                Long::class -> editor.putLong(key, value as Long)
                else -> {
                    editor.putString(key, GsonManager.instance.gson.toJson(value))
                }
            }
            editor.apply()
        } catch (e: Exception) {
            Logger.e(e, "PersistenceUtil putValue error")
        }
    }

    inline fun <reified T> getValue(key: String?): T? {
        if (key.isNullOrEmpty() || !mkv.containsKey(key)) return null
        return when (T::class) {
            Boolean::class -> mkv.getBoolean(key, DEFAULT_BOOLEAN) as? T
            Int::class -> mkv.getInt(key, DEFAULT_INT) as? T
            String::class -> mkv.getString(key, DEFAULT_STRING) as? T
            Float::class -> mkv.getFloat(key, DEFAULT_FLOAT) as? T
            Long::class -> mkv.getLong(key, DEFAULT_LONG) as? T
            else ->
                mkv.getString(key, DEFAULT_STRING) as T
        }
    }

    inline fun <reified T> getArrayValue(key: String?): MutableList<T>? {
        val value: String? = getValue(key)
        if (value.isNullOrEmpty()) return null
        return GsonManager.instance.gson.fromJson(
            value,
            object : TypeToken<MutableList<T>>() {}.type
        )
    }

    inline fun <reified T> getMapValue(key: String?): MutableMap<String, T>? {
        val value: String? = getValue(key)
        if (value.isNullOrEmpty()) return null
        return GsonManager.instance.gson.fromJson(
            value,
            object : TypeToken<MutableMap<String, T>>() {}.type
        )
    }
}
