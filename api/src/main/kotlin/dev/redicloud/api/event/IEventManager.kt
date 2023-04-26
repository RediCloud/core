package dev.redicloud.api.event

import dev.redicloud.api.ICoreAPI

interface IEventManager {
    suspend fun registerListener(listener: Any)

    suspend fun unregisterListener(listener: Any)

    fun callEvent(event: CoreEvent)
}

suspend inline fun <reified T : CoreEvent> listenCoreEvent(crossinline block: T.() -> Unit) {
    ICoreAPI.INSTANCE.eventManager.registerListener(object {
        @CoreListener
        fun onEvent(event: T) = block(event)
    })
}