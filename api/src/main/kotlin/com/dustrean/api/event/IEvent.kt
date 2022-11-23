package com.dustrean.api.event

import java.io.Serializable

interface IEvent : Serializable{
    val type: EventType
}