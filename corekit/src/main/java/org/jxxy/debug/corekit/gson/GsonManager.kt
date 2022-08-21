package org.jxxy.debug.corekit.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory

class GsonManager private constructor(builder: GsonManager.Builder) {

    val gson: Gson

    companion object Instance {
        lateinit var instance: GsonManager
            private set

        fun init(builder: GsonManager.Builder) {
            instance = GsonManager(builder)
        }
    }

    init {
        val gsonBuilder = GsonBuilder()
        builder.getTypeAdapterFactories().forEach {
            gsonBuilder.registerTypeAdapterFactory(it)
        }
        gson = gsonBuilder.create()
    }

    class Builder {
        private val typeAdapterFactories: MutableList<TypeAdapterFactory> = mutableListOf()

        fun registerTypeAdapterFactory(adapterFactory: TypeAdapterFactory): Builder {
            typeAdapterFactories.add(adapterFactory)
            return this
        }

        fun getTypeAdapterFactories(): List<TypeAdapterFactory> {
            return typeAdapterFactories
        }
    }
}
