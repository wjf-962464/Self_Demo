package org.jxxy.debug.corekit.gson

import com.google.gson.TypeAdapterFactory

interface TypeAdapterService {
    fun registerTypeAdapterFactory(): List<TypeAdapterFactory>?
}
