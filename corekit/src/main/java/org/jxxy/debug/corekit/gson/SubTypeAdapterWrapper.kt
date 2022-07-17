package org.jxxy.debug.corekit.gson

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class SubTypeAdapterWrapper<T, R : T>(
    private val clazz: Class<R>,
    private val adapter: TypeAdapter<R>
) : TypeAdapter<T>() {
    override fun write(out: JsonWriter, value: T) {
        if (!clazz.isInstance(value)) {
            throw JsonSyntaxException("Expected a " + clazz.name + " but was " + value)
        }
        adapter.write(out, value as R)
    }

    override fun read(`in`: JsonReader): T {
        return adapter.read(`in`)
    }
}
