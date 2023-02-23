package dev.redicloud.api.event

import java.io.Serializable

abstract class CoreEvent(val type: EventType) : Serializable