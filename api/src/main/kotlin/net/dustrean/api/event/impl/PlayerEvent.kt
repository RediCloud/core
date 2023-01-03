package net.dustrean.api.event.impl

import net.dustrean.api.event.CoreEvent
import net.dustrean.api.event.EventType
import net.dustrean.api.language.placeholder.PlaceholderProvider
import net.dustrean.api.language.placeholder.PlaceholderReference
import net.dustrean.api.player.IPlayer

abstract class PlayerEvent(type: EventType, val player: IPlayer) : CoreEvent(type), PlaceholderReference {
    override fun reference(provider: PlaceholderProvider) {
        provider.placeholderCollections+=player.getPlaceholders()
    }
}