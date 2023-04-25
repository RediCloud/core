package dev.redicloud.api.event.impl

import dev.redicloud.api.ICoreAPI
import dev.redicloud.api.event.CoreEvent
import dev.redicloud.api.event.EventType

class CoreInitializedEvent(core: ICoreAPI) : CoreEvent(EventType.LOCAL)