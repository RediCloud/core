package net.dustrean.api.event

import java.lang.reflect.Method

class EventInvoker(
    val listener: Any,
    val method: Method
){

    fun invoke(event: net.dustrean.api.event.CoreEvent){
        method.invoke(listener, event)
    }

}