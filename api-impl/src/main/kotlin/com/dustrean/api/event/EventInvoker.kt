package com.dustrean.api.event

import java.lang.reflect.Method

class EventInvoker(
    val listener: Any,
    val method: Method
){

    fun invoke(event: IEvent){
        method.invoke(listener, event)
    }

}