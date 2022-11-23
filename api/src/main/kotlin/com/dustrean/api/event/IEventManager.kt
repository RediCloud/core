package com.dustrean.api.event

interface IEventManager {
    suspend fun registerListener(listener: CoreListener)

    suspend fun unregisterListener(listener: CoreListener)

    fun callEvent(event: IEvent)
}