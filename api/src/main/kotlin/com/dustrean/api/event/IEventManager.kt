package com.dustrean.api.event

interface IEventManager {
    fun registerListener(listener: CoreListener)

    fun unregisterListener(listener: CoreListener)

    fun callEvent(event: IEvent)
}