package net.dustrean.api.event

interface IEventManager {
    suspend fun registerListener(listener: Any)

    suspend fun unregisterListener(listener: Any)

    fun callEvent(event: CoreEvent)
}