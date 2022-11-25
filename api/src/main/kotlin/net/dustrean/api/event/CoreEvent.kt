package net.dustrean.api.event

import java.io.Serializable

abstract class CoreEvent(val type: EventType) : Serializable