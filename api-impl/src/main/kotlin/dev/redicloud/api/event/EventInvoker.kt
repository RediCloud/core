package dev.redicloud.api.event

import java.lang.reflect.Method

class EventInvoker(
    val listener: Any,
    val method: Method
) {

    fun invoke(event: CoreEvent) {
        method.invoke(listener, event)
    }

}