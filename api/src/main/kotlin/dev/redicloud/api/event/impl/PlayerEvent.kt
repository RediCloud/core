package dev.redicloud.api.event.impl

import dev.redicloud.api.event.CoreEvent
import dev.redicloud.api.event.EventType
import dev.redicloud.api.language.placeholder.PlaceholderProvider
import dev.redicloud.api.language.placeholder.PlaceholderReference
import dev.redicloud.api.player.IPlayer

abstract class PlayerEvent(type: EventType, val player: IPlayer) : CoreEvent(type), PlaceholderReference {
    override fun reference(provider: PlaceholderProvider) {
        provider.placeholderCollections+=player.getPlaceholders()
    }
}