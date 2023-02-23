package dev.redicloud.api.event.impl

import dev.redicloud.api.event.EventType
import dev.redicloud.api.language.placeholder.PlaceholderProvider
import dev.redicloud.api.player.IPlayer

class PlayerWithTargetEvent(type: EventType, player: IPlayer, val target: IPlayer) : PlayerEvent(type, player) {
    override fun reference(provider: PlaceholderProvider) {
        super.reference(provider)
        provider.placeholderCollections+=target.getPlaceholders("target")
    }
}