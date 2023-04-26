package dev.redicloud.api.event

import java.lang.reflect.Method

interface IEventManager {
    suspend fun registerListener(listener: Any)

    suspend fun unregisterListener(listener: Any)

    suspend fun registerListenerMethode(eventClass: Class<out CoreEvent>, method: Method, prio: Byte)

    fun callEvent(event: CoreEvent)
}