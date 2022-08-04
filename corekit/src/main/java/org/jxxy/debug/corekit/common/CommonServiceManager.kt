package org.jxxy.debug.corekit.common

import java.util.*

object CommonServiceManager {
    val cacheMap: HashMap<String, ServiceLoader<*>> = hashMapOf()

    inline fun <reified T> service(): T? {
        getService<T>()?.let {
            val iterator = it.iterator()
            while (iterator.hasNext()) {
                return (iterator.next() as? T)
            }
        }
        return null
    }

    inline fun <reified T> service(block: (T) -> Unit) {
        getService<T>()?.let { loader ->
            val iterator = loader.iterator()
            while (iterator.hasNext()) {
                (iterator.next() as? T)?.let {
                    block(it)
                }
            }
        }
    }

    inline fun <reified T> getService(): ServiceLoader<*>? {
        val c = T::class.java
        val name = c.name
        var service: ServiceLoader<*>? = cacheMap[name]
        if (service == null) {
            service = ServiceLoader.load(c)
            cacheMap[name] = service
        }
        return service
    }
}
